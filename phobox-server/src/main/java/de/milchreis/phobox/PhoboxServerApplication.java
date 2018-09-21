package de.milchreis.phobox;

import java.util.List;

import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.events.BasicEvent;
import de.milchreis.phobox.core.events.UpdateDatabaseEvent;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.gui.ServerGui;
import de.milchreis.phobox.gui.StorageAsk;
import de.milchreis.phobox.gui.StorageAskGui;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PhoboxServerApplication implements CommandLineRunner {
	
	@Autowired private UpdateDatabaseEvent updateDatabaseEvent;
	@Autowired private List<BasicEvent> events;
	
	public static void main(String[] args) {
				
		// CLI
		try {
			CLIManager.parse(args);
		} catch (ParseException e) {
			log.error("Could not parse the commandline arguments.");
		}
		
		// Define shutdown hook for all events
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Phobox.getEventRegistry().onStop();
			}
		});
		
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
		
		SpringApplication.run(PhoboxServerApplication.class, args);
	}
	
	private static boolean isFirstRun() {
		if(Phobox.getModel().getStoragePath() == null 
				&& PreferencesManager.get(PreferencesManager.STORAGE_PATH) == null) {
			return true;
		}
		return false;
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Start all implemented background tasks
		Phobox.startSchedules();
		
		// Set up the EventRegistry
		Phobox.getEventRegistry().addEvent(updateDatabaseEvent);
		events.forEach(Phobox.getEventRegistry()::addEvent);

		Phobox.getEventRegistry().onCreation();
	}
}
