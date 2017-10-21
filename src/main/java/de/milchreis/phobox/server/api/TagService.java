package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.core.model.TagOperation;
import de.milchreis.phobox.db.DBManager;
import de.milchreis.phobox.db.ItemAccess;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.entities.ItemTag;
import de.milchreis.phobox.utils.PathConverter;

@Path("/tags/")
public class TagService {
	private static final Logger log = Logger.getLogger(TagService.class);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Status saveTags(TagOperation tagOps) {
		
		Status status = new Status(Status.OK);
		String path = Phobox.getOperations().getWebPath(tagOps.getItem());
		path = PathConverter.decode(path);
		File physicalFile = Phobox.getOperations().getPhysicalFile(path);
		PhotosService ps = new PhotosService();
		
		List<String> items = new ArrayList<>();
		items.add(path);
		
		if(physicalFile.isDirectory()) {
			items = ps.scanDirectory(path).getItems().stream()
					.map(i -> i.getPath())
					.collect(Collectors.toList());
		} else {
			try {
				ItemAccess.deleteTagsForItem(path);
			} catch (SQLException | IOException e) {
				log.error("Error while removing the old TAGs", e);
			}
		}
		
		for(String file : items) {		
			for(String tag : tagOps.getTags()) {
				ItemTag itemTag = new ItemTag();
				Item item = new Item();
				item.setPath(file);
				itemTag.setItem(item);
				itemTag.setTagValue(tag);
				
				try {
					DBManager.store(itemTag, ItemTag.class);
				} catch (SQLException | IOException e) {
					log.error("Error while saving the new TAG: " + tag, e);
					status.setStatus(Status.ERROR);
					status.setStatus("Error while saving");
				}
			}
		}
		
		return status;
	}
	
	@GET
	@Path("item/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getTagsForItem(@PathParam("tag") String tag, @PathParam("path") String imagepath) 
	throws SQLException, IOException {
	
		String path = Phobox.getOperations().getWebPath(PathConverter.decode(imagepath));
		return ItemAccess.getTagsForItem(path)
				.stream()
				.map(t -> t.getTagValue())
				.collect(Collectors.toList());
	}

	@GET
	@Path("{tag}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StorageItem> getItemsForTag(@PathParam("tag") String tag) 
	throws SQLException, IOException {
	
		PhoboxOperations ops = Phobox.getOperations();
		PhotoService ps = new PhotoService();
		
		return ItemAccess.getItemsForTag(tag)
				.stream()
				.map(i -> ps.getItem(ops.getPhysicalFile(i.getPath())))
				.collect(Collectors.toList());
	}
	
	
}
