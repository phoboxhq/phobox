package de.milchreis.phobox.server.services;

import java.util.List;

import de.milchreis.phobox.core.model.StorageAlbum;

public interface IAlbumService {

	List<String> getAllAlbumNames();
	
	StorageAlbum getStorageAlbumsByName(String albumname);
	
	void addItemToAlbum(String albumName, String itemPath);
	
}
