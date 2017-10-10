package de.milchreis.phobox.core.model;

public class StorageItem {
	
	public static final String TYPE_DIRECTORY = "dir";
	public static final String TYPE_FILE = "file";
	
	private String name;
	private String time;
	private String path;
	private String type;
	private String preview;
	private String thumbHigh;
	private String thumbLow;
	private String raw;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPreview() {
		return preview;
	}
	
	public void setPreview(String preview) {
		this.preview = preview;
	}
	
	public String getThumbHigh() {
		return thumbHigh;
	}
	
	public void setThumbHigh(String thumbHigh) {
		this.thumbHigh = thumbHigh;
	}
	
	public String getThumbLow() {
		return thumbLow;
	}
	
	public void setThumbLow(String thumbLow) {
		this.thumbLow = thumbLow;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}
}
