package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.storage.MD5Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class HashEvent extends BasicEvent {

	private PhoboxOperations ops = Phobox.getOperations();
	
	@Override
	public void onNewFile(File file) {
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}

		try {
			String subpath = ops.getWebPath(file);
			Item item = itemRepository.findByFullPath(subpath);
			
			if(item == null)
				throw new IllegalStateException("Item not found in database: " + subpath);

			item.setHash(MD5Helper.getMD5(file));
			itemRepository.save(item);

		} catch (Exception e) {
			log.error("Error while saving hash for new file in database", e);
		}
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
