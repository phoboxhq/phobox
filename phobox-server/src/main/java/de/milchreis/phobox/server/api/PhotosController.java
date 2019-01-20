package de.milchreis.phobox.server.api;

import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.server.services.IPhotosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/photos")
public class PhotosController {

	@Autowired private IPhotosService photosService;

	@RequestMapping(value = "scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageStatus scanDirectory(Pageable pageable) {
		return scanDirectory("", pageable);
	}
	
	@RequestMapping(value = "scan/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageStatus scanDirectory(@PathVariable("dir") String directory, Pageable pageable) {
		return photosService.getStorageItemsByDirectory(photosService.convertItemPathToFileObject(directory), pageable);
	}
	
	@RequestMapping(value = "download/{path}", method = RequestMethod.GET)
	public void downloadDirectory(@PathVariable("path") String imagepath, HttpServletResponse response) throws IOException {
		File directory = photosService.convertItemPathToFileObject(imagepath);
		photosService.downloadDirectoryAsZip(directory, response);
	}
}
