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
	
	protected String name;
	protected String time;
	protected String path;
	protected String type;
	protected String preview;
	protected String thumb;
	protected String raw;
	protected boolean generatingThumb = false;
	protected Boolean landscape;
	
}
