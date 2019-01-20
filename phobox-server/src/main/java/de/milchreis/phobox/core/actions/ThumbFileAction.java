package de.milchreis.phobox.core.actions;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.io.FilenameUtils;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.ImageProcessing;
import de.milchreis.phobox.utils.ImportFormatter;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.exif.ExifHelper;
import de.milchreis.phobox.utils.exif.RawToJpg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThumbFileAction implements FileAction {
	
	private int width;
	private int height;
	
	public ThumbFileAction(int maxImgWidth, int maxImgHeight) {
		width = maxImgWidth;
		height = maxImgHeight;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		
		if(!ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_THUMBNAIL_FORMATS)) {
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
			thumbPath = new File(thumbPath, PhoboxDefinitions.STORAGE_UNSORTED);
			thumb = new File(thumbPath, file.getName());
			thumbPath.mkdirs();
		}
			
		try {
			if(!thumb.exists()) {
				BufferedImage image = null;
				
				if(ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_RAW_FORMATS)) {
					image = RawToJpg.getJpg(file);
					thumb = new File(thumb.getAbsolutePath(), FilenameUtils.getBaseName(thumb.getName()) + ".jpg");
				} else {
					image = ImageProcessing.getImage(file);
				}
				
				long t0 = System.currentTimeMillis();
				ImageProcessing.scale(image, thumb, width, height);
				long t1 = System.currentTimeMillis();
				log.debug((t1-t0)/1000.0 + " secs for scale");

				t0 = System.currentTimeMillis();
				ImageProcessing.rotate(thumb, thumb, ExifHelper.ORIENTATION_ROTATION_MAP.get(orientation));
				t1 = System.currentTimeMillis();
				log.debug((t1-t0)/1000.0 + " secs for rotate");
			}
		} catch(Exception e) {
			log.warn("Could not create thumbnail for " + file.getAbsolutePath());
		}
	}
}
