package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/approval")
public class ApprovalController {
	
	@RequestMapping(value = "accept", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status accept(@RequestBody String filename) {
		
		filename = PathConverter.decode(filename);
		File file = Phobox.getOperations().getPhysicalFile(filename);
		
		Status status = new Status();
		
		try {
			FileUtils.moveFileToDirectory(file, Phobox.getModel().getIncomingPath(), false);
			status.setStatus(Status.OK);

		} catch (IOException e) {
			log.error("Error while approval -> accept", e);
			status.setStatus(Status.ERROR);
		}

		return status;
	}
	
	@RequestMapping(value = "decline", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status decline(@RequestBody String filename) {
		
		filename = PathConverter.decode(filename);
		File file = Phobox.getOperations().getPhysicalFile(filename);
		
		Status status = new Status();
		
		try {
			file.delete();
			status.setStatus(Status.OK);

		} catch (Exception e) {
			log.error("Error while approval -> decline", e);
			status.setStatus(Status.ERROR);
		}

		return status;
	}
	
	@RequestMapping(value = "scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> scanDirectory() {

		PhoboxOperations ops = Phobox.getOperations();
		
		File directory = Phobox.getModel().getApprovalPath();
		
		List<String> files = new ArrayList<>();
		
		DirectoryStream<java.nio.file.Path> stream = null;
		try {
			stream = Files.newDirectoryStream(directory.toPath());
			for(java.nio.file.Path path : stream) {
				File file = path.toFile();
				
				// select supported formats and directories only
				if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS)) {
					files.add("ext" + ops.getWebPath(file.getPath()));
				}
			}
		} catch(IOException e) {
			log.warn("Error while scanning directory: " + e.getLocalizedMessage());
		}
		
		return files;
	}
	
}