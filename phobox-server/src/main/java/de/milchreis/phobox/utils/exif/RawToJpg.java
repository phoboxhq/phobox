package de.milchreis.phobox.utils.exif;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;

import it.tidalwave.imageio.cr2.CR2Metadata;
import it.tidalwave.imageio.crw.CRWMetadata;
import it.tidalwave.imageio.orf.ORFMetadata;
import it.tidalwave.imageio.pef.PEFMetadata;
import it.tidalwave.imageio.raf.RAFMetadata;
import it.tidalwave.imageio.srf.SRFMetadata;
import it.tidalwave.imageio.tiff.ThumbnailLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RawToJpg {
	
	public static BufferedImage getJpg(File file) throws Exception {
		return getPreviews(file, false).get(0);
	}
	
	public static List<BufferedImage> getAllJpgs(File file) throws Exception {
		return getPreviews(file, true);
	}
	
	private static List<BufferedImage> getPreviews(File file, boolean all) throws Exception {

		String name = file.getName().toLowerCase();
		
		if(name.endsWith("arw")) {
			return Arrays.asList(ImageIO.read(new ByteArrayInputStream(extractJpegFromRawForARW(file, new FileInputStream(file)))));
		}
		
		if(name.endsWith("nef")) {
			return Arrays.asList(ImageIO.read(new ByteArrayInputStream(extractJpegFromRawForNEF(file, new FileInputStream(file)))));
		}
		
		Iterator<ImageReader> iter = ImageIO.getImageReaders(file);
		
		while(iter.hasNext()) {
			try {
				ImageReader reader = (ImageReader) iter.next();
				reader.setInput(ImageIO.createImageInputStream(file));
				final IIOMetadata metadata = reader.getImageMetadata(0);
				ThumbnailLoader[] thumbs = null;
				
				if(name.endsWith("crw")) {
					final CRWMetadata specificMetadata = (CRWMetadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(name.endsWith("cr2")) {
					final CR2Metadata specificMetadata = (CR2Metadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(name.endsWith("orf")) {
					final ORFMetadata specificMetadata = (ORFMetadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(name.endsWith("raw")) {
					final RAFMetadata specificMetadata = (RAFMetadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(name.endsWith("srf")) {
					final SRFMetadata specificMetadata = (SRFMetadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(name.endsWith("pef")) {
					final PEFMetadata specificMetadata = (PEFMetadata) metadata;
					thumbs = specificMetadata.getThumbnailHelper();
				}
				
				if(all) {
					return extractAllImage(thumbs, file);
				} else {
					return Arrays.asList(extractGreatesImage(thumbs, file));
				}
				
			} catch (Exception e) {
				log.error("Error while reading preview", e);
			}
		}
		
		return null;
	}
	
	private static BufferedImage extractGreatesImage(ThumbnailLoader[] thumbs, File file) throws IOException, ImageProcessingException {
		
		if(thumbs == null || thumbs.length == 0)
			throw new IllegalArgumentException("No thumbnail loaders given");
		
		return Arrays.asList(thumbs).stream()
			.sorted((t1, t2) -> Integer.compare(t2.getHeight(), t1.getHeight()))
			.map(l ->  {
				try {
					return l.load(ImageIO.createImageInputStream(file));
				} catch (IOException e) {
					log.error("Error while loading preview", e);
					return null;
				}
			})
			.findFirst()
			.orElseThrow(() -> new ImageProcessingException("No preview image found"));
		
	}
	
	private static List<BufferedImage> extractAllImage(ThumbnailLoader[] thumbs, File file) throws IOException {
		return Arrays.asList(thumbs)
				.stream()
				.map(l ->  {
					try {
						return l.load(ImageIO.createImageInputStream(file));
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	private static byte[] extractJpegFromRawForNEF(File file, InputStream inStream) throws Exception {
		
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		
		int offset = directory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET);
		int length = directory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH);
		return getBytes(inStream, offset, length);
	}

	private static byte[] extractJpegFromRawForARW(File file, InputStream inStream) throws Exception {

		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

		int offset = directory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET);
		int length = directory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH);
		return getBytes(inStream, offset, length);
	}
	
	private static byte[] getBytes(InputStream inStream, int offset, int length) throws IOException {

		inStream.skip(offset);
		byte[] jpegData = new byte[length];
		inStream.read(jpegData, 0, length);

		return jpegData;
	}

}
