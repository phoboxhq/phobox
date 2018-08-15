package de.milchreis.phobox.db.repositories;

import org.springframework.data.repository.CrudRepository;

import de.milchreis.phobox.db.entities.Album;

public interface AlbumRepository extends CrudRepository<Album, Integer>{

	Album findByName(String albumName);
	
}
