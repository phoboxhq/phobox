package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.UserCredentials;
import de.milchreis.phobox.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public interface ISettingsService {

    void setUserCrendentials(UserCredentials userCredentials) throws IOException;

    UserCredentials getUserCrendentials();

    void unsetUserCrendentials() throws IOException;

    String getImportPattern();

    void setImportPattern(String pattern) throws InvalidFormatException, IOException;

    String getStoragePath();

    void setStoragePath(File path);

}
