package de.milchreis.phobox.core.model;

import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.config.StorageConfiguration;
import lombok.Data;

import java.io.File;
import java.io.IOException;

@Data
public class PhoboxModel {

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
	private int imgPageSize = 30;
	private boolean autoSave = true;
	private StorageConfiguration storageConfiguration = new StorageConfiguration();

	public void setStoragePath(String storagePath) throws IOException {
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

		storageConfiguration = StorageConfiguration.load(new File(phoboxPath, "phobox.yaml"));

		if(autoSave)
			PreferencesManager.setStoragePath(new File(storagePath));
	}

	public void writeStorageConfig() throws IOException {
		storageConfiguration.write(new File(phoboxPath, "phobox.yaml"));
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
	
}
