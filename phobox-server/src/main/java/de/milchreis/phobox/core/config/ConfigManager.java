package de.milchreis.phobox.core.config;

import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * The ConfigManager is a convince class to access the predefined configurations for the 
 * this application.
 */
@Slf4j
public class ConfigManager {

	public static final String STORAGE_INCOMING = "storage.incomingDirName";
	public static final String STORAGE_APPROVAL = "storage.approval";
	public static final String STORAGE_UNSORTED = "storage.unsorted";
	public static final String STORAGE_THUMBS = "storage.thumbs";
	public static final String STORAGE_DOUBLES = "storage.doubles";
	public static final String STORAGE_MOVIES = "storage.movies";
	public static final String STORAGE_ALBUMS = "storage.albums";

	
	private static Properties props;
	private static Properties generatedProps;
	
	public static String get(String key) {
		if(props == null) {
			init();
		}
		return props.getProperty(key);
	}
	
	public static String getVersion() {
		return generatedProps.getProperty("version");
	}

	private static void init() {
		props = new Properties();
		try {
			props.load(ConfigManager.class.getClassLoader().getResourceAsStream("AppConfig.properties"));
		} catch (IOException e) {
			log.error("Error while loading properties file: " + e.getCause());
		}
		
		generatedProps = new Properties();
		try {
			generatedProps.load(ConfigManager.class.getClassLoader().getResourceAsStream("app.properties"));
		} catch (IOException e) {
			log.error("Error while loading generated properties file: " + e.getCause());
		}
	}
}
