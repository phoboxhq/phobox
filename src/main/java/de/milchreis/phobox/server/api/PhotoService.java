package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.utils.ExifHelper;
import de.milchreis.phobox.utils.FilesystemManager;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;


@Path("/photo/")
public class PhotoService {
	
	@GET
	@Path("{path}")
	@Produces(MediaType.APPLICATION_JSON)
	public StorageItem getInfo(@PathParam("path") String imagepath) {
		
		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);
		
		if(!file.exists() || !ListHelper.endsWith(file.getName(), PhoboxConfigs.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
			return null;
		}

		return getItem(file);
	}
	
	@GET
	@Path("exif/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> getExif(@PathParam("path") String imagepath) 
	throws ImageProcessingException, IOException {
		
		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);
		
		if(!file.exists() || !ListHelper.endsWith(file.getName(), PhoboxConfigs.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
			return null;
		}
		
		return ExifHelper.getExifDataMap(file);
	}
	
	@GET
	@Path("rename/{dir}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status rename( @PathParam("dir") String directory, @PathParam("name") String targetname ) {
		
		directory = directory.replace("%2F", "/");
		
		Status response = new Status();
		PhoboxModel model = Phobox.getModel();
		File dir = new File(model.getStoragePath(), directory);
		
		try {
			Phobox.getOperations().rename(dir, targetname);
			response.setStatus(Status.OK);
		} catch(Exception e) {
			response.setStatus(Status.ERROR);
		}
		
		return response;
	}
	
	@GET
	@Path("delete/{dir}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status rename( @PathParam("dir") String directory ) {
		
		directory = directory.replace("%2F", "/");
		
		Status response = new Status();
		PhoboxModel model = Phobox.getModel();
		File item = new File(model.getStoragePath(), directory);
		
		try {
			Phobox.getOperations().delete(item);
			response.setStatus(Status.OK);
			
		} catch(Exception e) {
			response.setStatus(Status.ERROR);
		}
		
		return response;
	}
	
	@GET
	@Path("move/{dir}/to/{targetDir}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status directory( @PathParam("dir") String directory, @PathParam("targetDir") String targetDir ) {
		
		Status response = new Status();
		
		PhoboxOperations ops = Phobox.getOperations();
		PhoboxModel model = Phobox.getModel();
		File dir = new File(model.getStoragePath(), directory);
		File tar = new File(model.getStoragePath(), targetDir);
		
		if(!tar.exists()) {
			tar.mkdirs();
		}
		
		if(dir.isFile() && tar.isDirectory()) {
			try {
				ops.moveFile(dir, tar);
				response.setStatus(Status.OK);
			} catch (IOException e) {
				response.setStatus(Status.ERROR);
			}
		}
		
		if(dir.isDirectory() && tar.isDirectory()) {
			try {
				ops.moveDir(dir, tar);
				response.setStatus(Status.OK);
			} catch (IOException e) {
				response.setStatus(Status.ERROR);
			}
		}
		
		return response;
	}	
	
	public StorageItem getItem(File file) {
		
		PhoboxOperations ops = Phobox.getOperations();
		PhoboxModel model = Phobox.getModel();
		
		String filename = file.getName();
		String directory = file.getAbsolutePath()
				.replace(model.getStoragePath(), "")
				.replace(filename, "");
		
		StorageItem item = new StorageItem();

		item.setName(FilenameUtils.getBaseName(file.getName()));
		item.setTime(ops.getElementTimestamp(file));
		item.setPath(ops.getWebPath(new File(directory, file.getName())));
		item.setType(file.isDirectory() ? StorageItem.TYPE_DIRECTORY : StorageItem.TYPE_FILE);
		
		File rawFile = FilesystemManager.getRawIfExists(file, PhoboxConfigs.SUPPORTED_RAW_FORMATS);
		if(rawFile != null) {
			File raw = new File(directory, rawFile.getName().toString());
			item.setRaw(ops.getWebPath(raw));
		} 

		String fileExtension = FilenameUtils.getExtension(file.getName()).toLowerCase();
		
		if(file.isDirectory()) {
			List<String> previewFiles = ops.getFiles(file, 1);
			if(previewFiles.size() >= 1) {
				item.setPreview(ops.getWebPath(model.getThumbPath()) + "/low/"+previewFiles.get(0));
			}
			
		} else if(ListHelper.endsWith(fileExtension, PhoboxConfigs.SUPPORTED_VIEW_FORMATS)) {

			item.setThumbLow(ops.getWebPath(model.getThumbPath()) + "/low/" + item.getPath());
			item.setThumbHigh(ops.getWebPath(model.getThumbPath()) + "/high/" + item.getPath());

			// Check existence of the thumbnails
			if (!new File(model.getStoragePath(), item.getThumbHigh()).exists() || 
				!new File(model.getStoragePath(), item.getThumbLow()).exists()) {

				// Start creating thumb-nails
				Phobox.processThumbnails(file);
				
				// Set waiting icon
				item.setThumbLow("img/stopwatch.png");
				item.setThumbHigh("img/stopwatch.png");
			}
		}
		
		return item;		
	}
}
