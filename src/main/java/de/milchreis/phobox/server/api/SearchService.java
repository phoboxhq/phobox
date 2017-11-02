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

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.ItemAccess;
import de.milchreis.phobox.db.entities.Item;


@Path("/search/")
public class SearchService {
	private static Logger log = Logger.getLogger(SearchService.class);
	
	@GET
	@Path("{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StorageItem> getAlbums(@PathParam("search") String searchString) throws SQLException, IOException {
		
		// Utils
		PhotoService photoService = new PhotoService();
		PhoboxOperations ops = Phobox.getOperations();

		// Search String Analyser
		//  -> Datum?
		//  -> Befehl? (z.B. :name ....
		
		// Item:		Path,  Creation Description, 
		// Item_Tag:	Tags
		
		
		List<Item> items = ItemAccess.getItemsWhereMetaLike(searchString);
		items.addAll(ItemAccess.getItemsWhereTagsLike(searchString));

		return items.stream()
				.map(i -> photoService.getItem(ops.getPhysicalFile(i.getPath())))
				.collect(Collectors.toList());
	}
		
}
