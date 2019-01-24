package de.milchreis.phobox.utils.storage;

import java.io.File;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileIntegrityChecker {

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
