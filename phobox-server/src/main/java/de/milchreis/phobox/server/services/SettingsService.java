package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.config.StorageConfiguration;
import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.exceptions.InvalidFormatException;
import de.milchreis.phobox.utils.image.ImportFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SettingsService implements ISettingsService {


    @Override
    public void setUserCrendentials(UserCredentials userCredentials) throws IOException {

        // Remove existing users
        unsetUserCrendentials();

        // Set up the new user
        StorageConfiguration config = Phobox.getModel().getStorageConfiguration();

        if(config.getLoginCredentials() == null) {
            config.setLoginCredentials(new ArrayList<>());
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

        config.getLoginCredentials().add(userCredentials);
        Phobox.getModel().writeStorageConfig();
    }

    @Override
    public UserCredentials getUserCrendentials() {

        StorageConfiguration config = Phobox.getModel().getStorageConfiguration();

        if (config.getLoginCredentials() != null && config.getLoginCredentials().size() > 0) {
            return config.getLoginCredentials().get(0);

        } else {
            return null;
        }
    }

    @Override
    public void unsetUserCrendentials() throws IOException {
        // Remove existing users
        StorageConfiguration config = Phobox.getModel().getStorageConfiguration();

        if(config.getLoginCredentials() != null) {
            config.getLoginCredentials().clear();
            Phobox.getModel().writeStorageConfig();
        }
    }

    @Override
    public String getImportPattern() {
        StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
        return config.getImportFormat();
    }

    @Override
    public void setImportPattern(String pattern) throws InvalidFormatException, IOException {

        ImportFormatter importFormatter = new ImportFormatter(pattern);

        if(importFormatter.isValid()) {
            StorageConfiguration config = Phobox.getModel().getStorageConfiguration();
            config.setImportFormat(pattern);

            Phobox.getModel().writeStorageConfig();

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
        PreferencesManager.setStoragePath(path);
    }
}
