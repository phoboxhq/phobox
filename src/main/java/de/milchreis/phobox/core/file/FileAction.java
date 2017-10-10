package de.milchreis.phobox.core.file;

import java.io.File;

public interface FileAction {

	public void process(File file, LoopInfo info);
}
