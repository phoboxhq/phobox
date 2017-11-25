package de.milchreis.phobox.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.ConfigManager;
import de.milchreis.phobox.db.entities.Config;
import de.milchreis.phobox.utils.ResourcesHelper;
import de.milchreis.phobox.utils.VersionComparator;

public class DBManager {
	
	private static JdbcConnectionPool pool;

	public static void init() throws ClassNotFoundException, SQLException, IOException {
		Class.forName("org.h2.Driver");
		
		File phoboxpath = Phobox.getModel().getDatabasePath();
		pool = JdbcConnectionPool.create("jdbc:h2:" + phoboxpath.getAbsolutePath()+";IGNORECASE=TRUE", "", "");
		
		// Init SQL script (creates tables if not exists)
		executeSQL(ResourcesHelper.getResourceContent("db/init.sql")); 

		// Version 
		Config versionConfig = ConfigAccess.getConfig("version");

		if(versionConfig == null) {
			versionConfig = new Config();
			versionConfig.setKey("version");
			versionConfig.setValue("0.0.1");
		}
		
		String lastversion = versionConfig.getValue();
		String version = ConfigManager.getVersion();
		
		if(!lastversion.equals(version)) {

			List<String> versions = ResourcesHelper.getResourceFiles("db/");
			Collections.sort(versions);
			Collections.reverse(versions);
			
			for(String v : versions) {

				// Skip older versions
				if(v.equals("init.sql") || VersionComparator.compare("\\.", v, lastversion) <= 0) {
					continue;
				}
				
				executeSQL(ResourcesHelper.getResourceContent("db/" + v));
			}
			
			// Update current version in database
			versionConfig.setValue(version);
			store(versionConfig, Config.class);
		}
	}
	
	public static void dispose() {
        pool.dispose();
	}
	
	public static <T> T getById(Object id, Class<T> clazz) throws SQLException, IOException {
		Dao dao = DaoManager.createDao(getJdbcConnection(), clazz);
		T obj = (T) dao.queryForId(id);
		dao.getConnectionSource().close();
		return obj;
	}
	
	public static <T> void store(Object element, Class<T> clazz) throws SQLException, IOException {
		Dao dao = DaoManager.createDao(getJdbcConnection(), clazz);
		try {
			dao.createOrUpdate(element);
		} catch(Exception e) {
			dao.refresh(element);
		}
		dao.getConnectionSource().close();
	}
	
	public static Dao<?, ?> getDAO(Class<?> clazz) throws SQLException {
		return DaoManager.createDao(getJdbcConnection(), clazz);
	}
	
	public static JdbcConnectionSource getJdbcConnection() throws SQLException {
		File phoboxpath = Phobox.getModel().getDatabasePath();
		return new JdbcConnectionSource("jdbc:h2:" + phoboxpath.getAbsolutePath()+";IGNORECASE=TRUE");
	}
	
	
	public static Connection getConnection() throws SQLException {
        return pool.getConnection();
	}
	
	public static void executeSQL(String sql, Object... arguments) throws SQLException {
		try(Connection con = getConnection()) {
			PreparedStatement statement = con.prepareStatement(sql);
			
			for(int i=1; i<=arguments.length; i++) {
				statement.setString(i, arguments[i-1].toString());
			}
			statement.execute();
		} 
	}
	
	public static void executeSQL(String sql) throws SQLException {
		try(Connection con = getConnection()) {
			Statement statement = con.createStatement();
			for(String stat : sql.split(";")) {
				statement.execute(stat+";");
			}
		} 
	}

}


