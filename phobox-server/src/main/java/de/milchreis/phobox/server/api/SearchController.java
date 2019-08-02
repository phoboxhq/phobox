package de.milchreis.phobox.server.api;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.server.services.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	
	@Autowired private ItemRepository itemRepository;
	@Autowired private IPhotoService photoService;
	
	@RequestMapping(value = "{search}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<StorageItem> getAlbums(@PathVariable("search") String searchString, Pageable pageable) {
		
		// Utils
		PhoboxOperations ops = Phobox.getOperations();	

		return itemRepository.findBySearchString(searchString, pageable)
				.stream()
				.map(i -> photoService.getItem(ops.getPhysicalFile(i.getFullPath())))
				.collect(Collectors.toList());
	}
		
}
