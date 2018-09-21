package de.milchreis.phobox.core.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageAlbum {

	private String name;
	private List<StorageItem> items;
	
}
