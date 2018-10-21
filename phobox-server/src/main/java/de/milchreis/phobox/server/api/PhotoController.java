package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.FilesystemHelper;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import de.milchreis.phobox.utils.exif.ExifHelper;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
	
	@Autowired private ItemRepository itemRepository;
	
	@RequestMapping(value = "{path}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageItem getInfo(@PathVariable("path") String imagepath) {
		
		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);
		
		if(!file.exists() || !ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
			return null;
		}

		return getItem(file);
	}
	
	@RequestMapping(value = "exif/{path}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, String> getExif(@PathVariable("path") String imagepath) 
	throws ImageProcessingException, IOException {
		
		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);
		
		if(!file.exists() || !ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
			return null;
		}
		
		return ExifHelper.getExifDataMap(file);
	}
	
	@RequestMapping(value = "rename/{dir}/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rename( @PathVariable("dir") String directory, @PathVariable("name") String targetname ) {
		
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
	
	@RequestMapping(value = "/delete/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rename( @PathVariable("dir") String directory ) {
		
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
	
	@RequestMapping(value = "move/{dir}/to/{targetDir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status directory( @PathVariable("dir") String directory, @PathVariable("targetDir") String targetDir ) {
		
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
		String directory = ops.getWebPath(file).replace(filename, "");
		
		String extention = FilenameUtils.getExtension(file.getName());
		extention = ListHelper.endsWith(extention.toLowerCase(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS) ? extention : "jpg";
		
		File thumbnail = ops.getThumb(new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + "." + extention));
		
		StorageItem item = new StorageItem();

		item.setName(FilenameUtils.getBaseName(file.getName()));
		item.setPath(ops.getWebPath(new File(directory, file.getName())));
		item.setType(file.isDirectory() ? StorageItem.TYPE_DIRECTORY : StorageItem.TYPE_FILE);
		
		Item dbItem = null;
		dbItem = itemRepository.findByFullPath(item.getPath());
		
		File rawFile = FilesystemHelper.getRawIfExists(file, PhoboxDefinitions.SUPPORTED_RAW_FORMATS);
		if(rawFile != null) {
			File raw = new File(directory, rawFile.getName().toString());
			item.setRaw(ops.getWebPath(raw));
		} 

		
		if(file.isDirectory()) {
			List<String> previewFiles = ops.getFiles(file, 1);
			if(previewFiles.size() >= 1) {
				
				item.setPreview(ops.getStaticResourcePath(model.getThumbPath(), previewFiles.get(0)));
				File previewFile = new File(model.getStoragePath(), previewFiles.get(0));
				
				if(!ops.getThumb(previewFile).exists()) {
					
					// Add to database and create thumbnail
					Phobox.getEventRegistry().onNewFile(previewFile);
					
					// Set waiting icon
					item.setGeneratingThumb(true);
				}
			}
			
		} else {
			
			item.setThumb(ops.getStaticResourcePath(thumbnail));

			// Add landscape/portrait information by database item
			if(dbItem != null && Objects.nonNull(dbItem.getWidth()) && Objects.nonNull(dbItem.getHeight())) {
				item.setLandscape(dbItem.getWidth() > dbItem.getHeight());
			}
			
			// Check existence of the thumbnails
			if(!thumbnail.exists()) {
				
				// Add to database and create thumbnail
				Phobox.getEventRegistry().onNewFile(file);
				
				// Set waiting icon
				item.setGeneratingThumb(true);
			}
		}
		
		return item;		
	}
}
