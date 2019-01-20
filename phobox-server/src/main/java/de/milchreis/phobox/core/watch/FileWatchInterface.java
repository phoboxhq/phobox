package de.milchreis.phobox.core.watch;

import java.io.File;

public interface FileWatchInterface {
	
	public void created(File file);
	public void deleted(File file);
	public void modified(File file);
	public void renamed(File oldFile, File newFile);
}
