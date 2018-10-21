package de.milchreis.phobox.core.storage;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.db.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageScanQueue implements Runnable {
	
	private Queue<String> queue;
	private ItemRepository itemRepository;
	private StorageScanScheduler scheduler;
	
	public StorageScanQueue(ItemRepository itemRepository) {
		this.queue = new LinkedBlockingQueue<>();
		this.itemRepository = itemRepository;
		
		Executors.newFixedThreadPool(1).execute(this);
	}
	
	public void putScan(File path) {
		if(!queue.contains(path.getAbsolutePath())) {
			queue.add(path.getAbsolutePath());
		}
		log.debug(getInfo().toString());
	}

	@Override
	public void run() {
		
		while(true) {
				
			if(queue.size() > 0) {

				String path = queue.poll();
				log.debug("Start scan: " + path);
				
				scheduler = new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, new File(path), itemRepository, false);
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
	
	public StorageScanQueueInfo getInfo() {
		return new StorageScanQueueInfo(
				new File(queue.peek()),
				scheduler != null ? scheduler.getCurrentFile() : null,
				queue.size());
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.warn("Error while sleeping");
		}
	}

}
