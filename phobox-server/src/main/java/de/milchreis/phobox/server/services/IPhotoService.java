package de.milchreis.phobox.server.services;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;

public interface IPhotoService {

	StorageItem getItem(File physicalFile);
	StorageItem getItem(String webFilePath);
	StorageItem getItem(Item item);

	Map<String, String> getExifData(String webFilePath) throws ImageProcessingException, IOException;
	
	void rename(String webItemPath, String targetname) throws Exception;
	
	void delete(String webItemPath) throws Exception;
	
	void move(String webItemPath, String targetDir) throws Exception;

}
