package de.milchreis.phobox.core.actions;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.ConfigManager;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.ExifHelper;
import de.milchreis.phobox.utils.ImageProcessing;
import de.milchreis.phobox.utils.ImportFormatter;
import de.milchreis.phobox.utils.ListHelper;

public class ThumbFileAction implements FileAction {
	private static final Logger log = Logger.getLogger(ThumbFileAction.class);
	
	public static String[] IMAGE_FORMATS = {"jpg", "jpeg", "png", "bmp"};

	private int width;
	private int height;
	
	public ThumbFileAction(int maxImgWidth, int maxImgHeight) {
		width = maxImgWidth;
		height = maxImgHeight;
	}
	
	
	@Override
	public void process(File file, LoopInfo info) {
		
		if(!ListHelper.endsWith(file.getName(), IMAGE_FORMATS)) {
			log.info("Skipped, because no supported image file " + file.getAbsolutePath());
			return;
		}
		
		PhoboxModel model = Phobox.getModel();
		File thumbPath = model.getThumbPath();
		File thumb = null;
		int orientation = 1;
		
		try {
			ImportFormatter importFormatter = new ImportFormatter(model.getImportFormat());
			File dirStructure = importFormatter.createPath(file);
			thumbPath = new File(thumbPath, dirStructure.toString());
			
			thumbPath.mkdirs();
			thumb = new File(thumbPath, file.getName());
			try {
				orientation = ExifHelper.getOrientation(file);
			} catch(Exception ee) {
				log.info("Could not find orientation flag for " + file.getAbsolutePath());
			}
			
		} catch (Exception e) {
			log.info("Could not create thumbnail by date for " + file.getAbsolutePath());
			thumbPath = new File(thumbPath, ConfigManager.get(ConfigManager.STORAGE_UNSORTED));
			thumb = new File(thumbPath, file.getName());
			thumbPath.mkdirs();
		}
			
		try {
			if(!thumb.exists()) {
				long t0 = System.currentTimeMillis();
				ImageProcessing.scale(file, thumb, width, height);
				long t1 = System.currentTimeMillis();
				log.debug((t1-t0)/1000.0 + " secs for scale");

				t0 = System.currentTimeMillis();
				ImageProcessing.rotate(thumb, thumb, ExifHelper.ORIENTATION_ROTATION_MAP.get(orientation));
				t1 = System.currentTimeMillis();
				log.debug((t1-t0)/1000.0 + " secs for rotate");
			}
		} catch(IOException e) {
			log.warn("Could not create thumbnail for " + file.getAbsolutePath());
		}
		
		return;
	}
}
