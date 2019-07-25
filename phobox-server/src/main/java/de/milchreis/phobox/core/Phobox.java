package de.milchreis.phobox.core;

import java.io.File;

import de.milchreis.phobox.core.config.StorageConfiguration;
import de.milchreis.phobox.core.events.EventRegistry;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.ThumbProcessorQueue;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.core.operations.ThumbnailOperations;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.core.storage.StorageScanQueue;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.phobox.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

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
		StorageConfiguration config = getModel().getStorageConfiguration();

		// Initialize the scheduler for importing and scanning new files
		phobox.importScheduler = new ImportScheduler(3000, 100);
		phobox.copyScheduler = new CopyScheduler(3000);
		phobox.scanQueue = new StorageScanQueue(BeanUtil.getBean(ItemRepository.class));
		phobox.eventRegistry.setItemRepository(BeanUtil.getBean(ItemRepository.class));

		if(config.hasValidStorageScanTime()) {
			int[] storageScantime = config.getStorageScantime();
			phobox.storageScanScheduler = new StorageScanScheduler(storageScantime[0], storageScantime[1], 24, getModel().getStoragePathAsFile(), BeanUtil.getBean(ItemRepository.class), true);
			phobox.storageScanScheduler.start();
		}

		phobox.copyScheduler.start();
		phobox.importScheduler.start();
	}
	
	public static ThumbnailOperations getThumbnailOperations() {
		return getOperations().getThumbnailOperations();
	}
	
	public static FileProcessor getImportProcessor() {
		return getInstance().importProcessor;
	}

	public static EventRegistry getEventRegistry() {
		return getInstance().eventRegistry;
	}

}
