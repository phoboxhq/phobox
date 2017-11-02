package de.milchreis.phobox.db;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import de.milchreis.phobox.db.entities.Config;

public class ConfigAccess {

	public static String getValue(String key) throws SQLException, IOException {
		Config conf = getConfig(key);
		return conf == null ? null : conf.getValue();
	}
	
	public static Config getConfig(String key) throws SQLException, IOException {
		Dao<Config, String> itemDao = DaoManager.createDao(DBManager.getJdbcConnection(), Config.class);
		Config item = itemDao.queryForId(key);
		itemDao.getConnectionSource().close();
		return item;
	}

}
