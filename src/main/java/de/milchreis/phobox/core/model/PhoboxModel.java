package de.milchreis.phobox.core.model;

import java.io.File;

import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;

import de.milchreis.phobox.core.config.ConfigManager;
import de.milchreis.phobox.core.config.PreferencesManager;

@ManagedObject("phoboxModel")
public class PhoboxModel {

	private int port = 8080;
	private boolean activeGui = true;
	private boolean databasebrowser = true;
	private String storagePath;
	private File phoboxPath;
	private File databasePath;
	private File incomingPath;
	private File albumPath;
	private File watchPath;
	private File backupPath;
	private File thumbPath;
	private String importFormat = "%Y/%Y-%M/%Y-%M-%D";
	
	@ManagedAttribute(value="State of the gui", name="activeGui")
	public boolean isActiveGui() {
		return activeGui;
	}
	
	public void setActiveGui(boolean activeGui) {
		this.activeGui = activeGui;
	}

	
	@ManagedAttribute(value="The main path of the saved images", name="storagePath")
	public String getStoragePath() {
		return storagePath;
	}
	
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
		phoboxPath = new File(storagePath, "phobox");
		incomingPath = new File(phoboxPath, ConfigManager.get(ConfigManager.STORAGE_INCOMING));
		
		albumPath = new File(phoboxPath, ConfigManager.get(ConfigManager.STORAGE_ALBUMS));
		albumPath.mkdirs();
		
		thumbPath = new File(phoboxPath, ConfigManager.get(ConfigManager.STORAGE_THUMBS));
		thumbPath.mkdirs();
		
		databasePath = new File(phoboxPath, "phobox");
		
		PreferencesManager.set(PreferencesManager.STORAGE_PATH, storagePath);
	}
	

	@ManagedAttribute(value="Port of the phobox server", name="port")
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
		
	}

	
	@ManagedAttribute(value="Name of the directory for importing files", name="incomingPath")
	public File getIncomingPath() {
		return incomingPath;
	}
	
	public void setIncomingPath(File incomingPath) {
		incomingPath.mkdirs();
		this.incomingPath = incomingPath;
	}
	

	@ManagedAttribute(value="Path for the backup service", name="backupPath")
	public File getBackupPath() {
		return backupPath;
	}
	
	public void setBackupPath(File backupPath) {
		this.backupPath = backupPath;
	}

	
	@ManagedAttribute(value="Name of the directory for albums", name="albumPath")
	public File getAlbumPath() {
		return albumPath;
	}
	
	public void setAlbumPath(File albumPath) {
		this.albumPath = albumPath;
	}

	
	@ManagedAttribute(value="Name of the directory for thumbnails", name="albumPath")
	public File getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(File thumbPath) {
		this.thumbPath = thumbPath;
	}
	

	@ManagedAttribute(value="Path for an optional directory for importing files", name="watchPath")
	public File getWatchPath() {
		return watchPath;
	}

	public void setWatchPath(File watchPath) {
		this.watchPath = watchPath;
		watchPath.mkdirs();
	}

	
	@ManagedAttribute(value="Defines the pattern for creating directories for new images", name="importFormat")
	public String getImportFormat() {
		return importFormat;
	}

	public void setImportFormat(String importFormat) {
		if(importFormat != null) {
			this.importFormat = importFormat;
			PreferencesManager.set(PreferencesManager.IMPORT_FORMAT, importFormat);
		}
	}

	@ManagedAttribute(value="Defines the path to the phobox meta data", name="phoboxPath")
	public File getPhoboxPath() {
		return phoboxPath;
	}

	public void setPhoboxPath(File phoboxPath) {
		this.phoboxPath = phoboxPath;
	}

	@ManagedAttribute(value="Defines the path and the name to the database", name="databasePath")
	public File getDatabasePath() {
		return databasePath;
	}

	public void setDatabasePath(File databasePath) {
		this.databasePath = databasePath;
	}

	@ManagedAttribute(value="Is the databaser browser started on startup (h2 browser on port 8082)", name="databasebrowser")
	public boolean isDatabasebrowser() {
		return databasebrowser;
	}

	public void setDatabasebrowser(boolean databasebrowser) {
		this.databasebrowser = databasebrowser;
	}
}
