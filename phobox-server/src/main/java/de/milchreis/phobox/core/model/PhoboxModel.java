package de.milchreis.phobox.core.model;

import java.io.File;

import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.config.PreferencesManager;
import lombok.Data;

@Data
public class PhoboxModel {

	private int port = 8080;
	private boolean activeGui = true;
	private boolean databasebrowser = true;
	private String storagePath;
	private File phoboxPath;
	private File databasePath;
	private File incomingPath;
	private File approvalPath;
	private File watchPath;
	private File backupPath;
	private File thumbPath;
	private String importFormat = "%Y/%Y-%M/%Y-%M-%D";
	private int imgPageSize = 30;
	private boolean autoSave = true;

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
		phoboxPath = new File(storagePath, "phobox");
		incomingPath = new File(phoboxPath, PhoboxDefinitions.STORAGE_INCOMING);
		
		thumbPath = new File(phoboxPath, PhoboxDefinitions.STORAGE_THUMBS);

		if(autoSave)
			thumbPath.mkdirs();

		approvalPath = new File(phoboxPath, PhoboxDefinitions.STORAGE_APPROVAL);

		if(autoSave)
			approvalPath.mkdirs();

		databasePath = new File(phoboxPath, "phobox");

		if(autoSave)
			PreferencesManager.set(PreferencesManager.STORAGE_PATH, storagePath);
	}

	public File getStoragePathAsFile() {
		if(storagePath != null)
			return new File(storagePath);

		return null;
	}
	
	public void setIncomingPath(File incomingPath) {
		incomingPath.mkdirs();
		this.incomingPath = incomingPath;
	}
	
	public void setWatchPath(File watchPath) {
		this.watchPath = watchPath;
		watchPath.mkdirs();
	}
	
	public void setImportFormat(String importFormat) {
		if(importFormat != null) {
			this.importFormat = importFormat;
			PreferencesManager.set(PreferencesManager.IMPORT_FORMAT, importFormat);
		}
	}

}
