package de.milchreis.phobox.core.converter;

import java.io.File;

public class StorageFileConverter {

	public static File getOriginalFileByThumb(File thumbFile) {
		
		return new File(
				thumbFile.getAbsolutePath()
					.replace("phobox/thumbs/low", "")
					.replace("phobox/thumbs/high", "")
		);
	}
}
