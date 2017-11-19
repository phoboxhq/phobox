package de.milchreis.phobox.core.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

import org.apache.commons.io.filefilter.IOFileFilter;

import de.milchreis.phobox.utils.ListHelper;

public class ImageFileFilter implements FileFilter, IOFileFilter, Filter<Path>  {

	private String[] supportedFileExt;
	
	public ImageFileFilter(String[] supportedFileExtentions) {
		supportedFileExt = supportedFileExtentions;
	}
	
	@Override
	public boolean accept(File pathname) {
		return ListHelper.endsWith(pathname.getName(), supportedFileExt) || pathname.isDirectory();
	}

	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}

	@Override
	public boolean accept(Path entry) throws IOException {
		return accept(entry.toFile());
	}
	
}
