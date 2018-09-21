package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.entities.ItemTag;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.db.repositories.ItemTagRepository;
import de.milchreis.phobox.server.api.requestmodel.TagOperation;
import de.milchreis.phobox.utils.PathConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tags")
public class TagController {
	
	@Autowired private ItemRepository itemRepository;
	@Autowired private ItemTagRepository tagRepository;
	
	@RequestMapping(value = "", 
			method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status saveTags(@RequestBody TagOperation tagOps) {
		
		Status status = new Status(Status.OK);
		String path = Phobox.getOperations().getWebPath(tagOps.getItem());
		path = PathConverter.decode(path);
		File physicalFile = Phobox.getOperations().getPhysicalFile(path);
		
		try {
			List<Item> items = new ArrayList<>();
			
			if(physicalFile.isDirectory()) {
				path = path.endsWith("/") ? path : path + "/" ;
				items.addAll(itemRepository.findByPath(path));
			} else {
				Item item = itemRepository.findByFullPath(path);
				if(item != null) {
					items.add(item);
				}
			}
			
			// Add tags
			for(Item item : items) {
				
				item.getTags().addAll(tagOps.getTags().stream()
						.map(t -> new ItemTag(t))
						.collect(Collectors.toList()));
				
				itemRepository.save(item);
			}

		} catch(Exception e) {
			status.setStatus(Status.ERROR);
			log.error("Error while saving tags", e);
		}
		
		return status;
	}
	
	@RequestMapping(value = "item/{path}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> getTagsForItem(@PathVariable("path") String imagepath) 
	throws SQLException, IOException {
	
		String path = Phobox.getOperations().getWebPath(PathConverter.decode(imagepath));
		return tagRepository.findByItemPath(path)
				.stream()
				.map(t -> t.getName())
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<StorageItem> getItemsForTag(@PathVariable("tag") String tag) 
	throws SQLException, IOException {
	
		PhoboxOperations ops = Phobox.getOperations();
		PhotoController ps = new PhotoController();
		
		return itemRepository.findByTag(tag)
				.stream()
				.map(i -> ps.getItem(ops.getPhysicalFile(i.getPath())))
				.collect(Collectors.toList());
	}
	
	
}
