package de.milchreis.phobox.core.watch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleWatchDog implements Runnable {
	
	private Path path;
	private WatchService watcher;
	private Map<WatchKey,Path> keys;
	private FileWatchInterface behavior;
	
	public SimpleWatchDog(File path, FileWatchInterface watchBehavoir) throws IOException {
		this.path = path.toPath();
		this.keys = new HashMap<WatchKey,Path>();
		this.watcher = FileSystems.getDefault().newWatchService();
		this.behavior = watchBehavoir;

		log.info("Scanning: " + this.path);
        registerAll(this.path);
        log.info("scanning done");
		
	    new Thread(this).start();
	}
	
	@Override
	public void run() {
		for(;;) {
		    // wait for key to be signaled
		    WatchKey key;
		    try {
		        key = watcher.take();
		    } catch (InterruptedException x) {
		        return;
		    }

		    List<WatchEvent<?>> events = key.pollEvents();
		    
		    for(int i=0; i<events.size(); i++) {
		        WatchEvent<?> event = events.get(i);
		        WatchEvent.Kind<?> kind = event.kind();
		    	
		        File file = getFile(event);
		        Path filename = file.toPath();
		        
		        if(kind == StandardWatchEventKinds.OVERFLOW) {
		        	continue;
		        }

		        if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
		        	behavior.created(file);
		        }
		        
		        if(kind == StandardWatchEventKinds.ENTRY_DELETE) {
		        	if(i+1<events.size()) {
		        		WatchEvent<?> nextEvent = events.get(i+1);
		        		if(nextEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
		        			behavior.renamed(file, getFile(nextEvent));
		        			i++;
		        		}
		        		
		        	} else {
		        		behavior.deleted(file);
		        	}
		        }
		        
		        if(kind == StandardWatchEventKinds.ENTRY_MODIFY) {
		        	behavior.modified(file);
		        }
		        
		        log.info(filename + " " + event.kind().name());
		    }
		    
		    boolean valid = key.reset();
		    if(!valid) {
		        break;
		    }
		}
	}
	
	private File getFile(WatchEvent<?> event) {
        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = (WatchEvent<Path>)event;
        Path filename = ev.context();
        File file = filename.toFile();
        return file;
	}

	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, 
				StandardWatchEventKinds.ENTRY_CREATE, 
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

		Path prev = keys.get(key);
		if (prev == null) {
			log.info("register: " + dir);
		} else {
			if (!dir.equals(prev)) {
				log.info("update: " + prev + " -> " + dir);
			}
		}
		keys.put(key, dir);
	}
}
