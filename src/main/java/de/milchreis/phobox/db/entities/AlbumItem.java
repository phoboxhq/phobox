package de.milchreis.phobox.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "album_item")
public class AlbumItem {
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true, foreignColumnName = "id", columnName="id_album")
	private Album album;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true, foreignColumnName = "path", columnName="path")
	private Item item;

	public AlbumItem() {
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
