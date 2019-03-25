package de.milchreis.phobox.core.events;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.Date;

import de.milchreis.phobox.utils.image.CameraNameFormatter;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.exif.ExifHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MetaExtractEvent extends BasicEvent {

	private PhoboxOperations ops = Phobox.getOperations();
	
	@Override
	public void onNewFile(File file, EventLoopInfo loopInfo) {
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}
		
		try {
			Item item = getItem(loopInfo, file);

			try {
				if(item.getRotation() == null)
					item.setRotation(ExifHelper.getOrientation(file));
			} catch(Exception e) {
				log.warn("Could not read rotation information of " + item.getFileName());
			}

			try {
				if(item.getCreation() == null)
					item.setCreation(new Date(ExifHelper.getCreationDate(file).getTime()));
			} catch(Exception e) {
				log.warn("Could not read creation information of " + item.getFileName());
			}

			try {
				if(item.getCamera() == null) {
					String[] exifData = ExifHelper.getCamera(file);
					item.setCamera(CameraNameFormatter.getFormattedCameraName(exifData));
				}
			} catch(Exception e) {
				log.warn("Could not read camera vendor of " + item.getFileName());
			}

			try {
				if(item.getWidth() == null || item.getHeight() == null) {
					int[] dimension = ExifHelper.getDimension(file);
					item.setWidth(dimension[0]);
					item.setHeight(dimension[1]);
				}

			} catch(Exception e) {
				Image img = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
				item.setWidth(img.getWidth(null));
				item.setHeight(img.getHeight(null));
			}
			
			itemRepository.save(item);
			loopInfo.setItem(item);
			
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
