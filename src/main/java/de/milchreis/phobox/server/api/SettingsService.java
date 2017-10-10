package de.milchreis.phobox.server.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.utils.ImportFormatter;


@Path("/settings/")
public class SettingsService {
	
	@POST
	@Path("credentials/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Status setUserCrendentials(UserCredentials cred) {
		
		// Remove existing users
		PreferencesManager.unset(PreferencesManager.USERS);
		PreferencesManager.unset(PreferencesManager.PASSWORDS);
		
		// Set up the new user
		PreferencesManager.addUser(cred.getUsername(), cred.getPassword());
		
		return new Status(Status.OK);
	}
	
	@DELETE
	@Path("credentials/")
	@Produces(MediaType.APPLICATION_JSON)
	public Status unsetUserCrendentials() {
		
		// Remove existing users
		PreferencesManager.unset(PreferencesManager.USERS);
		PreferencesManager.unset(PreferencesManager.PASSWORDS);
		
		return new Status(Status.OK);
	}
	
	@GET
	@Path("credentials")
	@Produces(MediaType.APPLICATION_JSON)
	public UserCredentials getUserCrendentials() {

		List<UserCredentials> list = new ArrayList<>();
		Map<String, String> userMap = PreferencesManager.getUserMap();
		
		if(userMap.size() > 0) {
			userMap.entrySet().forEach(e -> list.add(new UserCredentials(e.getKey(), e.getValue())));
			return list.get(0);
		
		} else { 
			return null;
		}
	}
	
	@GET
	@Path("importPattern")
	public String getImportPattern() {
		PhoboxModel model = Phobox.getModel();
		return model.getImportFormat();
	}
	
	@POST
	@Path("importPattern/")
	@Produces(MediaType.APPLICATION_JSON)
	public Status setImportPattern(String pattern) {
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
	
	@POST
	@Path("path/")
	@Produces(MediaType.APPLICATION_JSON)
	public Status setStoragePath(String path) {
		try {
			PhoboxModel model = Phobox.getModel();
			model.setStoragePath(path);
			return new Status(Status.OK);
		} catch(Exception e) {
			return new Status(Status.ERROR);
		}
	}
}
