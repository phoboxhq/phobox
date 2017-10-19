package de.milchreis.phobox.core;

import java.io.File;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.events.EventRegistry;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.ThumbProcessorQueue;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.server.PhoboxServer;

public class Phobox {
	private static final Logger log = Logger.getLogger(Phobox.class);

	private PhoboxConfigs configs;
	private PhoboxModel model;
	private PhoboxOperations operations;

	private PhoboxServer server;
	private ThumbProcessorQueue thumbProcessor;
	private FileProcessor importProcessor;
	private EventRegistry eventRegistry;
	
	private ImportScheduler 	importScheduler;
	private CopyScheduler copyScheduler;
	private StorageScanScheduler storageScanScheduler;
	
	private static Phobox instance;
	
	private Phobox() {
		configs = new PhoboxConfigs();
		model = new PhoboxModel();
		operations = new PhoboxOperations(model);
		thumbProcessor = new ThumbProcessorQueue();
		importProcessor = new FileProcessor();
		eventRegistry = new EventRegistry();
		server = new PhoboxServer();
		
		// Initialize the scheduler for importing and scanning new files
		importScheduler = new ImportScheduler(3000);
		copyScheduler = new CopyScheduler(3000);
		storageScanScheduler = new StorageScanScheduler(24);
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

	public static void startSchedules() {
		Phobox phobox = getInstance();
		phobox.copyScheduler.start();
		phobox.storageScanScheduler.start();
		phobox.importScheduler.start();
	}

	
	public static FileProcessor getImportProcessor() {
		return getInstance().importProcessor;
	}

	public static EventRegistry getEventRegistry() {
		return getInstance().eventRegistry;
	}

	public ImportScheduler getImportScheduler() {
		return importScheduler;
	}

	public void setImportScheduler(ImportScheduler importScheduler) {
		this.importScheduler = importScheduler;
	}

	public CopyScheduler getCopyScheduler() {
		return copyScheduler;
	}

	public void setCopyScheduler(CopyScheduler copyScheduler) {
		this.copyScheduler = copyScheduler;
	}

	public StorageScanScheduler getStorageScanScheduler() {
		return storageScanScheduler;
	}

	public void setStorageScanScheduler(StorageScanScheduler storageScanScheduler) {
		this.storageScanScheduler = storageScanScheduler;
	}

}
