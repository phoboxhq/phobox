package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


@Path("/album/")
public class AlbumService {
	private static Logger log = Logger.getLogger(AlbumService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAlbums() {
		
		PhoboxModel model = Phobox.getModel();
		File albumPath = model.getAlbumPath();
		List<File> albums = new ArrayList<>();
		
		try {
			DirectoryStream<java.nio.file.Path> stream = Files.newDirectoryStream(albumPath.toPath());
			
			// for each file check and add to response
			for(java.nio.file.Path path : stream) {
				albums.add(path.toFile());
			}
			
		} catch(IOException e) {
			log.error("Error while scanning album directory", e);
		}
		
		Collections.sort(albums, new Comparator<File>() {
			@Override public int compare(File o1, File o2) {
				return o1.lastModified() < o2.lastModified() ? 1 : 0;
			}
		});
		
		List<String> albumNames = new ArrayList<>();
		albums.forEach(f -> albumNames.add(f.getName()));
		
		return albumNames;
		
//		List<Album> albumOverview = new ArrayList<>();
//		albums.forEach(f -> {
//			Album album = new Album(f.getName());
//			album.addItem(photoService.getItem(new File(albumPath, model.getFiles(f, 1).get(0)), model));
//			albumOverview.add(album);
//		});
//		
//		return albumOverview;
	}

	@GET
	@Path("{album}")
	@Produces(MediaType.APPLICATION_JSON)
	public Album getAlbumContent(@PathParam("album") String albumName) {
		
		PhoboxModel model = Phobox.getModel();
		File albumPath = new File(model.getAlbumPath(), albumName);
		PhotoService photoService = new PhotoService();
		
		Album album = new Album(albumName);
		
		try {
			DirectoryStream<java.nio.file.Path> stream = Files.newDirectoryStream(albumPath.toPath());

			// for each file check and add to response
			for(java.nio.file.Path path : stream) {
				java.nio.file.Path target = Files.readSymbolicLink(path);
				album.addItem(photoService.getItem(target.toFile()));
			}
			
		} catch(NoSuchFileException e) {
			log.warn("Album not found: " + e.getLocalizedMessage());
			
		} catch(IOException e) {
			log.error("Error while scanning album", e);
		}
		
		return album;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Status addToAlbum(AlbumOperation op) {
		
		PhoboxModel model = Phobox.getModel();
		File item = new File(model.getStoragePath(), op.getItem());
		Status status = new Status(Status.OK);
		
		try {
			Phobox.getOperations().addToAlbum(item, op.getName());
		} catch (IOException e) {
			status.setStatus(Status.ERROR);
			log.error("Error while adding item to album", e);
		}
		
		return status;
	}
	
}
