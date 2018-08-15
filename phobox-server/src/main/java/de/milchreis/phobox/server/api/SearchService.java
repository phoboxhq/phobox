package de.milchreis.phobox.server.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;


@Path("/search/")
public class SearchService {
	
	@GET
	@Path("{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StorageItem> getAlbums(@PathParam("search") String searchString) throws SQLException, IOException {
		
		// Utils
		PhotoService photoService = new PhotoService();
		PhoboxOperations ops = Phobox.getOperations();	
		
		List<Item> items = ItemRepository.getItemsWhereMetaLike(searchString);
		items.addAll(ItemRepository.getItemsWhereTagsLike(searchString));

		return items.stream()
				.map(i -> photoService.getItem(ops.getPhysicalFile(i.getPath() + i.getName())))
				.collect(Collectors.toList());
	}
		
}
