package de.milchreis.phobox.core.events;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.Date;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.ExifHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetaExtractEvent implements IEvent {

	private PhoboxOperations ops = Phobox.getOperations();

	@Override
	public void onNewFile(File file) {
		String subpath = ops.getWebPath(file);
		
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}
		
		try {
			Item item = ItemRepository.getItem(subpath);
			
			try {
				item.setRotation(ExifHelper.getOrientation(file));
			} catch(Exception e) {
			}
			
			try {
				item.setCreation(new Date(ExifHelper.getCreationDate(file).getTime()));
			} catch(Exception e) {
			}
			
			try {
				int[] dimension = ExifHelper.getDimension(file);
				item.setWidth(dimension[0]);
				item.setHeight(dimension[1]);
				
			} catch(Exception e) {
				Image img = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
				item.setWidth(img.getWidth(null));
				item.setHeight(img.getHeight(null));
			}
			
			ItemRepository.store(item);
			
		} catch (Exception e) {
			log.error("Error while saving new file in database", e);
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
