package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.core.events.model.IEvent;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Date;

@Slf4j
@Component
public class InitializeEvent implements IEvent {

	private PhoboxOperations ops = Phobox.getOperations();
	
	@Autowired
	private ItemRepository itemRepository;

	@Override
	public void onImportFile(File incomingfile, EventLoopInfo loopInfo) {
		String subpath = ops.getWebPath(incomingfile);

		try {
			Item item = loopInfo.getItem();
			if(item == null)
				item = itemRepository.findByFullPath(subpath);

			if(item == null) {
				item = new Item();
				item.setFullPath(subpath);
				item.setImported(new Date(System.currentTimeMillis()));
				item.setFileName(FilenameUtils.getBaseName(incomingfile.getName()));
				item.setFileExtension(FilenameUtils.getExtension(subpath));
				item.setPath(FilenameUtils.getFullPath(subpath));
			}

			loopInfo.setItem(item);

		} catch (Exception e) {
			log.error("Error while saving new file in database", e);
		}
	}

	@Override
	public void onCheckExistingFile(File incomingfile, EventLoopInfo loopInfo) {

		String subpath = ops.getWebPath(incomingfile);
		Item item = itemRepository.findByFullPath(subpath);

		if(item == null) {
			onImportFile(incomingfile, loopInfo);
		}
	}

	@Override
	public void onDeleteFile(File file) {
		String subpath = ops.getWebPath(file);

		try {
			Item item = itemRepository.findByFullPath(subpath);

			if(item != null) {
				itemRepository.delete(item);
			}

		} catch (Exception e) {
			log.error("Error while deleting file in database", e);
		}
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
		try {
			String subpath = ops.getWebPath(directory);
			itemRepository.deleteBySubpath(subpath);
			
		} catch (Exception e) {
			log.error("Error while updateing file path in database", e);
		}
	}

	@Override
	public void onRenameFile(File original, File newFile) {
		String subpath = ops.getWebPath(original);
		String newsubpath = ops.getWebPath(newFile);

		Item item = itemRepository.findByFullPath(subpath);
		
		if(item == null) {
			log.error("Error while updateing file path in database: " + subpath);
			return;
		}
		
		item.setFullPath(newsubpath);
		item.setPath(FilenameUtils.getFullPath(newsubpath));
		item.setFileName(FilenameUtils.getBaseName(newsubpath));
		item.setFileExtension(FilenameUtils.getExtension(newsubpath));
		itemRepository.save(item);
	}
	
	@Override
	public void onRenameDirectory(File directory, File newDirectory) {
		
		try {
			String subpath = ops.getWebPath(directory);
			String newsubpath = ops.getWebPath(newDirectory);
			
			itemRepository.replaceSubpath(subpath, newsubpath);
			
		} catch (Exception e) {
			log.error("Error while updateing file path in database", e);
		}
	}

	@Override
	public void onCreation() {
	}
	
	@Override
	public void onStop() {
	}


}
