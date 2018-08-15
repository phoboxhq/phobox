package de.milchreis.phobox.core.events;

import java.io.File;
import java.sql.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.DBManager;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateDatabaseEvent implements IEvent {

	private PhoboxOperations ops = Phobox.getOperations();

	@Override
	public void onNewFile(File incomingfile) {
		String subpath = ops.getWebPath(incomingfile);
		
		try {
			Item item = ItemRepository.getItem(subpath);
			
			if(item == null) {
				item = new Item();
				item.setFound(new Date(System.currentTimeMillis()));
				item.setName(incomingfile.getName());
				item.setPath(FilenameUtils.getFullPath(subpath));
				ItemRepository.store(item);
			}

		} catch (Exception e) {
			log.error("Error while saving new file in database", e);
		}
	}

	@Override
	public void onDeleteFile(File file) {
		String subpath = ops.getWebPath(file);

		try {
			Item item = ItemRepository.getItem(subpath);
			ItemRepository.deleteItem(item);
			
		} catch (Exception e) {
			log.error("Error while deleting file in database", e);
		}
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
		try {
			String subpath = ops.getWebPath(directory);
			
			String sql = "DELETE FROM item WHERE path LIKE '"+subpath+"%'";			
			DBManager.executeSQL(sql);
			
		} catch (Exception e) {
			log.error("Error while updateing file path in database", e);
		}
	}

	@Override
	public void onRenameFile(File original, File newFile) {
		String subpath = ops.getWebPath(original);
		String newsubpath = ops.getWebPath(newFile);

		try {
			Dao<Item, Integer> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
			Item item = new Item();
			item.setPath(FilenameUtils.getFullPath(subpath));
			item.setName(original.getName());
			
			List<Item> items = itemDAO.queryForMatching(item);
			item = items.get(0);
			
			item.setPath(FilenameUtils.getFullPath(newsubpath));
			item.setName(newFile.getName());
			
			itemDAO.update(item);
			
			itemDAO.getConnectionSource().close();
			
		} catch (Exception e) {
			log.error("Error while updateing file path in database", e);
		}
	}
	
	@Override
	public void onRenameDirectory(File directory, File newDirectory) {
		
		try {
			String subpath = ops.getWebPath(directory);
			String newsubpath = ops.getWebPath(newDirectory);
			
			String sql = "UPDATE item SET path = REPLACE(path, ?, ?) WHERE path LIKE '"+subpath+"%'";			
			DBManager.executeSQL(sql, subpath, newsubpath);
			
		} catch (Exception e) {
			log.error("Error while updateing file path in database", e);
		}
	}

	@Override
	public void onCreation() {
	}
	
	@Override
	public void onStop() {
	}

}
