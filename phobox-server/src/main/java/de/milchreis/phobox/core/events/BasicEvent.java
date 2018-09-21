package de.milchreis.phobox.core.events;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.milchreis.phobox.db.repositories.AlbumRepository;
import de.milchreis.phobox.db.repositories.ConfigRepository;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.db.repositories.ItemTagRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Component
public abstract class BasicEvent implements IEvent {

	@Autowired @Getter
	protected ItemRepository itemRepository;
	
	@Autowired @Getter
	protected AlbumRepository albumRepository;
	
	@Autowired @Getter
	protected ConfigRepository configRepository;
	
	@Autowired @Getter
	protected ItemTagRepository itemTagRepository;
	
	@Override
	public abstract void onCreation();

	@Override
	public abstract void onStop();

	@Override
	public abstract void onNewFile(File incomingfile);

	@Override
	public abstract void onDeleteFile(File file);

	@Override
	public abstract void onDeleteDirectory(File directory);

	@Override
	public abstract void onRenameFile(File original, File newFile);
	
	@Override
	public abstract void onRenameDirectory(File directory, File newDirectory);
	
}
