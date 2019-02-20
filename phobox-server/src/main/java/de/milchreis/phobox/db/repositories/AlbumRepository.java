package de.milchreis.phobox.db.repositories;

import de.milchreis.phobox.db.entities.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Integer>{

	Album findByName(String albumName);

}
