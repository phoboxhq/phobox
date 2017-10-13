package de.milchreis.phobox.db.entities;

import java.sql.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "album")
public class Album {
	
	@DatabaseField(id = true)
	private int id;

	@DatabaseField(canBeNull = false)
	private String name;
	
	@DatabaseField
	private Date creation;
	
	public Album() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}
}
