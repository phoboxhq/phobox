package de.milchreis.phobox.db.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.milchreis.phobox.db.entities.Item;

public interface ItemRepository extends CrudRepository<Item, Integer>{

	
	List<Item> findByPathAndName(String path, String name);

	List<Item> findByPath(String path);
	
	List<Item> findByPath(String path, Pageable pageRequest);
	
	@Query("SELECT i FROM Item i JOIN ItemTag t ON t.idItem = i.id WHERE t.tagValue :tag")
	List<Item> findByTag(String tag);
	
	@Query("SELECT i FROM Item i WHERE i.path LIKE '%:searchString%' OR i.name LIKE '%:searchString%'")
	List<Item> findBySearchStringInNameAndPath(String searchString);

	@Query("SELECT t FROM ItemTag t, Item i WHERE t.tagValue LIKE '%:searchString%' AND i.id = t.idItem")
	List<Item> findBySearchStringInTags(String searchString);

}
