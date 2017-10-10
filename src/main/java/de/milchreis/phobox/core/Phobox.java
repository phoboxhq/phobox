package de.milchreis.phobox.core;

import java.io.File;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.ThumbProcessorQueue;
import de.milchreis.phobox.server.PhoboxServer;

public class Phobox {
	private static final Logger log = Logger.getLogger(Phobox.class);

	private PhoboxConfigs configs;
	private PhoboxModel model;
	private PhoboxOperations operations;

	private PhoboxServer server;
	private ThumbProcessorQueue thumbProcessor;
	private FileProcessor importProcessor;

	private static Phobox instance;
	
	private Phobox() {
		configs = new PhoboxConfigs();
		model = new PhoboxModel();
		operations = new PhoboxOperations(model);
		thumbProcessor = new ThumbProcessorQueue();
		importProcessor = new FileProcessor();
		server = new PhoboxServer();
	}
	
	private static Phobox getInstance() {
		if(instance == null) {
			instance = new Phobox();
		}
		return instance;
	}
	
	public static PhoboxServer getSever() {
		return getInstance().server;
	}
	
	public static PhoboxModel getModel() {
		return getInstance().model;
	}
	
	public static PhoboxConfigs getConfigs() {
		return getInstance().configs;
	}
	
	public static PhoboxOperations getOperations() {
		return getInstance().operations;
	}
	

	public static void processThumbnails(File file) {
		getInstance().thumbProcessor.put(file);
	}

	public static void startServer() {
		getSever().init(getModel().getPort());
		try {
			getSever().start();

			// Add MBeans for JMX-Control
			getSever().addMBean(getModel());
		} catch (Exception e) {
			log.error("Error while starting server", e);
		}
	}

	public static FileProcessor getImportProcessor() {
		return getInstance().importProcessor;
	}
}
