package de.milchreis.phobox.server.services;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.core.model.exif.ExifContainer;

public interface IPhotoService {

	StorageItem getItem(File physicalFile);
	StorageItem getItem(String webFilePath);
	StorageItem getItem(Item item);

	ExifContainer getExifData(String webFilePath) throws ImageProcessingException, IOException;
	
	void rename(String webItemPath, String targetname) throws Exception;
	
	void delete(String webItemPath) throws Exception;
	
	void move(String webItemPath, String targetDir) throws Exception;

}
