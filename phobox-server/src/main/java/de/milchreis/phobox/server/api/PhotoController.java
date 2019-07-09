package de.milchreis.phobox.server.api;

import java.io.IOException;
import java.util.Map;

import de.milchreis.phobox.utils.exif.ExifContainer;
import de.milchreis.phobox.utils.exif.ExifItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.server.services.IPhotoService;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {
	
	@Autowired private IPhotoService photoService;
	
	@RequestMapping(value = "{path}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageItem getInfo(@PathVariable("path") String imagepath) {
		return photoService.getItem(imagepath);
	}
	
	@RequestMapping(value = "exif/{path}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ExifContainer getExif(@PathVariable("path") String imagepath)
	throws ImageProcessingException, IOException {
		return photoService.getExifData(imagepath);
	}
	
	@RequestMapping(value = "rename/{dir}/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rename(@PathVariable("dir") String directory, @PathVariable("name") String targetname) {
		try {
			photoService.rename(directory, targetname);
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "/delete/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rename(@PathVariable("dir") String directory) {
		try {
			photoService.delete(directory);
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "move/{dir}/to/{targetDir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status directory(@PathVariable("dir") String directory, @PathVariable("targetDir") String targetDir) {
		try {
			photoService.move(directory, targetDir);
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}	
	
}
