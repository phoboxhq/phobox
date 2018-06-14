package de.milchreis.phobox.core.model;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class StorageStatus extends StorageItem {

	private Map<String, StorageItem> items;
	private boolean isFragment;
	private boolean isProcessing;
	
	public StorageStatus() {
		setItems(new TreeMap<>());
	}
	
	public void add(StorageItem item) {
		if(item.getType().equals(StorageItem.TYPE_DIRECTORY)) {
			items.put(item.getPath().toString(), item);
		} else {
			items.put(item.getPath().substring(0, item.getPath().length()-4), item);
		}
	}

	public StorageItem getItem(File path) {
		return items.get(path.getAbsolutePath().substring(0,path.getAbsolutePath().length()-4));
	}
	
	public Collection<StorageItem> getItems() {
		return items.values();
	}

	public void setItems(Map<String, StorageItem> items) {
		this.items = items;
	}

	public boolean isFragment() {
		return isFragment;
	}

	public void setFragment(boolean isFragment) {
		this.isFragment = isFragment;
	}

	public boolean isProcessing() {
		return isProcessing;
	}

	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}
}
