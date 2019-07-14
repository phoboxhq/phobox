package de.milchreis.phobox.server.services;

import com.drew.imaging.ImageProcessingException;
import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.core.model.exif.ExifContainer;
import de.milchreis.phobox.utils.exif.ExifHelper;
import de.milchreis.phobox.utils.phobox.ListHelper;
import de.milchreis.phobox.utils.storage.FilesystemHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class PhotoService implements IPhotoService {
	
	@Autowired private ItemRepository itemRepository;
	
	@Override
	public StorageItem getItem(String webFilePath) {
		return getItem(Phobox.getOperations().getPhysicalFile(webFilePath));
	}

	@Override
	public StorageItem getItem(File physicalFile) {
		return generateStorageItem(physicalFile, null);
	}

	@Override
	public StorageItem getItem(Item dbItem) {
		return generateStorageItem(null, dbItem);
	}

	@Override
	public ExifContainer getExifData(String webFilePath) throws ImageProcessingException, IOException {
		return ExifHelper.getExifDataMap(Phobox.getOperations().getPhysicalFile(webFilePath));
	}

	@Override
	public void rename(String webItemPath, String targetname) throws Exception {
		File dir = Phobox.getOperations().getPhysicalFile(webItemPath);
		Phobox.getOperations().rename(dir, targetname);
	}
	
	@Override
	public void delete(String webItemPath) throws Exception {
		File dir = Phobox.getOperations().getPhysicalFile(webItemPath);
		Phobox.getOperations().delete(dir);
	}
	
	@Override
	public void move(String webItemPath, String targetDir) throws Exception {
		
		PhoboxOperations ops = Phobox.getOperations();
		File dir = ops.getPhysicalFile(webItemPath);
		File tar = ops.getPhysicalFile(targetDir);
		
		if(!tar.exists()) {
			tar.mkdirs();
		}
		
		if(dir.isFile() && tar.isDirectory()) {
			ops.moveFile(dir, tar);
		}
		
		if(dir.isDirectory() && tar.isDirectory()) {
			ops.moveDir(dir, tar);
		}
	}

	private boolean isValidItem(File physicalItem) {
		return (physicalItem.exists())
				|| physicalItem.isDirectory()
				|| (physicalItem.isFile() && ListHelper.endsWith(physicalItem.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS));
	}

	private StorageItem generateStorageItem(File physicalFile, Item dbItem) {

		PhoboxOperations ops = Phobox.getOperations();

		if(dbItem == null && physicalFile == null)
			return null;

		if(physicalFile == null)
			physicalFile = ops.getPhysicalFile(dbItem.getFullPath());

		if(dbItem == null && physicalFile.isFile())
			dbItem = itemRepository.findByFullPath(ops.getWebPath(physicalFile));

		if(!isValidItem(physicalFile)) {
			return null;
		}

		String filename = physicalFile.getName();
		String directory = ops.getWebPath(physicalFile).replace(filename, "");
		File thumbnail = Phobox.getThumbnailOperations().getPhysicalThumbnail(physicalFile);

		StorageItem storageItem = new StorageItem();

		storageItem.setName(FilenameUtils.getBaseName(physicalFile.getName()));
		storageItem.setPath(ops.getWebPath(new File(directory, physicalFile.getName())));
		storageItem.setTypeByFile(physicalFile);

		File rawFile = FilesystemHelper.getRawIfExists(physicalFile, PhoboxDefinitions.SUPPORTED_RAW_FORMATS);
		if(rawFile != null) {
			File raw = new File(directory, rawFile.getName());
			storageItem.setRaw(ops.getWebPath(raw));
		}

		if(physicalFile.isDirectory()) {
			List<String> previewFiles = ops.getFiles(physicalFile, 1);
			if(previewFiles.size() >= 1) {

				File previewFile = ops.getPhysicalFile(previewFiles.get(0));
				File previewThumb = Phobox.getThumbnailOperations().getPhysicalThumbnail(previewFile);

				storageItem.setPreview(ops.getStaticResourcePath(new File(ops.getWebPath(previewThumb))));

				if(!previewThumb.exists()) {

					// Add to database and create thumbnail
					Phobox.getEventRegistry().onNewFile(previewFile, null);

					// Set waiting icon
					storageItem.setGeneratingThumb(true);
				}
			}

		} else {

			storageItem.setThumb(ops.getStaticResourcePath(thumbnail));

			// Add landscape/portrait information by database item
			if(dbItem != null && dbItem.getRotation() != null) {
				storageItem.setLandscape(dbItem.isLandscape());
			}

			if(dbItem != null && dbItem.getCreation() != null) {
				storageItem.setTime(dbItem.getCreation().toString());
			}

			// Check existence of the thumbnails
			if(!thumbnail.exists()) {

				// Add to database and create thumbnail
				Phobox.getEventRegistry().onNewFile(physicalFile, null);

				// Set waiting icon
				storageItem.setGeneratingThumb(true);
			}
		}

		return storageItem;
	}
}
