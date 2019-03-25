package de.milchreis.phobox.core.events;

import java.io.File;
import java.io.IOException;

import de.milchreis.phobox.db.entities.Item;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ThumbnailEvent extends BasicEvent {

	private PhoboxOperations ops = Phobox.getOperations();

	@Override
	public void onNewFile(File file, EventLoopInfo loopInfo) {
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}
		
		log.debug("Start ThumbnailEvent for " + file.getAbsolutePath());
		
		File thumbnail = ops.getThumb(file);
		
		if(!thumbnail.exists()) {
			Phobox.processThumbnails(file);
		}
	}

	@Override
	public void onDeleteFile(File file) {
		
		File thumbnail = ops.getThumb(file);
		
		if(thumbnail.exists()) {
			thumbnail.delete();
		}
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
		try {
			FileUtils.deleteDirectory(ops.getThumb(directory));
		} catch (IOException e) {
			log.error("Error while deleting thumbnail directory", e);
		}
	}

	@Override
	public void onRenameFile(File original, File newFile) {
		File thumbDir = ops.getThumb(original);
		File thumbTar = ops.getThumb(newFile);
		thumbDir.renameTo(thumbTar);
	}
	
	@Override
	public void onRenameDirectory(File directory, File newDirectory) {
		onRenameFile(directory, newDirectory);
	}

	@Override
	public void onCreation() {
	}
	
	@Override
	public void onStop() {
	}

}
