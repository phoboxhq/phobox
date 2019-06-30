package de.milchreis.phobox.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * The PrefernencesManager provides methods to save the user preferences to the local
 * file system.
 */
@Slf4j
public class PreferencesManager {

	@Getter
	private static File storagePath;
	private static File file = new File(System.getProperty("user.home"), ".phobox.properties");

	public static void setStoragePath(File path) {
		storagePath = path;
		save();
	}

	private static void save() {
		try(OutputStream os = new FileOutputStream(file)) {
			Properties props = new Properties();
			props.put("storage.path", storagePath.getAbsolutePath());
			props.store(os, " Preferences for the Phobox image application");

		} catch (Exception e) {
			log.error("Error while saving properties");
		}
	}

	public static Properties init() {
		Properties props = new Properties();

		try(InputStream is = new FileInputStream(file)) {
			props.load(is);
			storagePath = new File(props.getProperty("storage.path"));

		} catch (Exception e) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				log.error("Could not create properties files", e);
			}
		}
		return props;
	}
}
