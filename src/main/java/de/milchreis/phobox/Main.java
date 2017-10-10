package de.milchreis.phobox;

import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.ThumbCleanerScheduler;
import de.milchreis.phobox.gui.ServerGui;
import de.milchreis.phobox.gui.StorageAsk;
import de.milchreis.phobox.gui.StorageAskGui;
import javafx.application.Application;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		
		// CLI
		try {
			CLIManager.parse(args);
		} catch (ParseException e) {
			log.error("Could not parse the commandline arguments.");
		}
		
		PhoboxModel model = Phobox.getModel();

		// Ask for default Storage-Path on first run (no properties file found)
		if(isFirstRun()) {
			if(model.isActiveGui()) {
				Application.launch(StorageAskGui.class);
			} else {
				StorageAsk.askWithCLI();
			}
		}

		// Update the storage path
		model.setStoragePath(PreferencesManager.get(PreferencesManager.STORAGE_PATH));
		model.setImportFormat(PreferencesManager.get(PreferencesManager.IMPORT_FORMAT));
		
		// Initialize an application instance for JavaFX (for scaling and window)
		// JavaFX is used for scaling and rotating images, because is much faster
		// than other implementations (also on embedded devices like the raspberry pi).
		new Thread(() -> {
			Application.launch(ServerGui.class);
		}).start();

		Phobox.startServer();
		
		// Initialize the scheduler for importing and scanning new files
		new ImportScheduler(3000);
		new CopyScheduler(3000);
		new ThumbCleanerScheduler(24);
	}
	
	private static boolean isFirstRun() {
		if(Phobox.getModel().getStoragePath() == null 
				&& PreferencesManager.get(PreferencesManager.STORAGE_PATH) == null) {
			return true;
		}
		return false;
	}
}
