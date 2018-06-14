package de.milchreis.phobox.core;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.schedules.StorageScanScheduler;

public class StorageScanQueue implements Runnable {
	private static final Logger log = Logger.getLogger(StorageScanQueue.class);
	
	private Queue<String> queue;
	private Thread thread;
	
	public StorageScanQueue() {
		queue = new LinkedBlockingQueue<>();
		thread = new Thread(this);
		thread.start();
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
				
				StorageScanScheduler scheduler = new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, new File(path), false);
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
