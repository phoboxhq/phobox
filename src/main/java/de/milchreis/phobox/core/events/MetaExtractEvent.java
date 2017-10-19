package de.milchreis.phobox.core.events;

import java.io.File;
import java.sql.Date;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.DBManager;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.ExifHelper;

public class MetaExtractEvent implements IEvent {
	private static final Logger log = Logger.getLogger(MetaExtractEvent.class);

	private PhoboxOperations ops = Phobox.getOperations();

	@Override
	public void onNewFile(File file) {
		String subpath = ops.getWebPath(file);
		
		try {
			Item item = DBManager.getById(subpath, Item.class);
			
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
			}
			
			DBManager.store(item, Item.class);
			
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
