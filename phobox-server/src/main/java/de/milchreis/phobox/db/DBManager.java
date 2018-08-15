package de.milchreis.phobox.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;

@Component
public class DBManager {
	
	@Bean
	public DataSource secondDatasource(@Qualifier("firstDatasource") DataSource ds){
	    return DataSourceBuilder
	        .create()
	        .username("sa")
	        .password("")
	        .url(Phobox.getModel().getStoragePath()+";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;IGNORECASE=TRUE")
	        .driverClassName("org.h2.Driver")
	        .build();
	}
	
	
//	private static JdbcConnectionPool pool;
//
//	public static void init() throws ClassNotFoundException, SQLException, IOException {
//		Class.forName("org.h2.Driver");
//		
//		File phoboxpath = Phobox.getModel().getDatabasePath();
//		pool = JdbcConnectionPool.create("jdbc:h2:" + phoboxpath.getAbsolutePath()+";IGNORECASE=TRUE", "", "");
//		
//		Flyway flyway = new Flyway();
//		flyway.setDataSource("jdbc:h2:" + phoboxpath.getAbsolutePath()+";IGNORECASE=TRUE", "", "");
//		
//		flyway.baseline();
//		flyway.migrate();
//	}
//	
//	public static void dispose() {
//        pool.dispose();
//	}
//	
//	public static <T> T getById(Object id, Class<T> clazz) throws SQLException, IOException {
//		Dao dao = DaoManager.createDao(getJdbcConnection(), clazz);
//		T obj = (T) dao.queryForId(id);
//		dao.getConnectionSource().close();
//		return obj;
//	}
//	
//	public static <T> void store(Object element, Class<T> clazz) throws SQLException, IOException {
//		Dao dao = DaoManager.createDao(getJdbcConnection(), clazz);
//		try {
//			dao.createOrUpdate(element);
//		} catch(Exception e) {
//			dao.refresh(element);
//		}
//		dao.getConnectionSource().close();
//	}
//	
//	public static Dao<?, ?> getDAO(Class<?> clazz) throws SQLException {
//		return DaoManager.createDao(getJdbcConnection(), clazz);
//	}
//	
//	public static JdbcConnectionSource getJdbcConnection() throws SQLException {
//		File phoboxpath = Phobox.getModel().getDatabasePath();
//		return new JdbcConnectionSource("jdbc:h2:" + phoboxpath.getAbsolutePath()+";IGNORECASE=TRUE");
//	}
//	
//	
//	public static Connection getConnection() throws SQLException {
//        return pool.getConnection();
//	}
//	
//	public static void executeSQL(String sql, Object... arguments) throws SQLException {
//		try(Connection con = getConnection()) {
//			PreparedStatement statement = con.prepareStatement(sql);
//			
//			for(int i=1; i<=arguments.length; i++) {
//				statement.setString(i, arguments[i-1].toString());
//			}
//			statement.execute();
//		} 
//	}
//	
//	public static void executeSQL(String sql) throws SQLException {
//		try(Connection con = getConnection()) {
//			Statement statement = con.createStatement();
//			for(String stat : sql.split(";")) {
//				statement.execute(stat+";");
//			}
//		} 
//	}

}


