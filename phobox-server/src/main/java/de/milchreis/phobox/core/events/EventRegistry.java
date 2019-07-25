package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.core.events.model.IEvent;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class EventRegistry implements IEvent {

	private List<IEvent> eventRegistry;
	private ItemRepository itemRepository;

	public EventRegistry() {
		eventRegistry = new ArrayList<>();
	}

	@Transactional
	@Override
	public void onImportFile(File incomingfile, EventLoopInfo eventLoopInfo) {

		if(itemRepository == null) {
			throw new IllegalStateException("ItemRepository is not set");
		}

		final EventLoopInfo freshLoopInfo = new EventLoopInfo(eventLoopInfo);

		for(IEvent e : eventRegistry) {
			try {
				e.onImportFile(incomingfile, freshLoopInfo);

				if(freshLoopInfo.isStopLoop()) {
					throw new FileAlreadyExistsException(freshLoopInfo.getStopLoopReason());
				}

			} catch (FileAlreadyExistsException ee) {
				log.warn(ee.getLocalizedMessage());

			} catch (Exception ee) {
				log.warn("Error in event registry catched", ee);
			}
		}

		itemRepository.save(freshLoopInfo.getItem());
	}

	@Transactional
	@Override
	public void onCheckExistingFile(File incomingfile, EventLoopInfo loopInfo) {
		if(itemRepository == null) {
			throw new IllegalStateException("ItemRepository is not set");
		}

		final EventLoopInfo freshLoopInfo = new EventLoopInfo(loopInfo);

		for(IEvent e : eventRegistry) {
			try {
				e.onCheckExistingFile(incomingfile, freshLoopInfo);

				if(freshLoopInfo.isStopLoop()) {
					throw new FileAlreadyExistsException(freshLoopInfo.getStopLoopReason());
				}

			} catch (FileAlreadyExistsException ee) {
				log.warn(ee.getLocalizedMessage());

			} catch (Exception ee) {
				log.warn("Error in event registry catched", ee);
			}
		}

		itemRepository.save(freshLoopInfo.getItem());
	}

	@Override
	public void onDeleteFile(File file) {
		eventRegistry.stream().forEach(e -> e.onDeleteFile(file));
	}
	
	@Override
	public void onDeleteDirectory(File directory) {
		eventRegistry.stream().forEach(e -> e.onDeleteDirectory(directory));
	}

	@Override
	public void onRenameFile(File original, File newFile) {
		eventRegistry.stream().forEach(e -> e.onRenameFile(original, newFile));
	}
	
	@Override
	public void onRenameDirectory(File directory, File newDirectory) {
		eventRegistry.stream().forEach(e -> e.onRenameDirectory(directory, newDirectory));
	}

	@Override
	public void onCreation() {
		eventRegistry.stream().forEach(e -> e.onCreation());
	}
	
	@Override
	public void onStop() {
		eventRegistry.stream().forEach(e -> e.onStop());
	}


	public void addEvent(IEvent plugin) {
		eventRegistry.add(plugin);
	}
	
}
