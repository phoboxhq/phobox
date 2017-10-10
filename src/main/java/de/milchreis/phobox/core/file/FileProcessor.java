package de.milchreis.phobox.core.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import de.milchreis.phobox.core.file.filter.ImageFileFilter;
import de.milchreis.phobox.utils.FileIntegrityChecker;

public class FileProcessor {
	private static Logger log = Logger.getLogger(FileProcessor.class);
	
	public static final String WAITING = "ready";
	public static final String PROCESSING = "processing";
	public static final int WAIT_TIME_IN_MILLIS = 250; 
	
	private String status = WAITING;
	private String state = "";
	private String currentfile = "";

	public void foreachFile(File path, String[] format, FileAction action) {
		foreachFile(path, format, action, false);
	}
	
	public void foreachFile(File path, String[] format, FileAction action, boolean recursivly) {
		List<FileAction> list = new ArrayList<FileAction>();
		list.add(action);
		foreachFile(path, format, list, recursivly);
	}
	
	public void foreachFile(File path, String[] format, List<FileAction> actions) {
		foreachFile(path, format, actions, false);
	}
	
	public void foreachFile(File path, String[] format, List<FileAction> actions,  boolean recursivly) {
		
		status = PROCESSING;

		ImageFileFilter filter = new ImageFileFilter(format);
		Iterator<File> it = FileUtils.iterateFiles(path, null, recursivly);
		LoopInfo info = new LoopInfo();
		
		it.forEachRemaining(f -> {
			if(!filter.accept(f)) {
				return;
			}
			currentfile = f.getAbsolutePath();
	
			FileIntegrityChecker.checkFile(f, WAIT_TIME_IN_MILLIS);
			
			actions.stream().forEach(action -> {
				long t0 = System.currentTimeMillis();
				state = action.getClass().getSimpleName();
				try {
					action.process(f, info);
				} catch(Exception e) {
					log.error("Error while performing an action: ", e);
				}
				long t1 = System.currentTimeMillis();
				log.debug(state + " in " + (t1-t0)/1000.0 + " secs");
			});
		});
		
		state = "";
		currentfile = "";
		status = WAITING;
	}
	
	public boolean isActive() {
		synchronized (status) {
			return (status == PROCESSING);
		}
	}

	public String getCurrentfile() {
		return currentfile;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getState() {
		return state;
	}
}
