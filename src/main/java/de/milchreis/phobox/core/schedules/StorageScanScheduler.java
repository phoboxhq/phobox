package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.converter.StorageFileConverter;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;

public class StorageScanScheduler extends TimerTask {
	private static final Logger log = Logger.getLogger(StorageScanScheduler.class);

	public StorageScanScheduler(int timeInHours) {
		new Timer().schedule(this, getStartDate(), timeInHours * 3600000);
	}

	@Override
	public void run() {

		log.debug("Start thumb cleaner");
		
		File thumbDir = Phobox.getModel().getThumbPath();
		
		new FileProcessor().foreachFile(
				thumbDir, 
				PhoboxConfigs.SUPPORTED_VIEW_FORMATS,
				new FileAction() {
					@Override
					public void process(File file, LoopInfo info) {
						File originalFile = StorageFileConverter.getOriginalFileByThumb(file);
						
						// If the original file does not exists, the thumb file is
						// not necessary any more.
						if(!originalFile.exists()) {
							log.debug("Remove thumb, because original doenst exists: " + file);
							file.delete();
						}
					}
				},
				true);
		return;
	}
	
	private Date getStartDate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}
}
