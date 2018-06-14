package de.milchreis.phobox.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.imgscalr.Scalr.Rotation;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ExifHelper {

	/**
	 * Assigns the EXIF orientation flag to rotation in degrees;
	 */
	@SuppressWarnings("serial")
	public static Map<Integer, Rotation> ORIENTATION_ROTATION_MAP = new HashMap<Integer, Rotation>() {{
		put(1, null);
		put(2, Rotation.FLIP_HORZ);
		put(3, Rotation.CW_180);
		put(4, null);
		put(5, null);
		put(6, Rotation.CW_90);
		put(7, null);
		put(8, Rotation.CW_270);
	}};
	
	public static Date getCreationDate(File file) throws IOException, ImageProcessingException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
	}

	public static int getOrientation(File file) throws ImageProcessingException, IOException, MetadataException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
	}
	
	public static int[] getDimension(File file) throws ImageProcessingException, IOException, MetadataException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		int width = directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
		int height = directory.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
		return new int[] {width, height};
	}
	
	public static Map<String, String> getExifDataMap(File file) throws ImageProcessingException, IOException {
		
		Map<String, String> map = new TreeMap<>();
		
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		metadata.getDirectories().forEach(directory -> {
			directory.getTags().forEach(tag -> {
				map.put(tag.getTagName(), tag.getDescription());
			});
		});
		
		return map;
	}
}
