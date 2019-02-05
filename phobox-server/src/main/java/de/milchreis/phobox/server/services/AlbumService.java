package de.milchreis.phobox.server.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.exceptions.AlbumException;
import de.milchreis.phobox.utils.storage.ZipStreamHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.StorageAlbum;
import de.milchreis.phobox.db.entities.Album;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;

import javax.servlet.http.HttpServletResponse;

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
	
	public StorageAlbum getStorageAlbumsByName(String albumname) throws AlbumException {
		
		PhoboxOperations ops = Phobox.getOperations();

		Album album = albumRepository.findByName(albumname);

		if(album == null)
			throw new AlbumException("The album '" + albumname + "' not exists");

		return new StorageAlbum(
				albumname, 
				album
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

	@Override
	public void downloadAlbumAsZip(String albumname, HttpServletResponse response) throws IOException {

		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\"" + albumname + ".zip\"");
		response.setContentType("txt/plain");

		String storagePath = Phobox.getModel().getStoragePath();
		Album album = albumRepository.findByName(albumname);

		List<File> files = album.getItems().stream()
				.map(i -> new File(storagePath, i.getFullPath()))
				.collect(Collectors.toList());

		ZipStreamHelper zip = new ZipStreamHelper();
		zip.compressToStream(files, response.getOutputStream());
	}

	@Override
	public void deleteAlbum(String albumname) throws AlbumException {
		if(albumname == null || albumname.isEmpty())
			throw new AlbumException("The given albumname is empty");

		Album album = albumRepository.findByName(albumname);

		if(album == null)
			throw new AlbumException("Album '" + albumname + "' not found");

		albumRepository.delete(album);
	}

}
