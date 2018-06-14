package de.milchreis.phobox.core.model;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.actions.ReThumbFileAction;
import de.milchreis.phobox.utils.ThumbHelper;

public class ThumbProcessorQueue implements Runnable {
	private static final Logger log = Logger.getLogger(ThumbProcessorQueue.class);
	
	private Queue<File> thumbQueue;
	
	public ThumbProcessorQueue() {

		thumbQueue = new LinkedBlockingQueue<>();
		new Thread(this).start();
	}

	public void put(File file) {
		thumbQueue.add(file);
	}

	@Override
	public void run() {

		// Start delay with 5 seconds
		sleep(5000);

		ReThumbFileAction thumbAction = ThumbHelper.createReThumbAction();
		
		while(true) {
		
			if(thumbQueue.size() == 0) {
				sleep(500);
				
			} else {
				File file = thumbQueue.poll();
				try {
					thumbAction.process(file, null);
				} catch(Exception e) {
					log.error("Error while creating thumbnails" , e);
				}
			}
		}
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.warn("Error while sleeping command");
		}
	}

}
