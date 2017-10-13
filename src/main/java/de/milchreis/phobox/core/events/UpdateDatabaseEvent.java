package de.milchreis.phobox.core.events;

import java.io.File;
import java.sql.Date;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.DBManager;
import de.milchreis.phobox.db.entities.Item;

public class UpdateDatabaseEvent implements IEvent {
	private static final Logger log = Logger.getLogger(UpdateDatabaseEvent.class);

	private PhoboxOperations ops = Phobox.getOperations();

	@Override
	public void onNewFile(File incomingfile) {
		String subpath = ops.getWebPath(incomingfile);

		Item item = new Item();
		item.setFound(new Date(System.currentTimeMillis()));
		item.setPath(subpath);
		
		try {
			Dao<Item, String> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
			itemDAO.createIfNotExists(item);
			itemDAO.getConnectionSource().close();
			
		} catch (Exception e) {
			log.error("Error while saving new file in database", e);
		}
	}

	@Override
	public void onDeleteFile(File file) {
		String subpath = ops.getWebPath(file);

		try {
			Dao<Item, String> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
			itemDAO.deleteById(subpath);
			itemDAO.getConnectionSource().close();
			
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
			Dao<Item, String> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
			Item item = itemDAO.queryForId(subpath);
			
			if(item != null) {
				itemDAO.updateId(item, newsubpath);
			}
			
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
