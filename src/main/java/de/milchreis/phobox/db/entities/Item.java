package de.milchreis.phobox.db.entities;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "item")
public class Item {

	@DatabaseField(id = true, canBeNull = false)
	private String path;
	
	@DatabaseField(canBeNull = false, defaultValue="0")
	private Integer rotation;
	
	@DatabaseField(canBeNull = false)
	private Date found;
	
	public Item() {
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getRotation() {
		return rotation;
	}
	
	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
	
	public Date getFound() {
		return found;
	}
	
	public void setFound(Date found) {
		this.found = found;
	}
}
