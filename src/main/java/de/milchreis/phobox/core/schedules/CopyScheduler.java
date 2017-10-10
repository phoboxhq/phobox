package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;

public class CopyScheduler extends TimerTask {
	private static final Logger log = Logger.getLogger(CopyScheduler.class);

	private int lastSize = 0;
	
	public CopyScheduler(int intervallInMillis) {
		new Timer().schedule(this, 100, intervallInMillis);
	}

	@Override
	public void run() {
	
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
					PhoboxConfigs.SUPPORTED_IMPORT_FORMATS, 
					new FileAction() {
						@Override
						public void process(File file, LoopInfo info) {
							try {
								FileUtils.copyFileToDirectory(file, model.getIncomingPath());
							} catch (IOException e) {
								log.error("Could not copy file: " + file.getName());
							}
						}
					},
					true);
			
			lastSize = numOfFiles;
		}
				
		return;
	}
}
