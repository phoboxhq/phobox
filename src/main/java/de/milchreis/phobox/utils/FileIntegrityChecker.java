package de.milchreis.phobox.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class FileIntegrityChecker {
	private static final Logger log = Logger.getLogger(FileIntegrityChecker.class);
	

	public static void checkFile(File f, long waitTimeInMillis) {
		try {
			while(true) {
				long t0 = f.lastModified();
				TimeUnit.MILLISECONDS.sleep(waitTimeInMillis);
				log.debug("Waiting");
				if(f.lastModified() == t0)
					return;
			}
			
		} catch (InterruptedException e) {
			log.warn("Error while sleep: " + e.getLocalizedMessage());
		}
	}
}
