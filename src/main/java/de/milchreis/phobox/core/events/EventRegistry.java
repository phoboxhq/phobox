package de.milchreis.phobox.core.events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventRegistry implements IEvent {

	private List<IEvent> eventRegistry;

	public EventRegistry() {
		eventRegistry = new ArrayList<>();
	}
	
	@Override
	public void onNewFile(File incomingfile) {
		eventRegistry.stream().forEach(e -> e.onNewFile(incomingfile));
	}

	@Override
	public void onDeleteFile(File file) {
		eventRegistry.stream().forEach(e -> e.onDeleteFile(file));
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
	
	public List<IEvent> getEventRegistry() {
		return eventRegistry;
	}

	public void setEventRegistry(List<IEvent> eventRegistry) {
		this.eventRegistry = eventRegistry;
	}
	
}
