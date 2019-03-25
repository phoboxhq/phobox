package de.milchreis.phobox.core.file.filter;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

public class DirectoryFilter implements FileFilter, IOFileFilter, Filter<Path> {

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}

	@Override
	public boolean accept(File dir, String name) {
		return dir.isDirectory();
	}

	@Override
	public boolean accept(Path entry) {
		return entry.toFile().isDirectory();
	}

}
