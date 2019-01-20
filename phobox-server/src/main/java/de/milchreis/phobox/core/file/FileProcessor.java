package de.milchreis.phobox.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import de.milchreis.phobox.core.file.filter.ImageFileFilter;
import de.milchreis.phobox.utils.FileIntegrityChecker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileProcessor {
	
	public static final String WAITING = "ready";
	public static final String PROCESSING = "processing";
	public static final int WAIT_TIME_IN_MILLIS = 250; 
	
	private String status = WAITING;
	private String state = "";
	private String currentfile = "";

	public void foreachFile(File path, String[] format, FileAction action) {
		foreachFile(path, format, action, true);
	}
	
	public void foreachFile(File path, String[] format, FileAction action, boolean recursivly) {
		List<FileAction> list = new ArrayList<FileAction>();
		list.add(action);
		foreachFile(path, format, list, recursivly, WAIT_TIME_IN_MILLIS);
	}
	
	public void foreachFile(File path, String[] format, List<FileAction> actions) {
		foreachFile(path, format, actions, false, WAIT_TIME_IN_MILLIS);
	}
	
	public void foreachFile(File path, String[] format, List<FileAction> actions,  boolean recursivly, Integer waitTimeInMillis) {
		
		status = PROCESSING;

		final ImageFileFilter filter = new ImageFileFilter(format);
		final LoopInfo info = new LoopInfo();
		final int waiting = waitTimeInMillis == null ? WAIT_TIME_IN_MILLIS : waitTimeInMillis;

		try {
			Files.walkFileTree(
					path.toPath(), 
					EnumSet.noneOf(FileVisitOption.class),
					recursivly ? Integer.MAX_VALUE : 1,
					new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

							File f = path.toFile();
							
							if(!filter.accept(f) || f.isDirectory()) {
								return FileVisitResult.CONTINUE;
							}
							currentfile = f.getAbsolutePath();

							if(waiting > 0) {
								FileIntegrityChecker.checkFile(f, waiting);
							}

							actions.stream().forEach(action -> {
								
								state = action.getClass().getSimpleName();
								try {
									action.process(f, info);
								} catch(Exception e) {
									log.error("Error while performing an action: ", e);
								}
							});
							
							return FileVisitResult.CONTINUE;
						}
			});
			
		} catch (IOException e) {
			log.error("Error while iteration directory: " + path.getAbsolutePath(), e);
		}
		
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
