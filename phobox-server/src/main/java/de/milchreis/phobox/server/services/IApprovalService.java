package de.milchreis.phobox.server.services;

import java.io.File;
import java.util.List;

public interface IApprovalService {

	void moveFileToIncoming(String webFilePath) throws Exception;
	void moveFileToIncoming(File file) throws Exception;
	
	void deleteFile(String webFilePath) throws Exception;
	void deleteFile(File file) throws Exception;
	
	List<String> getAllImagePathInApproval();
	
}
