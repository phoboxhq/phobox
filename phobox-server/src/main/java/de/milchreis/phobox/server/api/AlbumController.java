package de.milchreis.phobox.server.api;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageAlbum;
import de.milchreis.phobox.db.entities.Album;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.server.api.requestmodel.AddToAlbumRequest;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
	
	@Autowired private AlbumRepository albumRepository;
	@Autowired private ItemRepository itemRepository;
	@Autowired private PhotoController photoController;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> getAlbums() {
		
		List<String> albumnames = new ArrayList<>();
		for(Album a : albumRepository.findAll()) {
			albumnames.add(a.getName());
		}
		return albumnames;
	}

	@RequestMapping(value = "{albumname}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageAlbum getAlbumContent(@PathVariable String albumname) {
		
		PhoboxOperations ops = Phobox.getOperations();
		
		return new StorageAlbum(
				albumname, 
				albumRepository.findByName(albumname)
					.getItems().stream()
					.map(i -> photoController.getItem(ops.getPhysicalFile(i.getFullPath())))
					.collect(Collectors.toList())
		);
	}
	
	@RequestMapping(value = "", 
			method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status addItemToAlbum(@RequestBody AddToAlbumRequest request) {
		
		Album album = albumRepository.findByName(request.getAlbumName());
		
		// Create if not exists
		if(album == null) {
			album = new Album();
			album.setName(request.getAlbumName());
			album.setCreation(new Date(System.currentTimeMillis()));
			album.setItems(new LinkedHashSet<Item>());
		}
		
		Item item = itemRepository.findByFullPath(request.getItemPath());
		
		if(item == null) {
			Phobox.getEventRegistry().onNewFile(new File(Phobox.getModel().getStoragePath(), request.getItemPath()));
			item = itemRepository.findByFullPath(request.getItemPath());
		}
		
		item.getAlbums().add(album);
		itemRepository.save(item);
		
		return new Status(Status.OK);
	}
		
}
