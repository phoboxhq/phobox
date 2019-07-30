package de.milchreis.phobox.core.events;

import java.awt.*;
import java.io.File;
import java.sql.Timestamp;

import com.drew.metadata.exif.ExifIFD0Directory;
import de.milchreis.phobox.core.events.model.BasicEvent;
import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.core.model.exif.ExifContainer;
import de.milchreis.phobox.utils.image.CameraNameFormatter;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.utils.exif.ExifHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MetaExtractEvent extends BasicEvent {

	@Override
	public void onImportFile(File file, EventLoopInfo loopInfo) {
		// Skip directory items
		if(file.isDirectory()) {
			return;
		}

		onCheckExistingFile(file, loopInfo);
	}


	@Override
	public void onCheckExistingFile(File file, EventLoopInfo loopInfo) {
		try {
			Item item = getItem(loopInfo, file);
			updateMetaData(file, item);
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

	private void updateMetaData(File file, Item item) {

		try {
			if(item.getRotation() == null)
				item.setRotation(ExifHelper.getOrientation(file));
		} catch(Exception e) {
			log.warn("Could not read rotation information of " + item.getFileName());
		}

		try {
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
			ExifContainer exifData = ExifHelper.getExifDataMap(file);

			if(item.getLens() == null) {
				item.setLens(exifData.getLens());
			}

			if(item.getFocalLength() == null) {
				item.setFocalLength(exifData.getValueByTagId(ExifIFD0Directory.TAG_FOCAL_LENGTH));
			}

			if(item.getCreation() == null || item.getCreation().toString().endsWith("00:00:00.0")) {
				item.setCreation(exifData.getCreation());
			}

			try {
				if(item.getWidth() == null || item.getHeight() == null) {
					Dimension dimension = exifData.getDimension();
					item.setWidth(dimension.width);
					item.setHeight(dimension.height);
				}

			} catch(Exception e) {
				Image img = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
				item.setWidth(img.getWidth(null));
				item.setHeight(img.getHeight(null));
			}
		} catch(Exception e) {
			log.warn("Could not read exif data of " + item.getFileName());
		}

	}

}
