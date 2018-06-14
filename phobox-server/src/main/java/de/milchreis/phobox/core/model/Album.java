package de.milchreis.phobox.core.model;

import java.util.ArrayList;
import java.util.List;

public class Album {

	private String name;
	private List<StorageItem> items;
	
	public Album(String name) {
		setName(name);
		items = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<StorageItem> getItems() {
		return items;
	}

	public void setItems(List<StorageItem> items) {
		this.items = items;
	}

	public void addItem(StorageItem item) {
		items.add(item);
	}
}
