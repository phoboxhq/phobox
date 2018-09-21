package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.actions.FileMoveAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportScheduler extends TimerTask {
		
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
					PhoboxDefinitions.SUPPORTED_IMPORT_FORMATS, 
					new FileMoveAction());
			
			// TODO: Here remove empty directories
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
