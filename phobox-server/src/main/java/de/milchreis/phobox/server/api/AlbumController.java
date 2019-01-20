package de.milchreis.phobox.server.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageAlbum;
import de.milchreis.phobox.server.api.requestmodel.AddToAlbumRequest;
import de.milchreis.phobox.server.services.IAlbumService;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
	
	@Autowired private IAlbumService albumService;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> getAlbums() {
		return albumService.getAllAlbumNames();
	}

	@RequestMapping(value = "{albumname}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public StorageAlbum getAlbumContent(@PathVariable String albumname) {
		return albumService.getStorageAlbumsByName(albumname);
	}
	
	@RequestMapping(value = "", 
			method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status addItemToAlbum(@RequestBody AddToAlbumRequest request) {
		albumService.addItemToAlbum(request.getAlbumName(), request.getItemPath());
		return new Status(Status.OK);
	}
		
}
