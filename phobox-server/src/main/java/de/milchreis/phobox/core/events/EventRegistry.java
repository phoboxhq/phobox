package de.milchreis.phobox.core.events;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class EventRegistry implements IEvent {

	private List<IEvent> eventRegistry;

	public EventRegistry() {
		eventRegistry = new ArrayList<>();
	}
	
	@Override
	public void onNewFile(File incomingfile, EventLoopInfo loopInfo) {
		final EventLoopInfo freshLoopInfo = new EventLoopInfo(loopInfo);

		for(IEvent e : eventRegistry) {
			try {
				e.onNewFile(incomingfile, freshLoopInfo);
			} catch (Exception ee) {
				log.warn("Error in event registry catched", ee);
			}
		}
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
