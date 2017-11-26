package de.milchreis.phobox.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.entities.ItemTag;

public class ItemAccess {

	public static Item getItem(String path) throws SQLException, IOException {
		Dao<Item, Integer> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		
		Item item = new Item();
		item.setPath(FilenameUtils.getFullPath(path));
		item.setName(FilenameUtils.getName(path));
		
		// Escape single quotes
		item.setPath(item.getPath().replace("'", "''"));
		item.setName(item.getName().replace("'", "''"));
		
		List<Item> items = itemDao.queryForMatching(item);
		itemDao.getConnectionSource().close();
		
		return items.size() > 0 ? items.get(0) : null;
	}

	public static CloseableIterator<Item> getItems() throws SQLException, IOException {
		Dao<Item, Integer> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
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

	public static List<Item> getItemsWhereMetaLike(String searchString) throws SQLException, IOException {
		Dao<Item, Integer> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		QueryBuilder<Item, Integer> itemQB = itemDao.queryBuilder();
		
		List<Item> items = itemQB.where()
				.like("path", "%"+searchString+"%")
				.or()
				.like("name", "%"+searchString+"%").query();

		itemDao.getConnectionSource().close();
		
		return items;
	}

	public static List<Item> getItemsWhereTagsLike(String searchString) throws SQLException, IOException {
		Dao<ItemTag, Integer> itemTagDao = DaoManager.createDao(DBManager.getJdbcConnection(), ItemTag.class);
		Dao<Item, Integer> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		
		QueryBuilder<ItemTag, Integer> itemTagQB = itemTagDao.queryBuilder();
		itemTagQB.where().like("tag_value", "%"+searchString+"%");
		
		QueryBuilder<Item, Integer> itemQB = itemDao.queryBuilder();
		List<Item> items = itemQB.join(itemTagQB).query();

		itemDao.getConnectionSource().close();
		itemTagDao.getConnectionSource().close();
		
		return items;
	}

	public static List<Item> getItemsByPath(String directory) throws SQLException, IOException {
		return getItemsByPath(directory, -1, -1);
	}
	
	public static List<Item> getItemsByPath(String directory, long start, long number) throws SQLException, IOException {
		Dao<Item, String> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		QueryBuilder<Item, String> itemQB = itemDao.queryBuilder();
		
		if(start >= 0 && number >= 0) {
			itemQB.offset(start).limit(number);
		}
		
		if(!directory.endsWith("/")) {
			directory = directory + "/";
		}

		// Escape single quotes
		directory = directory.replace("'", "''''");
		
		List<Item> items = itemQB.where().eq("path", directory).query();
		itemDao.getConnectionSource().close();
		
		return items;
	}
	
	public static void deleteItem(Item item) throws SQLException, IOException {
		Dao<Item, Integer> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		itemDAO.delete(item);
		itemDAO.getConnectionSource().close();
	}

	public static void store(Item item) throws SQLException, IOException {
		Dao<Item, Integer> itemDAO = DaoManager.createDao(DBManager.getJdbcConnection(), Item.class);
		itemDAO.createOrUpdate(item);
		itemDAO.getConnectionSource().close();
	}

}
