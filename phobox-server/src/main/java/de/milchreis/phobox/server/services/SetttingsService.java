package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.exceptions.InvalidFormatException;
import de.milchreis.phobox.utils.ImportFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SetttingsService implements ISettingsService {


    @Override
    public void setUserCrendentials(UserCredentials userCredentials) {

        // Remove existing users
        PreferencesManager.unset(PreferencesManager.USERS);
        PreferencesManager.unset(PreferencesManager.PASSWORDS);

        // Set up the new user
        PreferencesManager.addUser(userCredentials.getUsername(), userCredentials.getPassword());
    }

    @Override
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

    @Override
    public void unsetUserCrendentials() {
        // Remove existing users
        PreferencesManager.unset(PreferencesManager.USERS);
        PreferencesManager.unset(PreferencesManager.PASSWORDS);
    }

    @Override
    public String getImportPattern() {
        return Phobox.getModel().getImportFormat();
    }

    @Override
    public void setImportPattern(String pattern) throws InvalidFormatException {

        ImportFormatter importFormatter = new ImportFormatter(pattern);

        if(importFormatter.isValid()) {
            Phobox.getModel().setImportFormat(pattern);
            PreferencesManager.set(PreferencesManager.IMPORT_FORMAT, pattern);
        } else {
            throw new InvalidFormatException("The given format '" + pattern + "' is not valid");
        }
    }

    @Override
    public String getStoragePath() {
        return Phobox.getModel().getStoragePath();
    }

    @Override
    public void setStoragePath(File path) {
        Phobox.changeStoragePath(path);
    }
}
