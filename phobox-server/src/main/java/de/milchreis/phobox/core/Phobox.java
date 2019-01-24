package de.milchreis.phobox.core;

import java.io.File;

import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.events.EventRegistry;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.ThumbProcessorQueue;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.core.storage.StorageScanQueue;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.phobox.BeanUtil;
import lombok.Getter;
import lombok.Setter;

public class Phobox {

	private PhoboxDefinitions configs;
	private PhoboxModel model;
	private PhoboxOperations operations;

	private ThumbProcessorQueue thumbProcessor;
	private FileProcessor importProcessor;
	private EventRegistry eventRegistry;
	
	@Getter @Setter
	private ImportScheduler importScheduler;
	@Getter @Setter
	private CopyScheduler copyScheduler;
	@Getter @Setter
	private StorageScanScheduler storageScanScheduler;
	@Getter @Setter
	private StorageScanQueue scanQueue;
	
	private static Phobox instance;
	
	private Phobox() {
		configs = new PhoboxDefinitions();
		model = new PhoboxModel();
		operations = new PhoboxOperations(model);
		thumbProcessor = new ThumbProcessorQueue();
		importProcessor = new FileProcessor();
		eventRegistry = new EventRegistry();
	}
	
	private static Phobox getInstance() {
		if(instance == null) {
			instance = new Phobox();
		}
		return instance;
	}
	
	public static PhoboxModel getModel() {
		return getInstance().model;
	}
	
	public static PhoboxDefinitions getConfigs() {
		return getInstance().configs;
	}
	
	public static PhoboxOperations getOperations() {
		return getInstance().operations;
	}
	
	public static void processThumbnails(File file) {
		getInstance().thumbProcessor.put(file);
	}
	
	public static void addPathToScanQueue(File path) {
		getInstance().scanQueue.putScan(path);
	}

	public static void startSchedules() {
		Phobox phobox = getInstance();
		
		// Initialize the scheduler for importing and scanning new files
		phobox.importScheduler = new ImportScheduler(3000, 100);
		phobox.copyScheduler = new CopyScheduler(3000);
		phobox.storageScanScheduler = new StorageScanScheduler(24);
		phobox.scanQueue = new StorageScanQueue(BeanUtil.getBean(ItemRepository.class));
		
		phobox.copyScheduler.start();
		phobox.storageScanScheduler.start();
		phobox.importScheduler.start();
	}
	
	public static void changeStoragePath(File path) {
		PreferencesManager.set(PreferencesManager.STORAGE_PATH, path.getAbsolutePath());
		
		// TODO: Currently not working
//		getModel().setStoragePath(path.getAbsolutePath());
//		BeanUtil.getBean(StaticResourceConfiguration.class).update();
	}

	
	public static FileProcessor getImportProcessor() {
		return getInstance().importProcessor;
	}

	public static EventRegistry getEventRegistry() {
		return getInstance().eventRegistry;
	}

}
