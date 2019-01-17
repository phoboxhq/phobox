package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.util.*;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageScanScheduler extends TimerTask implements FileAction, Schedulable {
	
	public static final int IMMEDIATELY = 0;
	
	private boolean recursive;
	private Timer timer;
	private int timeInHours;
	private File directory;
	private boolean ready;
	private ItemRepository itemRepository;
	private File currentFile;
	private FileProcessor fileProcessor;
	
	
	public StorageScanScheduler(int timeInHours) {
		timer = new Timer();
		this.setTimeInHours(timeInHours);
		recursive = true;
		fileProcessor = new FileProcessor();
	}
	
	public StorageScanScheduler(int timeInHours, File directory, ItemRepository itemRepository, boolean recursive) {
		this(timeInHours);
		this.directory = directory;
		this.recursive = recursive;
		this.itemRepository = itemRepository;
	}

	
	@Override
	public void run() {
		log.debug("Start storage scanner");
		
		if(directory == null) {
			directory = new File(Phobox.getModel().getStoragePath());
		}

		fileProcessor.foreachFile(
				directory, 
				PhoboxDefinitions.SUPPORTED_VIEW_FORMATS,
				Arrays.asList(this),
				recursive,
				0);
		
		// Check the database
		itemRepository.findAll().forEach(item -> {
			
			File originalfile = Phobox.getOperations().getPhysicalFile(item.getPath());
			currentFile = originalfile;
			
			if(!originalfile.exists()) {
				Phobox.getEventRegistry().onDeleteFile(originalfile);
			}
		});
		
		ready = true;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		// Skip the phobox directory
		if(Phobox.getOperations().isInPhoboxDirectory(file)) {
			return;
		}
		
		// Crawling over the storage and find missing database entries and thumbnails
		// The magic is happened by the registered events
		Phobox.getEventRegistry().onNewFile(file);
	}

	@Override
	public void start() {
		if (timeInHours == IMMEDIATELY) {
			timer.schedule(this, 1);

		} else {
			timer.schedule(this, getStartDate(), getTimeInHours() * 3600000);
		}
	}

	@Override
	public void stop() {
		timer.cancel();
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	public int getTimeInHours() {
		return timeInHours;
	}

	public void setTimeInHours(int timeInHours) {
		this.timeInHours = timeInHours;
	}
	
	private Date getStartDate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}

	public File getCurrentFile() {
		String storageFile = fileProcessor.getCurrentfile();
		File dbFile = currentFile;
		return storageFile == null || storageFile.isEmpty() ? dbFile : new File(storageFile);
	}
	
}
