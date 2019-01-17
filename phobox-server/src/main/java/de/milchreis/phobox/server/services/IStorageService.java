package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.SystemStatus;
import de.milchreis.phobox.core.schedules.Schedulable;

import java.io.File;
import java.io.FileNotFoundException;

public interface IStorageService {

    Thread backupDirectory(File targetPath) throws FileNotFoundException;

    Schedulable reimportIncomingFiles();

    Schedulable startThumbnailing();

    SystemStatus getSystemStatus();
}
