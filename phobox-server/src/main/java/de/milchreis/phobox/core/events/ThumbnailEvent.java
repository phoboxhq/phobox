package de.milchreis.phobox.core.events;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.operations.PhoboxOperations;
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
		
		File thumbnail = Phobox.getThumbnailOperations().getPhysicalThumbnail(file);
		
		if(!thumbnail.exists()) {
			Phobox.processThumbnails(file);
		}
	}

	@Override
	public void onDeleteFile(File file) {
		
		File thumbnail = Phobox.getThumbnailOperations().getPhysicalThumbnail(file);
		
		if(thumbnail.exists()) {
			thumbnail.delete();
		}
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
		try {
			File thumbnail = Phobox.getThumbnailOperations().getPhysicalThumbnail(directory);
			FileUtils.deleteDirectory(thumbnail);
		} catch (IOException e) {
			log.error("Error while deleting thumbnail directory", e);
		}
	}

	@Override
	public void onRenameFile(File original, File newFile) {
		File thumbDir = Phobox.getThumbnailOperations().getPhysicalThumbnail(original);
		File thumbTar = Phobox.getThumbnailOperations().getPhysicalThumbnail(newFile);
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
