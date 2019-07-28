package de.milchreis.phobox.utils.exif;

import java.io.File;
import java.io.IOException;
import java.util.*;

import de.milchreis.phobox.core.model.exif.ExifContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.imgscalr.Scalr.Rotation;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import static de.milchreis.phobox.utils.exif.ExifHelper.ImageRotation.*;

public class ExifHelper {

    @AllArgsConstructor
	public enum ImageRotation {
		CLOCKWISE_90 (6), CLOCKWISE_180 (3), CLOCKWISE_270 (8);
		@Getter
		private int encoding;
	}

	/**
	 * Assigns the EXIF orientation flag to rotation in degrees;
	 */
	@SuppressWarnings("serial")
	public static Map<Integer, Rotation> ORIENTATION_ROTATION_MAP = new HashMap<Integer, Rotation>() {{
		put(1, null);
		put(2, Rotation.FLIP_HORZ);
		put(CLOCKWISE_180.getEncoding(), Rotation.CW_180);
		put(4, null);
		put(5, null);
		put(CLOCKWISE_90.getEncoding(), Rotation.CW_90);
		put(7, null);
		put(CLOCKWISE_270.getEncoding(), Rotation.CW_270);
	}};

	public static Date getCreationDate(File file) throws IOException, ImageProcessingException {
		checkNullFile(file);
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
	}

	public static int getOrientation(File file) throws ImageProcessingException, IOException, MetadataException {
		checkNullFile(file);
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
}
	
	public static int[] getDimension(File file) throws ImageProcessingException, IOException, MetadataException {
		checkNullFile(file);
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		int width = directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
		int height = directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
		return new int[] {width, height};
	}

	public static String[] getCamera(File file) throws ImageProcessingException, IOException {
		checkNullFile(file);
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		return new String[]{
				directory.getString(ExifIFD0Directory.TAG_MAKE),
				directory.getString(ExifIFD0Directory.TAG_MODEL)};
	}
	
	public static ExifContainer getExifDataMap(File file) throws ImageProcessingException, IOException {
		checkNullFile(file);

		ExifContainer container = new ExifContainer();
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		metadata.getDirectories().forEach(directory -> {
			directory.getTags().forEach(container::add);
		});
		
		return container;
	}

	private static void checkNullFile(File file) {
		if(file == null)
			throw new IllegalArgumentException("No file passed");

	}

}
