package de.milchreis.phobox;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.config.StorageConfiguration;
import de.milchreis.phobox.core.events.BasicEvent;
import de.milchreis.phobox.core.events.UpdateDatabaseEvent;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.gui.PhoboxServerGuiApplication;
import de.milchreis.phobox.gui.StorageAsk;
import de.milchreis.phobox.utils.system.Browser;
import de.milchreis.phobox.utils.phobox.StartupHelper;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootApplication
public class PhoboxServerApplication implements CommandLineRunner {
	
	@Autowired private UpdateDatabaseEvent updateDatabaseEvent;
	@Autowired private List<BasicEvent> events;

	private static PhoboxServerGuiApplication gui;

	public static void main(String[] args) {

		// CLI
		try {
			CLIManager.parse(args);

		} catch (ParseException e) {
			log.error("Could not parse the commandline arguments.");

		} catch (IOException e) {
			log.error("Could not load storage configuration file");
		}

		gui = StartupHelper.createGui();

		StartupHelper.initShutdownHook();

		PhoboxModel model = Phobox.getModel();

		try {
			// Ask for default Storage-Path on first run (no properties file found)
			if(StartupHelper.isFirstRun()) {
				if (model.isActiveGui()) {
					gui.showStorageInitalization();
				} else {
					StorageAsk.askWithCLI();
				}
			}

			if(model.isActiveGui()) {
				gui.showSplash();
			}

			StorageConfiguration config = Phobox.getModel().getStorageConfiguration();

			// Setup the spring params depending on storage path
			System.setProperty("server.port", config.getPhoboxPort()+"");
			System.setProperty("phobox.logging.path", model.getPhoboxPath().getAbsolutePath());

			SpringApplication.run(PhoboxServerApplication.class, args);

		} catch (Exception e) {
			log.error("Error while startup", e);

			if(model.isActiveGui()) {
				gui.closeSplash();
				gui.showAlert(e);
				gui.stop();
			}
		}
	}


	@Override
	public void run(String... args) {
		
		// Start all implemented background tasks
		Phobox.startSchedules();
		
		// Set up the EventRegistry
		Phobox.getEventRegistry().addEvent(updateDatabaseEvent);
		events.forEach(Phobox.getEventRegistry()::addEvent);

		Phobox.getEventRegistry().onCreation();

		if(Phobox.getModel().isActiveGui()) {

			gui.showUpload();

			StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
			Browser.open("http://localhost:" + config.getPhoboxPort());
		}
	}
}
