package de.milchreis.phobox.server.services;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.StorageAlbum;
import de.milchreis.phobox.db.entities.Album;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;

@Service
public class AlbumService implements IAlbumService {
	
	@Autowired private IPhotoService photoService;
	
	@Autowired private AlbumRepository albumRepository;
	@Autowired private ItemRepository itemRepository;
	
	
	public List<String> getAllAlbumNames() {
		List<String> albumnames = new ArrayList<>();
		albumRepository.findAll().forEach(album -> albumnames.add(album.getName()));
		return albumnames;
	}
	
	public StorageAlbum getStorageAlbumsByName(String albumname) {
		
		PhoboxOperations ops = Phobox.getOperations();
		
		return new StorageAlbum(
				albumname, 
				albumRepository.findByName(albumname)
					.getItems().stream()
					.map(i -> photoService.getItem(ops.getPhysicalFile(i.getFullPath())))
					.collect(Collectors.toList())
		);
	}
	
	public void addItemToAlbum(String albumName, String itemPath) {
		Album album = albumRepository.findByName(albumName);

		// Create if not exists
		if (album == null) {
			album = new Album();
			album.setName(albumName);
			album.setCreation(new Date(System.currentTimeMillis()));
			album.setItems(new LinkedHashSet<Item>());
		}

		Item item = itemRepository.findByFullPath(itemPath);

		if (item == null) {
			Phobox.getEventRegistry().onNewFile(new File(Phobox.getModel().getStoragePath(), itemPath));
			item = itemRepository.findByFullPath(itemPath);
		}

		item.getAlbums().add(album);
		itemRepository.save(item);
	}

}
