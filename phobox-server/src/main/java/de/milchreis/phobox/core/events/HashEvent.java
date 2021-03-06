package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.events.model.BasicEvent;
import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.storage.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class HashEvent extends BasicEvent {

	@Override
	public void onImportFile(File file, EventLoopInfo loopInfo) {
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}

		try {
			Item item = getItem(loopInfo, file);

			if(item.getHash() == null) {
				item.setHash(HashUtil.sha1File(file));
				loopInfo.setItem(item);
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
