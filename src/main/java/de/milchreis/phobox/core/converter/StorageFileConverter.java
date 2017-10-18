package de.milchreis.phobox.core.converter;

import java.io.File;

import de.milchreis.phobox.core.Phobox;

public class StorageFileConverter {

	public static File getOriginalFileByThumb(File thumbFile) {
		
		return new File(
				thumbFile.getAbsolutePath()
					.replace("phobox/thumbs/low", "")
					.replace("phobox/thumbs/high", "")
		);
	}
	
	public static File getThumbLowByOriginal(File originalfile) {
		String storage = Phobox.getModel().getStoragePath();
		return new File(
				originalfile.getAbsolutePath()
				.replace(storage, storage + "/phobox/thumbs/low/")
				.replace("//", "/"));
	}
	
	public static File getThumbHighByOriginal(File originalfile) {
		String storage = Phobox.getModel().getStoragePath();
		return new File(
				originalfile.getAbsolutePath()
				.replace(storage, storage + "/phobox/thumbs/high/")
				.replace("//", "/"));
	}
}
