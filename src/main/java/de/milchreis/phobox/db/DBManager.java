package de.milchreis.phobox.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import de.milchreis.phobox.core.Phobox;

public class DBManager {

	public static void init() throws ClassNotFoundException, SQLException, IOException {
		Class.forName("org.h2.Driver");
		InputStream in = DBManager.class.getClassLoader().getResourceAsStream("db/init.sql");
		String initscript = IOUtils.toString(in);
		executeSQL(initscript); 
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
		return new JdbcConnectionSource("jdbc:h2:" + phoboxpath.getAbsolutePath());
	}
	
	
	public static Connection getConnection() throws SQLException {
		File phoboxpath = Phobox.getModel().getDatabasePath();
		return DriverManager.getConnection("jdbc:h2:" + phoboxpath.getAbsolutePath());
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


