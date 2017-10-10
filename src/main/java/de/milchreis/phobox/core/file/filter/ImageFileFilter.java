package de.milchreis.phobox.core.file.filter;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.IOFileFilter;

import de.milchreis.phobox.utils.ListHelper;

public class ImageFileFilter implements FileFilter, IOFileFilter  {

	private String[] supportedFileExt;
	
	public ImageFileFilter(String[] supportedFileExtentions) {
		supportedFileExt = supportedFileExtentions;
	}
	
	@Override
	public boolean accept(File pathname) {
		return ListHelper.endsWith(pathname.getName(), supportedFileExt);
	}

	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
	
}
