package de.milchreis.phobox.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "album_item")
public class AlbumItem {
	
	@DatabaseField(columnName = "id", generatedId = true, allowGeneratedIdInsert = true)
	private Integer id;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, foreignColumnName = "id", columnName="id_album")
	private Album album;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, foreignColumnName = "id", columnName="id_item")
	private Item item;
	
	@DatabaseField
	private Integer order;

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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
