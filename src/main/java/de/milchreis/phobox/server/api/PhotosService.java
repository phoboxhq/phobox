package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.file.filter.DirectoryFilter;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.db.ItemAccess;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.FilesystemHelper;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import de.milchreis.phobox.utils.ZipStreamHelper;

@Path("/photos/")
public class PhotosService {
	private static Logger log = Logger.getLogger(PhotoService.class);

	@GET
	@Path("scan")
	@Produces(MediaType.APPLICATION_JSON)
	public StorageStatus scanDirectory() {
		return scanDirectory("");
	}
	
	@GET
	@Path("scan/{dir}")
	@Produces(MediaType.APPLICATION_JSON)
	public StorageStatus scanDirectory(@PathParam("dir") String directory) {
		return scanDirectory(directory, null);
	}
	
	@GET
	@Path("scan/{dir}/{lastItemIndex}")
	@Produces(MediaType.APPLICATION_JSON)
	public StorageStatus scanDirectory( 
			@PathParam("dir") String directory, 
			@PathParam("lastItemIndex") Integer lastIndex ) 
	{

		directory = PathConverter.decode(directory);
		lastIndex = lastIndex == null ? 0 : lastIndex;

		PhotoService photoService = new PhotoService();
		PhoboxModel model = Phobox.getModel();
		PhoboxOperations ops = Phobox.getOperations();
		
		File storage = new File(model.getStoragePath());
		File dir = new File(storage, directory);
		boolean isRoot = directory.equals("/");
		
		if(directory == null || directory.isEmpty())
			dir = storage;
		
		// Update directory on database if it is no fragment
		if(lastIndex == 0) {
			new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, dir, false).start();
		}
		
		StorageStatus response = new StorageStatus();
		response.setName(ops.getElementName(dir));
		response.setPath(directory);
		
		if(dir.isDirectory()) {
			response.setType(StorageStatus.TYPE_DIRECTORY);
		} else {
			response.setType(StorageStatus.TYPE_FILE);
		}
	
		// Find including directories
		DirectoryStream<java.nio.file.Path> stream = null;
		try {
			stream = Files.newDirectoryStream(dir.toPath(), new DirectoryFilter());
			for(java.nio.file.Path path : stream) {
				File file = path.toFile();
				
				// Skip internal phobox directory and hidden files
				if((isRoot && file.isDirectory() && file.getName().equals("phobox")) || file.isHidden()) {
					continue;
				}
				
				// select supported formats and directories only
				if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxConfigs.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
					response.add(photoService.getItem(file));
				}
			}
		
		} catch(IOException e) {
			log.warn("Error while scanning directory: " + e.getLocalizedMessage());
		}
		
		// Scan files from database
		try {
						
			List<Item> items = ItemAccess.getItemsByPath(directory, lastIndex, model.getImgPageSize() + 1); 

			for(Item item : items) {
				File file = ops.getPhysicalFile(item.getPath() + item.getName());
				response.add(photoService.getItem(file));
			}
			
			// More images available
			if(items.size() == model.getImgPageSize() + 1) {
				response.setFragment(true);
				items.remove(items.size()-1);
			}
			
			if(items.size() == 0 && !FilesystemHelper.isDirEmpty(dir.toPath())) {
				response.setProcessing(true);
			}
			
		} catch (SQLException | IOException e) {
			log.warn("Error while scanning database: " + e.getLocalizedMessage());
		}
		
		return response;
	}
	
	@GET
	@Path("download/{path}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadDirectory(@PathParam("path") String imagepath) {

		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);

		if(!file.isDirectory()) {
			return Response.noContent().build();
		}

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				ZipStreamHelper zip = new ZipStreamHelper();
				zip.compressToStream(file, os);
			}
		};

		return Response
				.ok(stream)
				.header("Content-Disposition",  "attachment; filename=\""+file.getName()+".zip\"")
				.build();
	}
}
