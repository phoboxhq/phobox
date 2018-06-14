package de.milchreis.phobox.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import de.milchreis.phobox.gui.ServerGui;

public class ImageProcessing {

	public static void scale(File original, File target, int sizeW, int sizeH) throws IOException {

		if(JavaFXDetector.isAvailable()) {
			ServerGui.scale(original, target, sizeW, sizeH);
			
		} else {
			String format = FilenameUtils.getExtension(target.getName()).toLowerCase();
			BufferedImage img = Scalr.resize(ImageIO.read(original), sizeW, sizeH, null);
			ImageIO.write(img, format, target);
		}
	}
	
	public static void rotate(File original, File target, Rotation degree) throws IOException {

		BufferedImage img = ImageIO.read(original);
		
		if(degree != null) {
			img = Scalr.rotate(img, degree, Scalr.OP_ANTIALIAS);
		}
		
		if(img != null) {
			String format = target.getAbsolutePath().substring(target.getAbsolutePath().length() - 3).toLowerCase();
			ImageIO.write(img, format, target);
		}
	}
	
}
