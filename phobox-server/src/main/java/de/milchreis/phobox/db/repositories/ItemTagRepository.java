package de.milchreis.phobox.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.entities.ItemTag;

public interface ItemTagRepository extends CrudRepository<ItemTag, String>{

	@Query("DELETE FROM ItemTag t WHERE t.idItem = :item.id")
	void deleteTagsForItem(Item item);
	
	@Query("SELECT t FROM ItemTag t JOIN Item i ON i.id = t.idItem WHERE i.path = :path")
	List<ItemTag> findByItemPath(String path);

}
