package de.milchreis.phobox;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.PhoboxModel;

public class CLIManager {
	private static final Logger log = Logger.getLogger(CLIManager.class);
	
	public static void parse(String[] args) throws ParseException {

		PhoboxModel model = Phobox.getModel();
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addOption("nw", "noWindow", false, "Hides the application window for headless usage");
		options.addOption("s", "storage", true, "Defines the main storage path");
		options.addOption("w", "watchDirectory", true, "Scans this directory for new files");
		options.addOption("p", "port", true, "Sets the port for this application (default 8080)");
		options.addOption("b", "backupDirectory", true, "Sets the directory for backups");
		options.addOption("h", "help", false, "Prints this help");
		
		// parse the command line arguments
		CommandLine line = parser.parse( options, args );
		
		if(line.hasOption("noWindow")) {
			model.setActiveGui(false);
			log.debug("noWindow option is active");
		}
		
		if(line.hasOption("storage")) {
			model.setStoragePath(line.getOptionValue("storage"));
		}
		
		if(line.hasOption("watchDirectory")) {
			model.setWatchPath(new File(line.getOptionValue("watchDirectory")));
		}
		
		if(line.hasOption("backupDirectory")) {
			model.setBackupPath(new File(line.getOptionValue("backupDirectory")));
		}
		
		if(line.hasOption("port")) {
			model.setPort(Integer.parseInt(line.getOptionValue("port")));
		}
		
		if(line.hasOption("help")) {
			new HelpFormatter().printHelp("phobox", options);
		}
	}
}
