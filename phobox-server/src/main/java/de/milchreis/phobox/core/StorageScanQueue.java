package de.milchreis.phobox.core;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageScanQueue implements Runnable {
	
	private Queue<String> queue;
	private Thread thread;
	private ItemRepository itemRepository;
	
	public StorageScanQueue(ItemRepository itemRepository) {
		queue = new LinkedBlockingQueue<>();
		thread = new Thread(this);
		thread.start();
		this.itemRepository = itemRepository;
	}
	
	public void putScan(File path) {
		if(!queue.contains(path.getAbsolutePath())) {
			queue.add(path.getAbsolutePath());
		}
	}

	@Override
	public void run() {
		while(true) {
				
			if(queue.size() > 0) {

				String path = queue.poll();
				log.debug("Start scan: " + path);
				
				StorageScanScheduler scheduler = new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, new File(path), itemRepository, false);
				scheduler.start();
				
				while(!scheduler.isReady()) {
					sleep(500);
				}
				
				log.debug("Finished scan: " + path);
				
			} else {
				sleep(3000);
			}
		}
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.warn("Error while sleeping");
		}
	}

}
