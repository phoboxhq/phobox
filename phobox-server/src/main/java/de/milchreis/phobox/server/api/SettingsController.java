package de.milchreis.phobox.server.api;

import java.io.File;

import de.milchreis.phobox.exceptions.InvalidFormatException;
import de.milchreis.phobox.server.services.ISettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.UserCredentials;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

	@Autowired private ISettingsService settingsService;

	@RequestMapping(value = "credentials", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setUserCrendentials(@RequestBody UserCredentials cred) {
		settingsService.setUserCrendentials(cred);
		return new Status(Status.OK);
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status unsetUserCrendentials() {
		settingsService.unsetUserCrendentials();
		return new Status(Status.OK);
	}
	
	@RequestMapping(value = "credentials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UserCredentials getUserCrendentials() {
		// TODO: Security Issue
		return settingsService.getUserCrendentials();
	}
	
	@RequestMapping(value = "importPattern", method = RequestMethod.GET)
	public String getImportPattern() {
		return settingsService.getImportPattern();
	}
	
	@RequestMapping(value = "importPattern", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setImportPattern(@RequestBody String pattern) {
		try {
			settingsService.setImportPattern(pattern);
			return new Status(Status.OK);
		} catch (InvalidFormatException e) {
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "path", method = RequestMethod.GET)
	public String getStoragePath() {
		return settingsService.getStoragePath();
	}
	
	@RequestMapping(value = "path", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status setStoragePath(@RequestBody String path) {
		try {
			settingsService.setStoragePath(new File(path));
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}
}
