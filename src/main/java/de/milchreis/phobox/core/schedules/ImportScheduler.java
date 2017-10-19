package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.actions.FileMoveAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;

public class ImportScheduler extends TimerTask {
	private static final Logger log = Logger.getLogger(ImportScheduler.class);
		
	private Timer timer;
	private int intervallInMillis;
	
	public ImportScheduler(int intervallInMillis) {
		this.setIntervallInMillis(intervallInMillis);
		timer = new Timer();
	}

	@Override
	public void run() {
	
		PhoboxModel model = Phobox.getModel();
		FileProcessor importProcessor = Phobox.getImportProcessor();
		
		File incoming = model.getIncomingPath();
		incoming.mkdirs();
		
		if(incoming.list().length > 0 && !importProcessor.isActive()) {
			log.debug("Start importing files");
			importProcessor.foreachFile(
					incoming, 
					PhoboxConfigs.SUPPORTED_IMPORT_FORMATS, 
					new FileMoveAction());
		}
	}
	
	public void start() {
		timer.schedule(this, 100, getIntervallInMillis());
	}
	
	public void stop() {
		timer.cancel();
	}

	public int getIntervallInMillis() {
		return intervallInMillis;
	}

	public void setIntervallInMillis(int intervallInMillis) {
		this.intervallInMillis = intervallInMillis;
	}
}
