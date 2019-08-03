package de.milchreis.phobox;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.StorageConfiguration;
import de.milchreis.phobox.core.events.*;
import de.milchreis.phobox.core.events.model.BasicEvent;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.gui.PhoboxServerGuiApplication;
import de.milchreis.phobox.gui.StorageAsk;
import de.milchreis.phobox.utils.system.Browser;
import de.milchreis.phobox.utils.phobox.StartupHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootApplication
public class PhoboxServerApplication implements CommandLineRunner {
	
	@Autowired private InitializeEvent initializeEvent;
	@Autowired private HashEvent hashEvent;
	@Autowired private CheckDuplicateEvent checkDuplicateEvent;
	@Autowired private MetaExtractEvent metaExtractEvent;
	@Autowired private ThumbnailEvent thumbnailEvent;

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
			Phobox.getModel().writeStorageConfig();

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
		Phobox.getEventRegistry().addEvent(initializeEvent);
		Phobox.getEventRegistry().addEvent(hashEvent);
		Phobox.getEventRegistry().addEvent(checkDuplicateEvent);
		Phobox.getEventRegistry().addEvent(metaExtractEvent);
		Phobox.getEventRegistry().addEvent(thumbnailEvent);


		Phobox.getEventRegistry().onCreation();

		if(Phobox.getModel().isActiveGui() && gui != null) {

			gui.showUpload();
			StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
			Browser.open("http://localhost:" + config.getPhoboxPort());
		}
	}
}
