package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.exceptions.InvalidFormatException;

import java.io.File;

public interface ISettingsService {

    void setUserCrendentials(UserCredentials userCredentials);

    UserCredentials getUserCrendentials();

    void unsetUserCrendentials();

    String getImportPattern();

    void setImportPattern(String pattern) throws InvalidFormatException;

    String getStoragePath();

    void setStoragePath(File path);

}
