package de.milchreis.phobox.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.entities.ItemTag;

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

	public static void deleteTagsForItem(String imagepath) throws SQLException, IOException {
		Dao<ItemTag, Integer> itemTagDao = DaoManager.createDao(DBManager.getJdbcConnection(), ItemTag.class);
		
		DeleteBuilder<ItemTag, Integer> itemTagQB = itemTagDao.deleteBuilder();
		itemTagQB.where().eq("id_item", imagepath);
		itemTagQB.delete();
		
		itemTagDao.getConnectionSource().close();
	}
	
	public static List<ItemTag> getTagsForItem(String imagepath) throws SQLException, IOException {
		Dao<ItemTag, Integer> itemTagDao = DaoManager.createDao(DBManager.getJdbcConnection(), ItemTag.class);
		List<ItemTag> tags = itemTagDao.queryForEq("id_item", imagepath);
		itemTagDao.getConnectionSource().close();
		return tags;
	}

	public static List<Item> getItemsForTag(String tag) throws IOException, SQLException {
		Dao<ItemTag, Integer> itemTagDao = DaoManager.createDao(DBManager.getJdbcConnection(), ItemTag.class);
		Dao<Item, String> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);

		QueryBuilder<ItemTag, Integer> itemTagQB = itemTagDao.queryBuilder();
		itemTagQB.where().eq("tag_value", tag);

		QueryBuilder<Item, String> itemQB = itemDao.queryBuilder();
		List<Item> results = itemQB.join(itemTagQB).query();
		
		itemDao.getConnectionSource().close();
		itemTagDao.getConnectionSource().close();
		
		return results;
	}

}
