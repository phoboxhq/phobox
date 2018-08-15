package de.milchreis.phobox.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.milchreis.phobox.db.entities.AlbumItem;

public interface AlbumItemRepository extends CrudRepository<AlbumItem, Integer>{

	List<AlbumItem> findByAlbumName(String albumName);
	
}
