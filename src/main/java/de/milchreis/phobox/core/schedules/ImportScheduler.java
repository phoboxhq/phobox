package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.actions.FileMoveAction;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.ThumbHelper;

public class ImportScheduler extends TimerTask {
	private static final Logger log = Logger.getLogger(ImportScheduler.class);
		
	private List<FileAction> actions;
	
	public ImportScheduler() {
		actions = new ArrayList<FileAction>();
		actions.add(ThumbHelper.createThumbActionForLow());
		actions.add(ThumbHelper.createThumbActionForHigh());
		actions.add(new FileMoveAction());
	}
	
	public ImportScheduler(int intervallInMillis) {
		this();
		new Timer().schedule(this, 100, intervallInMillis);
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
					actions);
		}
				
		return;
	}
}
