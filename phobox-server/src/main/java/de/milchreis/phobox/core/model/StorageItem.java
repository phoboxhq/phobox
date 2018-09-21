package de.milchreis.phobox.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor	
public class StorageItem {
	
	public static final String TYPE_DIRECTORY = "dir";
	public static final String TYPE_FILE = "file";
	
	private String name;
	private String time;
	private String path;
	private String type;
	private String preview;
	private String thumb;
	private String raw;
	private Boolean landscape;
	
}
