package de.milchreis.phobox.server.services;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

	File storeFile(MultipartFile file) throws Exception;

}
