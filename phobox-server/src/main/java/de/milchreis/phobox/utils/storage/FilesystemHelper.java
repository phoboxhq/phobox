package de.milchreis.phobox.utils.storage;

import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.file.filter.ImageFileFilter;
import de.milchreis.phobox.utils.system.OSDetector;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesystemHelper {

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
		try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory, new ImageFileFilter(PhoboxDefinitions.SUPPORTED_VIEW_FORMATS))) {
	        return !dirStream.iterator().hasNext();
	    }
	}

	@SneakyThrows
	public static void openSystemExplorer(File target) {

		OSDetector.OS localOS = OSDetector.getLocalOS();

		if (localOS == OSDetector.OS.WINDOWS) {
			Runtime.getRuntime().exec("explorer.exe /select," + target.getAbsolutePath());

		} else if (localOS == OSDetector.OS.MAC) {
			Runtime.getRuntime().exec("open " + target.getAbsolutePath());

		} else if (localOS == OSDetector.OS.LINUX) {
			Runtime.getRuntime().exec("xdg-open " + target.getAbsolutePath());
		}
	}
}
