package de.milchreis.phobox.utils.storage;

import java.io.File;

public class SpaceInfo {

	public static double getFreeSpaceMB(String path) {
		return getFreeSpaceMB(new File(path));
	}
	
	public static double getFreeSpaceMB(File path) {
		long freeBytes = path.getFreeSpace();
		double mb = (freeBytes / 1024) / 1024;
		return mb;
	}

	public static double getMaxSpaceMB(String path) {
		return getMaxSpaceMB(new File(path));
	}
	
	public static double getMaxSpaceMB(File path) {
		long maxBytes = path.getTotalSpace();
		double mb = (maxBytes / 1024) / 1024;
		return mb;
	}
}
