package de.milchreis.phobox.core.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

import org.apache.commons.io.filefilter.IOFileFilter;

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
	public boolean accept(Path entry) throws IOException {
		return entry.toFile().isDirectory();
	}

}
