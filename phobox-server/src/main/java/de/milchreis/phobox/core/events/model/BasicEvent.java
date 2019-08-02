package de.milchreis.phobox.core.events.model;

import java.io.File;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.db.entities.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ConfigRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.db.repositories.ItemTagRepository;

@Slf4j
@ToString
@EqualsAndHashCode
@Getter
@Component
public abstract class BasicEvent implements IEvent {

	@Autowired protected ItemRepository itemRepository;
	@Autowired protected AlbumRepository albumRepository;
	@Autowired protected ConfigRepository configRepository;
	@Autowired protected ItemTagRepository itemTagRepository;
	
	@Override
	public abstract void onCreation();

	@Override
	public abstract void onStop();

	@Override
	public abstract void onCheckExistingFile(File incomingfile, EventLoopInfo loopInfo);

	@Override
	public abstract void onDeleteFile(File file);

	@Override
	public abstract void onDeleteDirectory(File directory);

	@Override
	public abstract void onRenameFile(File original, File newFile);
	
	@Override
	public abstract void onRenameDirectory(File directory, File newDirectory);

	public Item getItem(EventLoopInfo loopInfo, File incomingFile) {
		String subpath = Phobox.getOperations().getWebPath(incomingFile);
		Item item = loopInfo.getItem();

		if(item == null) {
			item = itemRepository.findByFullPath(subpath);
		}

		if(item == null)
			throw new IllegalStateException("Item not found in database: " + subpath);

		return item;
	}

}
