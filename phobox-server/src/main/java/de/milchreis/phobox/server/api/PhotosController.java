package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.file.filter.DirectoryFilter;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.FilesystemHelper;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import de.milchreis.phobox.utils.ZipStreamHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/photos")
public class PhotosController {
	
	@Autowired private ItemRepository itemRepository;
	@Autowired private PhotoController photoController;

	@RequestMapping(value = "scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageStatus scanDirectory(Pageable pageable) {
		return scanDirectory("", pageable);
	}
	
	@RequestMapping(value = "scan/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageStatus scanDirectory(@PathVariable("dir") String directory, Pageable pageable) {
		
		directory = PathConverter.decode(directory);

		PhoboxModel model = Phobox.getModel();
		PhoboxOperations ops = Phobox.getOperations();
		
		File storage = new File(model.getStoragePath());
		File dir = new File(storage, directory);
		boolean isRoot = directory.equals("/");
		
		if(directory == null || directory.isEmpty())
			dir = storage;
		
		// Update directory on database if it is no fragment
		if(pageable == null || pageable.getPageNumber() == 0) {
			Phobox.addPathToScanQueue(dir);
		}
		
		StorageStatus response = new StorageStatus();
		response.setName(ops.getElementName(dir));
		response.setPath(directory);
		
		if(dir.isDirectory()) {
			response.setType(StorageStatus.TYPE_DIRECTORY);
		} else {
			response.setType(StorageStatus.TYPE_FILE);
		}
	
		// Find including directories (but add it not by fragment scan)
		if(pageable == null || pageable.getPageNumber() == 0) {
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
					if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
						response.add(photoController.getItem(file));
					}
				}
				
			} catch(IOException e) {
				log.warn("Error while scanning directory: " + e.getLocalizedMessage());
			}
		}
		
		// Scan files from database
		List<Item> items;
		if(pageable != null) {
			items = itemRepository.findByPath(directory+"/", pageable).getContent();
			response.setFragment(items.size() > 0);
		} else {
			items = itemRepository.findByPath(directory+"/");
		}

		for(Item item : items) {
			File file = ops.getPhysicalFile(item.getFullPath());
			response.add(photoController.getItem(file));
		}
		
		try {
			if(items.size() == 0 && !FilesystemHelper.isDirEmpty(dir.toPath())) {
				response.setProcessing(true);
			}
		} catch (IOException e) {
			log.error("", e);
		}
			
		return response;
	}
	
	@RequestMapping(value = "download/{path}", method = RequestMethod.GET)
	public void downloadDirectory(@PathVariable("path") String imagepath, HttpServletResponse response) throws IOException {

		imagepath = PathConverter.decode(imagepath);
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		File file = new File(storage, imagepath);

		if(!file.isDirectory()) {
			return;
		}
		
	    response.setStatus(HttpServletResponse.SC_OK);
	    response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + ".zip\"");
	    response.setContentType("txt/plain");

	    ZipStreamHelper zip = new ZipStreamHelper();
	    zip.compressToStream(file, response.getOutputStream());
	}
}
