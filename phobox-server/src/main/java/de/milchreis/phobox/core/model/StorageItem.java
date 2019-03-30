package de.milchreis.phobox.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

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

	public void setTypeByFile(File physicalFile) {
		type = physicalFile.isDirectory() ? StorageItem.TYPE_DIRECTORY : StorageItem.TYPE_FILE;
	}
}
