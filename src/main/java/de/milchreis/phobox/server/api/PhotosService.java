package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

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
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.StorageStatus;
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
	public StorageStatus scanDirectory( @PathParam("dir") String directory ) {

		directory = PathConverter.decode(directory);

		PhotoService photoService = new PhotoService();
		PhoboxModel model = Phobox.getModel();
		PhoboxOperations ops = Phobox.getOperations();
		File storage = new File(model.getStoragePath());
		File dir = new File(storage, directory);
		boolean isRoot = directory.equals("/");
		
		if(directory == null || directory.isEmpty())
			dir = storage;

		StorageStatus response = new StorageStatus();
		response.setName(ops.getElementName(dir));
		response.setPath(directory);
		
		if(dir.isDirectory()) {
			response.setType(StorageStatus.TYPE_DIRECTORY);
		} else {
			response.setType(StorageStatus.TYPE_FILE);
		}
		
		DirectoryStream<java.nio.file.Path> stream = null;
		try {
			stream = Files.newDirectoryStream(dir.toPath());
			
			// for each file check and add to response
			for(java.nio.file.Path path : stream) {
				File file = path.toFile();
				
				// Skip internal phobox directory
				if(isRoot && file.isDirectory() && file.getName().equals("phobox")) {
					continue;
				}
				
				// select supported formats and directories only
				if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxConfigs.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
					response.add(photoService.getItem(file));
				}
			}
		} catch (IOException e) {
			log.warn("Error while scanning directory: " + e.getLocalizedMessage());
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
