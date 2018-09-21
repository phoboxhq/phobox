package de.milchreis.phobox.server.services;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.milchreis.phobox.core.Phobox;

@Service
public class FileStorageService implements IFileStorageService {

	@Override
	public File storeFile(MultipartFile contents) throws IOException {
		File incomingFile = new File(Phobox.getModel().getIncomingPath(), contents.getOriginalFilename());
		Files.copy(contents.getInputStream(), incomingFile.toPath(), REPLACE_EXISTING);
		return incomingFile;
	}

}
