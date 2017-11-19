package de.milchreis.phobox.db.entities;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "item")
public class Item {

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField
	private String path;
	
	@DatabaseField
	private String name;
	
	@DatabaseField(canBeNull = false, defaultValue="0")
	private Integer rotation;

	@DatabaseField
	private Integer width;

	@DatabaseField
	private Integer height;
	
	@DatabaseField
	private String description;

	@DatabaseField
	private Date creation;
	
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

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
