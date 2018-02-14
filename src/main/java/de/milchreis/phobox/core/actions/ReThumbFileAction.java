package de.milchreis.phobox.core.actions;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.ExifHelper;
import de.milchreis.phobox.utils.ImageProcessing;

public class ReThumbFileAction implements FileAction {
	private static final Logger log = Logger.getLogger(ReThumbFileAction.class);

	private int width;
	private int height;
	private File path;
	
	public ReThumbFileAction(int maxImgWidth, int maxImgHeight, File targetPath) {
		width = maxImgWidth;
		height = maxImgHeight;
		path = targetPath;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		log.debug(file.getName());
		
		PhoboxModel model = Phobox.getModel();
		File storage = new File(model.getStoragePath());
		
		String fileStr = file.getAbsolutePath();
		if(fileStr.contains("thumbs"))
			return;
		
		// Set target path in thumbs from current storage position
		File target = new File(
				path,
				file.getAbsolutePath().replace(storage.getAbsolutePath(), ""));
			
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
