package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.file.filter.DirectoryFilter;
import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.FilesystemHelper;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;
import de.milchreis.phobox.utils.ZipStreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class PhotosService implements IPhotosService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private IPhotoService photoService;

    @Override
    public StorageStatus getStorageItemsByDirectory(File dir, Pageable pageable) {

        PhoboxOperations ops = Phobox.getOperations();

        String directory = ops.getWebPath(dir);
        boolean isRoot = directory.equals("/");

        if(directory == null || directory.isEmpty())
            dir = new File(Phobox.getModel().getStoragePath());

        // Update directory on database if it is no fragment
        if(pageable == null || pageable.getPageNumber() == 0) {
            Phobox.addPathToScanQueue(dir);
        }

        StorageStatus response = new StorageStatus();
        response.setName(ops.getElementName(dir));
        response.setPath(directory);

        if(dir.isDirectory()) {
            response.setType(StorageStatus.TYPE_DIRECTORY);
        } else {
            response.setType(StorageStatus.TYPE_FILE);
        }

        // Find including directories (but add it not by fragment scan)
        if(pageable == null || pageable.getPageNumber() == 0) {
            DirectoryStream<Path> stream = null;
            try {
                stream = Files.newDirectoryStream(dir.toPath(), new DirectoryFilter());
                for(java.nio.file.Path path : stream) {
                    File file = path.toFile();

                    // Skip internal phobox directory and hidden files
                    if((isRoot && file.isDirectory() && file.getName().equals("phobox")) || file.isHidden()) {
                        continue;
                    }

                    // select supported formats and directories only
                    if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxDefinitions.SUPPORTED_VIEW_FORMATS) || file.isDirectory()) {
                        response.add(photoService.getItem(file));
                    }
                }

            } catch(IOException e) {
                log.warn("Error while scanning directory: " + e.getLocalizedMessage());
            }
        }

        // Scan files from database
        List<Item> items;
        if(pageable != null) {
            items = itemRepository.findByPath(directory+"/", pageable).getContent();
            response.setFragment(items.size() > 0);
        } else {
            items = itemRepository.findByPath(directory+"/");
        }

        for(Item item : items) {
            File file = ops.getPhysicalFile(item.getFullPath());
            response.add(photoService.getItem(file));
        }

        try {
            if(items.size() == 0 && !FilesystemHelper.isDirEmpty(dir.toPath())) {
                response.setProcessing(true);
            }
        } catch (IOException e) {
            log.error("", e);
        }

        return response;
    }

    @Override
    public File convertItemPathToFileObject(String itemPath) {
        return new File(Phobox.getModel().getStoragePath(), PathConverter.decode(itemPath));
    }

    @Override
    public void downloadDirectoryAsZip(File directory, HttpServletResponse response) throws IOException {

        if(!directory.isDirectory()) {
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + directory.getName() + ".zip\"");
        response.setContentType("txt/plain");

        ZipStreamHelper zip = new ZipStreamHelper();
        zip.compressToStream(directory, response.getOutputStream());
    }
}
