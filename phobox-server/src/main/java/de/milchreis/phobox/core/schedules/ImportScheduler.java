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
public class ImportScheduler extends TimerTask implements Schedulable {
		
	private Timer timer;
	private int intervalInMillis;
	private int delay;
	private boolean ready = false;
	
	public ImportScheduler(int intervalInMillis, int delay) {
		this.intervalInMillis = intervalInMillis;
		this.delay = delay;
		timer = new Timer();
	}

	@Override
	public void run() {
		ready = false;

		PhoboxModel model = Phobox.getModel();
		FileProcessor importProcessor = Phobox.getImportProcessor();
		
		File incoming = model.getIncomingPath();
		incoming.mkdirs();
		
		if(incoming.list().length > 0 && !importProcessor.isActive()) {
			importProcessor.foreachFile(
					incoming, 
					PhoboxDefinitions.SUPPORTED_IMPORT_FORMATS, 
					new FileMoveAction());
			
			// TODO: Here remove empty directories
		}

		ready = true;
	}

	@Override
	public void start() {
		timer.schedule(this, delay, intervalInMillis);
	}

	@Override
	public void stop() {
		timer.cancel();
	}

	@Override
	public boolean isReady() {
		return ready;
	}

}
