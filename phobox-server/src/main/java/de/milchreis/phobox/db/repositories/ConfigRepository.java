package de.milchreis.phobox.db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.milchreis.phobox.db.entities.Config;

public interface ConfigRepository extends CrudRepository<Config, String>{

	default Optional<Config> findByKey(String key) {
		return findById(key);
	}
	
	@Query("SELECT c.value FROM Config c WHERE c.key = :key")
	String findValue(String key);

}
