package de.milchreis.phobox.utils.storage;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FileIntegrityChecker {

	public static void waitUntilIsComplete(File f, long waitTimeInMillis) {
		try {
			while(true) {
			    log.debug(String.valueOf(f.lastModified()));
				long t0 = f.lastModified();
			    long now = System.currentTimeMillis();

			    if((now - t0) > (waitTimeInMillis*3)) {
			        return;
                }

				TimeUnit.MILLISECONDS.sleep(waitTimeInMillis);

				if(f.lastModified() == t0) {
					return;
                }
			}
			
		} catch (InterruptedException e) {
			log.warn("Error while sleep: " + e.getLocalizedMessage());
		}
	}
}
