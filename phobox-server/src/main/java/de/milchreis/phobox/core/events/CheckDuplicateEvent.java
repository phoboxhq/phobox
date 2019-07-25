package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.events.model.BasicEvent;
import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.db.entities.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CheckDuplicateEvent extends BasicEvent {

	@Override
	public void onImportFile(File incomingfile, EventLoopInfo loopInfo) {
		// Skip directory items
		if(incomingfile.isDirectory()) {
			return;
		}

		try {
			Item item = itemRepository.findByHash(loopInfo.getItem().getHash());

			if(item != null) {
				loopInfo.stopLoop("Duplicate image: Image already exists in " + item.getFullPath());
			}

		} catch (Exception e) {
			log.error("Error while saving hash for new file in database", e);
		}
	}

	@Override
	public void onCheckExistingFile(File file, EventLoopInfo loopInfo) {
	}

	@Override
	public void onDeleteFile(File file) {
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
	}

	@Override
	public void onRenameFile(File original, File newFile) {
	}
	
	@Override
	public void onRenameDirectory(File directory, File newDirectory) {
	}

	@Override
	public void onCreation() {
	}
	
	@Override
	public void onStop() {
	}

}
