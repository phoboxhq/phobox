package de.milchreis.phobox.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StorageStatus extends StorageItem {

	@Getter
	@Setter
	private List<StorageItem> items = new ArrayList<>();

	@Getter
	@Setter
	private boolean isFragment;

	@Getter
	@Setter
	private boolean isProcessing;

	public StorageStatus(String name, String path, String type) {
		this.name = name;
		this.path = path;
		this.type = type;
	}

	public void addAll(List<StorageItem> directories) {
		directories.forEach(this::add);
	}

	public void add(StorageItem item) {
		if(item == null) {
			return;
		}
		items.add(item);
	}

}
