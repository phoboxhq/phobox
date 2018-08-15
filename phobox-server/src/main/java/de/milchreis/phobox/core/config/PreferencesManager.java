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

import de.milchreis.phobox.utils.MD5Helper;
import lombok.extern.slf4j.Slf4j;

/**
 * The PrefernencesManager provides methods to save the user preferences to the local
 * file system.
 */
@Slf4j
public class PreferencesManager {

	public static final String STORAGE_PATH = "storage.path";
	public static final String USERS 		= "users";
	public static final String PASSWORDS 	= "passwords";
	public static final String IMPORT_FORMAT = "storage.import.format";
	
	private static File file = new File(System.getProperty("user.home"), ".phobox.props");
	private static Properties props = init();
	
	public static void unset(String key) {
		props.remove(key);
		save();
	}
	
	
	public static String get(String key) {
		return props.getProperty(key);
	}
	
	public static void set(String key, String value) {
		props.setProperty(key, value);
		save();
	}


	private static void save() {
		try(OutputStream os = new FileOutputStream(file)) {
			props.store(os, "Preferences for the Phobox image application");
			
		} catch (Exception e) {
			log.error("Error while saving properties");
		}
	}
	
	public static Map<String, String> getUserMap() {
		Map<String, String> userPassMap = new TreeMap<>();
		
		String users = get(USERS);
		String passwords = get(PASSWORDS);
		
		if(users != null && passwords != null) {
			String[] userArray = users.replace(", ", ",").split(",");
			String[] passArray = passwords.replace(", ", ",").split(",");
			
			for(int i=0; i<userArray.length; i++) {
				userPassMap.put(userArray[i], passArray[i]);
			}
		}
		
		return userPassMap;
	}
	
	private static Properties init() {
		Properties props = new Properties();
		
		try(InputStream is = new FileInputStream(file)) {
			props.load(is);
		} catch (Exception e) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				log.error("Could not create properties files", e);
			}
		}
		return props;
	}

	public static void addUser(String username, String password) {
		if(username == null && password == null)
			return;

		String users = get(USERS);
		String passwords = get(USERS);
		
		Map<String, String> userMap = getUserMap();
		
		if(userMap.containsKey(username))
			return;
		
		if(users == null) {
			users = username;
		} else {
			users += "," + username;
		}

		if(passwords == null) {
			passwords = MD5Helper.getMD5(password);
		} else {
			passwords += "," + MD5Helper.getMD5(password);
		}
		
		set(USERS, users);
		set(PASSWORDS, passwords);
	}
}
