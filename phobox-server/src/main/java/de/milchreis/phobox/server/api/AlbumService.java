package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.Album;
import de.milchreis.phobox.core.model.AlbumOperation;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.db.DBManager;
import de.milchreis.phobox.db.entities.AlbumItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;


@Path("/album/")
public class AlbumService {
	private static Logger log = Logger.getLogger(AlbumService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAlbums() {
		
		try {
			return AlbumRepository.getAlbums().stream()
					.map(a -> a.getName())
					.collect(Collectors.toList());
		
		} catch (SQLException | IOException e) {
			log.error("Error while scanning album directory", e);
		}
		
		return null;
	}

	@GET
	@Path("{album}")
	@Produces(MediaType.APPLICATION_JSON)
	public Album getAlbumContent(@PathParam("album") String albumName) {
		
		PhoboxModel model = Phobox.getModel();
		PhotoService photoService = new PhotoService();
		Album album = new Album(albumName);

		try {
			List<AlbumItem> albumItems = AlbumRepository.getAlbumItemsByAlbumName(albumName);
			for(AlbumItem dbAlbum : albumItems) {
				Item item = dbAlbum.getItem();
				album.addItem(photoService.getItem(new File(model.getStoragePath(), item.getPath() + item.getName())));
			}
		
		} catch (SQLException | IOException e) {
			log.error("Error while scanning album directory", e);
		}
		
		return album;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Status addToAlbum(AlbumOperation op) {
		
		String albumname = op.getName();
		Status status = new Status(Status.OK);

		try {
			de.milchreis.phobox.db.entities.Album album = AlbumRepository.getAlbumByName(albumname);
			
			// Create if not exists
			if(album == null) {
				album = new de.milchreis.phobox.db.entities.Album();
				album.setName(albumname);
				DBManager.store(album, de.milchreis.phobox.db.entities.Album.class);
				album = AlbumRepository.getAlbumByName(albumname);
			}
			
			Item item = ItemRepository.getItem(op.getItem());
			if(item == null) {
				Phobox.getEventRegistry().onNewFile(new File(Phobox.getModel().getStoragePath(), op.getItem()));
				item = ItemRepository.getItem(op.getItem());
			}
			
			AlbumItem albumItem = new AlbumItem();
			albumItem.setAlbum(album);
			albumItem.setItem(item);
			
			DBManager.store(albumItem, AlbumItem.class);
			
		} catch (SQLException | IOException e) {
			status.setStatus(Status.ERROR);
			log.error("Error while adding item to album", e);
		}
		
		return status;
	}
		
}
