package de.milchreis.phobox.core.actions;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.utils.ExifHelper;
import de.milchreis.phobox.utils.ImageProcessing;

public class ReThumbFileAction implements FileAction {
	private static final Logger log = Logger.getLogger(ReThumbFileAction.class);

	private int width;
	private int height;
	
	public ReThumbFileAction(int maxImgWidth, int maxImgHeight) {
		width = maxImgWidth;
		height = maxImgHeight;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		log.debug(file.getName());
		
		String fileStr = file.getAbsolutePath();
		if(fileStr.contains("thumbs"))
			return;
		
		File target = Phobox.getOperations().getThumb(file);
			
		if(!target.exists()) {
			target.getParentFile().mkdirs();
			int orientation = 1;
			
			try {
				orientation = ExifHelper.getOrientation(file);
			} catch(Exception ee) {
				log.info("Could not find orientation flag for " + file.getAbsolutePath());
			}
			
			try {
				ImageProcessing.scale(file, target, width, height);
				ImageProcessing.rotate(target, target, ExifHelper.ORIENTATION_ROTATION_MAP.get(orientation));
			} catch (IOException e) {
				log.info("Could not create thumb for " + file.getAbsolutePath());
			}
		}
	}
}
