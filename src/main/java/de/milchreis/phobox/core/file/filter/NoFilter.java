package de.milchreis.phobox.core.file.filter;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class NoFilter implements IOFileFilter  {

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

	@Override
	public boolean accept(File file) {
		return true;
	}
}
