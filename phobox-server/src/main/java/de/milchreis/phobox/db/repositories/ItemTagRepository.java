package de.milchreis.phobox.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.milchreis.phobox.db.entities.ItemTag;

public interface ItemTagRepository extends CrudRepository<ItemTag, String>{
	
	@Query("SELECT t FROM ItemTag t JOIN t.items i WHERE i.fullPath = :path")
	List<ItemTag> findByItemPath(@Param("path") String path);

}
