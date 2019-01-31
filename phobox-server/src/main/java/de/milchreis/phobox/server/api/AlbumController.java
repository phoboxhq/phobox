package de.milchreis.phobox.server.api;

import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageAlbum;
import de.milchreis.phobox.server.api.requestmodel.AddToAlbumRequest;
import de.milchreis.phobox.server.services.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

	@RequestMapping(value = "download/{albumname}", method = RequestMethod.GET)
	public void downloadAlbum(@PathVariable("albumname") String albumname, HttpServletResponse response) throws IOException {
		albumService.downloadAlbumAsZip(albumname, response);
	}

}
