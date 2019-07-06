package de.milchreis.phobox;

import java.io.File;
import java.io.IOException;

import de.milchreis.phobox.core.config.PreferencesManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.PhoboxModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CLIManager {
	
	public static void parse(String[] args) throws ParseException, IOException {

		PhoboxModel model = Phobox.getModel();
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addOption("nw", "noWindow", false, "Hides the application window for headless usage");
		options.addOption("s", "storage", true, "Defines the main storage path");
		options.addOption("w", "watchDirectory", true, "Scans this directory for new files");
		options.addOption("p", "port", true, "Sets the port for this application (default 8080)");
		options.addOption("b", "backupDirectory", true, "Sets the directory for backups");
		options.addOption("db", "dbbrowser", false, "Activates the integrated database browser (port 8082)");
		options.addOption("h", "help", false, "Prints this help");
		
		// parse the command line arguments
		CommandLine line = parser.parse( options, args );
		
		if(line.hasOption("noWindow")) {
			model.setActiveGui(false);
			log.debug("noWindow option is active");
		}
		
		if(line.hasOption("dbbrowser")) {
			model.setDatabasebrowser(true);
			log.debug("database browser option is active");
		}
		
		if(line.hasOption("storage")) {
			model.setStoragePath(line.getOptionValue("storage"));
		} else {
			PreferencesManager.init();
			File storage = PreferencesManager.getStoragePath();
			if(storage != null)
				model.setStoragePath(storage.getAbsolutePath());
		}
		
		if(line.hasOption("watchDirectory")) {
			model.setWatchPath(new File(line.getOptionValue("watchDirectory")));
		}
		
		if(line.hasOption("backupDirectory")) {
			model.setBackupPath(new File(line.getOptionValue("backupDirectory")));
		}
		
		if(line.hasOption("port")) {
			model.getStorageConfiguration().setPhoboxPort(Integer.parseInt(line.getOptionValue("port")));
		}
		
		if(line.hasOption("help")) {
			new HelpFormatter().printHelp("phobox", options);
		}
	}
}
