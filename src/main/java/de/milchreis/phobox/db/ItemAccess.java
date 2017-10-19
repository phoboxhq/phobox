package de.milchreis.phobox.db;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import de.milchreis.phobox.db.entities.Item;

public class ItemAccess {

	public static Item getItemByPath(String path) throws SQLException, IOException {
		Dao<Item, String> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		Item item = itemDao.queryForId(path);
		itemDao.getConnectionSource().close();
		return item;
	}

	public static CloseableIterator<Item> getItems() throws SQLException, IOException {
		Dao<Item, String> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		return itemDao.iterator();
	}

}
