package de.milchreis.phobox.server.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalService implements IApprovalService {
	
	@Override
	public void moveFileToIncoming(String webFilePath) throws Exception {
		moveFileToIncoming(decodeToPhysicalFile(webFilePath));
	}
	
	@Override
	public void moveFileToIncoming(File file) throws Exception {
		FileUtils.moveFileToDirectory(file, Phobox.getModel().getIncomingPath(), false);
	}

	@Override
	public void deleteFile(String webFilePath) throws Exception {
		deleteFile(decodeToPhysicalFile(webFilePath));
	}

	@Override
	public void deleteFile(File file) throws Exception {
		if(file != null && file.exists())
			file.delete();
	}

	@Override
	public List<String> getAllImagePathInApproval() {
		
		PhoboxOperations ops = Phobox.getOperations();
		File directory = Phobox.getModel().getApprovalPath();
		List<String> files = new ArrayList<>();
		
		DirectoryStream<java.nio.file.Path> stream = null;
		try {
			stream = Files.newDirectoryStream(directory.toPath());
			for(Path path : stream) {
				File file = path.toFile();
				
				// select supported formats and directories only
				if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS)) {
					files.add(ops.getStaticResourcePath(file));
//					files.add("ext" + ops.getWebPath(file.getPath()));
				}
			}
		} catch(IOException e) {
			log.warn("Error while scanning directory: " + e.getLocalizedMessage());
		}
		
		return files;
	}
	
	
	private File decodeToPhysicalFile(String webPath) {
		String filePathCleaned = PathConverter.decode(webPath);
		return Phobox.getOperations().getPhysicalFile(filePathCleaned);
	}
}
