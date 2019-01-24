package de.milchreis.phobox.utils.image;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import com.mortennobel.imagescaling.MultiStepRescaleOp;

public class ImageProcessing {

	public static BufferedImage getImage(File imageFile) throws IOException {
		return ImageIO.read(imageFile);
	}
	
	public static BufferedImage scale(BufferedImage original, int sizeW, int sizeH) throws IOException {
		
		float ratio = ((float) original.getHeight() / (float) original.getWidth());

		if (ratio <= 1) {
			if (sizeW == original.getWidth())
				return original;

			sizeH = Math.round((float) sizeW * ratio);
		} else {
			if (sizeH == original.getHeight())
				return original;

			sizeW = Math.round((float) sizeH / ratio);
		}
		
		BufferedImage scaledImage = new MultiStepRescaleOp(sizeW, sizeH, RenderingHints.VALUE_INTERPOLATION_BILINEAR).filter(original, null);
		return scaledImage;
	}
	
	public static void scale(BufferedImage original, File target, int sizeW, int sizeH) throws IOException {
		String format = FilenameUtils.getExtension(target.getName()).toLowerCase();
		BufferedImage img = scale(original, sizeW, sizeH);
		ImageIO.write(img, format, target);
	}
	
	@Deprecated
	public static void scale(File original, File target, int sizeW, int sizeH) throws IOException {

//		if(JavaFXDetector.isAvailable()) {
//			UploadController.scale(original, target, sizeW, sizeH);
//
//		} else {
			String format = FilenameUtils.getExtension(target.getName()).toLowerCase();
			BufferedImage img = Scalr.resize(ImageIO.read(original), sizeW, sizeH);
			ImageIO.write(img, format, target);
//		}
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
