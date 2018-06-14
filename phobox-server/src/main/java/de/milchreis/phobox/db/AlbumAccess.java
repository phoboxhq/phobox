package de.milchreis.phobox.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import de.milchreis.phobox.db.entities.Album;
import de.milchreis.phobox.db.entities.AlbumItem;

public class AlbumAccess {

	public static List<Album> getAlbums() throws SQLException, IOException {
		Dao<Album, Integer> albumDao = DaoManager.createDao(DBManager.getJdbcConnection(), Album.class);
		List<Album> albums = albumDao.queryForAll();
		albumDao.getConnectionSource().close();
		return albums;
	}
	
	public static Album getAlbumByName(String albumName) throws SQLException, IOException {
		Dao<Album, Integer> albumDao = DaoManager.createDao(DBManager.getJdbcConnection(), Album.class);
		List<Album> albums = albumDao.queryForEq("name", albumName);
		albumDao.getConnectionSource().close();
		return albums.size() > 0 ? albums.get(0) : null;
	}
	
	public static List<AlbumItem> getAlbumItemsByAlbumName(String albumName) throws SQLException, IOException {
		Album album = getAlbumByName(albumName);
		Dao<AlbumItem, String> albumItemDao = DaoManager.createDao(DBManager.getJdbcConnection(), AlbumItem.class);
		List<AlbumItem> albumItems = albumItemDao.queryForEq("id_album", album.getId());
		albumItemDao.getConnectionSource().close();
		return albumItems;
	}
}
