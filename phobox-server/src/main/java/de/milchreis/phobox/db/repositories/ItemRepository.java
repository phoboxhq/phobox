package de.milchreis.phobox.db.repositories;

import java.util.List;
import java.util.UUID;

import javax.persistence.OrderBy;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.milchreis.phobox.db.entities.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> {

	Page<Item> findByPath(String path, Pageable pageable);
	
	@OrderBy("creation ASC, file_name ASC")
	List<Item> findByPath(String path);

	Item findByFullPath(String subpath);

	@Query("SELECT i FROM Item i JOIN ItemTag t WHERE t.name = :tag ORDER BY creation ASC, file_name ASC")
	List<Item> findByTag(@Param("tag") String tag);

	@Query("SELECT i FROM Item i WHERE i.path LIKE CONCAT('%', :searchString, '%') OR i.fileName LIKE CONCAT('%', :searchString, '%') ORDER BY creation ASC, file_name ASC")
	List<Item> findBySearchStringInNameAndPath(@Param("searchString") String searchString);

	@Query("SELECT i FROM ItemTag t JOIN t.items i WHERE t.name LIKE CONCAT('%', :searchString, '%') ORDER BY creation ASC, file_name ASC" )
	List<Item> findBySearchStringInTags(@Param("searchString") String searchString);

	@Transactional
	@Modifying
	@Query("DELETE FROM Item WHERE path LIKE CONCAT(:subpath, '%')")
	void deleteBySubpath(@Param("subpath") String subpath);

	@Transactional
	@Modifying
	@Query("UPDATE Item SET " +
			"path = REPLACE(path, :subpath, :newsubpath), " +
			"full_path = REPLACE(full_path, :subpath, :newsubpath) " +
			"WHERE full_path LIKE CONCAT(:subpath, '%')")
	void replaceSubpath(@Param("subpath") String subpath, @Param("newsubpath") String newsubpath);

}
