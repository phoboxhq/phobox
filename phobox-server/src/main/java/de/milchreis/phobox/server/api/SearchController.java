package de.milchreis.phobox.server.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	
	@Autowired private ItemRepository itemRepository;
	@Autowired private PhotoController photoController;
	
	@RequestMapping(value = "{search}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<StorageItem> getAlbums(@PathVariable("search") String searchString) throws SQLException, IOException {
		
		// Utils
		PhoboxOperations ops = Phobox.getOperations();	
		
		List<Item> items = itemRepository.findBySearchStringInNameAndPath(searchString);
		items.addAll(itemRepository.findBySearchStringInTags(searchString));

		return items.stream()
				.map(i -> photoController.getItem(ops.getPhysicalFile(i.getFullPath())))
				.collect(Collectors.toList());
	}
		
}
