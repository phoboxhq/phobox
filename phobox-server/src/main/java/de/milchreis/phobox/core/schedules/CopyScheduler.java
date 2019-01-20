package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CopyScheduler extends TimerTask implements FileAction, Schedulable {

	private int lastSize = 0;
	private int intervalInMillis;
	private Timer timer;
	private boolean ready;
	
	public CopyScheduler(int intervalInMillis) {
		timer = new Timer();
		this.intervalInMillis = intervalInMillis;
	}

	@Override
	public void run() {
		ready = false;

		PhoboxModel model = Phobox.getModel();
		File watchDir = model.getWatchPath();
		
		// Skip here if no watchDir is defined
		if(watchDir == null) return;
		
		// Check directory
		int numOfFiles = watchDir.list().length;
		if(numOfFiles != lastSize) {
			log.debug("Files found... will co importing files");
			new FileProcessor().foreachFile(
					watchDir, 
					PhoboxDefinitions.SUPPORTED_IMPORT_FORMATS, 
					this,
					true);
			
			lastSize = numOfFiles;
		}

		ready = true;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		try {
			FileUtils.copyFileToDirectory(file, Phobox.getModel().getIncomingPath());
		} catch (IOException e) {
			log.error("Could not copy file: " + file.getName());
		}
	}

	public void start() {
		timer.schedule(this, 100, intervalInMillis);
	}
	
	public void stop() {
		timer.cancel();
	}

	@Override
	public boolean isReady() {
		return ready;
	}

}
