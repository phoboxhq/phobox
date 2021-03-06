package de.milchreis.phobox.core;

public class PhoboxDefinitions {

	public static String[] SUPPORTED_RAW_FORMATS = {
			"cr2", "nef", "orf", "arw", "crw"
	};

	public static String[] SUPPORTED_IMPORT_FORMATS = {
			"jpg", "jpeg",
			"cr2", "nef", "orf", "arw", "crw",
			"mp4", "mov"
	};
	
	public static String[] SUPPORTED_THUMBNAIL_FORMATS = {
			"jpg", "jpeg",
			"cr2", "nef", "arw", "crw"
	};

	public static String[] SUPPORTED_VIEW_FORMATS = {"jpg", "jpeg", "png"};
	
	public static int THUMB_WIDTH = 800;
	public static int THUMB_HEIGHT = 800;

	public static final String STORAGE_INCOMING = "import";
	public static final String STORAGE_APPROVAL = "approval";
	public static final String STORAGE_UNSORTED = "unsorted";
	public static final String STORAGE_THUMBS = "thumbs";
	public static final String STORAGE_DOUBLES = "doubles";
	public static final String STORAGE_MOVIES = "movies";
	public static final String STORAGE_ALBUMS = "albums";
	
}
