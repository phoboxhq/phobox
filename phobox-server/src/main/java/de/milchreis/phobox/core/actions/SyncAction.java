package de.milchreis.phobox.core.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.LoopInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyncAction implements FileAction {
	
	private File source;
	private File target;
	
	public SyncAction(File source, File target) {
		this.source = source;
		this.target = target;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		
		String subpath = file.getAbsolutePath().replace(source.getAbsolutePath(), "");
		File destFile = new File(target, subpath);
		
		try {
			if(destFile.isFile()) {
				if(destFile.lastModified() < file.lastModified()) {
					FileUtils.copyFile(file, destFile);
				}
				
			} else {
				new File(FilenameUtils.getPath(destFile.getAbsolutePath())).mkdirs();
				FileUtils.copyFile(file, destFile);
			}	

		} catch(Exception e) {
			log.warn("Error while copy process", e);
		}
	}
}
