package de.milchreis.phobox.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.file.filter.ImageFileFilter;

public class FilesystemHelper {

	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");
	
	public static File getTargetpathByDate(File mainPath, Date date) {
		File target = new File(mainPath, YEAR_FORMATTER.format(date));
		
		target = new File(
				target,
				TIMESTAMP_FORMATTER.format(date));
		
		return target;
	}
	
	public static File getRawIfExists(File f, String[] formats) {
		
		String mainPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length()-4);
		
		for(String format : formats) {
			File tmpFile = new File(mainPath+"."+format);
			if(tmpFile.exists())
				return tmpFile; 

			tmpFile = new File(mainPath+"."+format.toUpperCase());
			if(tmpFile.exists())
				return tmpFile; 
		}
		return null;
	}
	
	public static boolean isDirEmpty(final Path directory) throws IOException {
		try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory, new ImageFileFilter(PhoboxConfigs.SUPPORTED_VIEW_FORMATS))) {
	        return !dirStream.iterator().hasNext();
	    }
	}
}
