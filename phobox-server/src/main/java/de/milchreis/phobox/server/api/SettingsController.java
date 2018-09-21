package de.milchreis.phobox.server.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.utils.ImportFormatter;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
	
	@RequestMapping(value = "credentials", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setUserCrendentials(@RequestBody UserCredentials cred) {
		
		// Remove existing users
		PreferencesManager.unset(PreferencesManager.USERS);
		PreferencesManager.unset(PreferencesManager.PASSWORDS);
		
		// Set up the new user
		PreferencesManager.addUser(cred.getUsername(), cred.getPassword());
		
		return new Status(Status.OK);
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status unsetUserCrendentials() {
		
		// Remove existing users
		PreferencesManager.unset(PreferencesManager.USERS);
		PreferencesManager.unset(PreferencesManager.PASSWORDS);
		
		return new Status(Status.OK);
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserCredentials getUserCrendentials() {
		// TODO: Security Issue
		
		List<UserCredentials> list = new ArrayList<>();
		Map<String, String> userMap = PreferencesManager.getUserMap();
		
		if(userMap.size() > 0) {
			userMap.entrySet().forEach(e -> list.add(new UserCredentials(e.getKey(), e.getValue())));
			return list.get(0);
		
		} else { 
			return null;
		}
	}
	
	@RequestMapping(value = "importPattern", method = RequestMethod.GET)
	public String getImportPattern() {
		PhoboxModel model = Phobox.getModel();
		return model.getImportFormat();
	}
	
	@RequestMapping(value = "importPattern", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setImportPattern(@RequestBody String pattern) {
		
		PhoboxModel model = Phobox.getModel();
		ImportFormatter importFormatter = new ImportFormatter(pattern);
		
		if(importFormatter.isValid()) {
			model.setImportFormat(pattern);
			PreferencesManager.set(PreferencesManager.IMPORT_FORMAT, pattern);
			return new Status(Status.OK);
			
		} else {
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "path", method = RequestMethod.GET)
	public String getStoragePath() {
		PhoboxModel model = Phobox.getModel();
		return model.getStoragePath();
	}
	
	@RequestMapping(value = "path", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setStoragePath(@RequestBody String path) {
		try {
			Phobox.changeStoragePath(new File(path));
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}
}
