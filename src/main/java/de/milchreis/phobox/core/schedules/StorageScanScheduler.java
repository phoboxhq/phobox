package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.CloseableIterator;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.db.ItemAccess;
import de.milchreis.phobox.db.entities.Item;

public class StorageScanScheduler extends TimerTask implements FileAction {
	private static final Logger log = Logger.getLogger(StorageScanScheduler.class);
	
	public static final int IMMEDIATELY = 0;
	
	private boolean recursive;
	private Timer timer;
	private int timeInHours;
	private File directory;
	
	public StorageScanScheduler(int timeInHours) {
		timer = new Timer();
		this.setTimeInHours(timeInHours);
		recursive = true;
	}
	
	public StorageScanScheduler(int timeInHours, File directory, boolean recursive) {
		this(timeInHours);
		this.directory = directory;
		this.recursive = recursive;
	}

	@Override
	public void run() {
		log.debug("Start storage scanner");
		
		if(directory == null) {
			directory = new File(Phobox.getModel().getStoragePath());
		}
		
		new FileProcessor().foreachFile(
				directory, 
				PhoboxConfigs.SUPPORTED_VIEW_FORMATS, 
				this,
				recursive);
		
		// Check the database
		try {
			CloseableIterator<Item> iterator = ItemAccess.getItems();
			
			while(iterator.hasNext()) {
				Item item = iterator.next();
				
				File originalfile = Phobox.getOperations().getPhysicalFile(item.getPath());
				
				if(!originalfile.exists()) {
					Phobox.getEventRegistry().onDeleteFile(originalfile);
				}
			}

			iterator.close();
			
		} catch (IOException | SQLException e) {
			log.error("Error while scanning database", e);
		}
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

	public void start() {
		if(timeInHours == IMMEDIATELY) {
			timer.schedule(this, 1);
			
		} else {
			timer.schedule(this, getStartDate(), getTimeInHours() * 3600000);
		}
	}
	
	 public void stop() {
		 timer.cancel();
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
	
}
