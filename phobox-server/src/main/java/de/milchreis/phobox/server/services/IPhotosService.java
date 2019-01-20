package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.model.StorageStatus;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public interface IPhotosService {

    StorageStatus getStorageItemsByDirectory(File directory, Pageable pageable);

    File convertItemPathToFileObject(String itemPath);

    void downloadDirectoryAsZip(File directory, HttpServletResponse response) throws IOException;
}
