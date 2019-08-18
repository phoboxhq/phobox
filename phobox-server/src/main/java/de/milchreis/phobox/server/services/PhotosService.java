package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.core.file.filter.DirectoryFilter;
import de.milchreis.phobox.core.model.StorageItem;
import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.storage.FilesystemHelper;
import de.milchreis.phobox.utils.storage.PathConverter;
import de.milchreis.phobox.utils.storage.ZipStreamHelper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class PhotosService implements IPhotosService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private IPhotoService photoService;

    @Override
    public StorageStatus getStorageItemsByDirectory(File dir, Pageable pageable) {

        PhoboxOperations ops = Phobox.getOperations();

        String webPath = ops.getWebPath(dir);
        boolean isRoot = webPath.equals("/");

        if(webPath == null || webPath.isEmpty())
            dir = new File(Phobox.getModel().getStoragePath());

        // Update directory on database if it is no fragment
        if(pageable == null || pageable.getPageNumber() == 0) {
            Phobox.addPathToScanQueue(dir);
        }

        StorageStatus response = new StorageStatus(
                ops.getElementName(dir),
                webPath,
                dir.isDirectory() ? StorageStatus.TYPE_DIRECTORY : StorageStatus.TYPE_FILE);

        // Find including directories (but add it not by fragment scan)
        if(pageable == null || pageable.getPageNumber() == 0) {
            List<StorageItem> directories = getDirectories(dir, isRoot);
            response.addAll(directories);
        }

        // Scan files from database
        log.debug("Scanning database for directory: " + dir.getAbsolutePath());
        List<Item> items;
        String queryPath = webPath.equals("/") ? webPath : webPath+"/";
        if(pageable != null) {
            items = itemRepository.findByPath(queryPath, pageable).getContent();
            response.setFragment(items.size() > 0);
        } else {
            items = itemRepository.findByPath(queryPath);
        }

        log.debug("Creating item objects: " + dir.getAbsolutePath());
        items.forEach(item -> response.add(photoService.getItem(item)));

        try {
            if(items.size() == 0 && !FilesystemHelper.isDirEmpty(dir.toPath())) {
                response.setProcessing(true);
            }
        } catch (IOException e) {
            log.error("", e);
        }

        log.debug("Response is ready: " + dir.getAbsolutePath());
        return response;
    }

    @Override
    public List<StorageItem> getDirectories(File dir, boolean isRoot) {
        List<StorageItem> items = new ArrayList<>();

        try {
            log.debug("Scanning directory: " + dir.getAbsolutePath());
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir.toPath(), new DirectoryFilter());
            for(java.nio.file.Path path : stream) {
                File file = path.toFile();

                // Skip internal phobox directory and hidden files
                if((isRoot && file.isDirectory() && file.getName().equals("phobox")) || file.isHidden()) {
                    continue;
                }

                items.add(photoService.getItem(file));
            }

        } catch(IOException e) {
            log.warn("Error while scanning directory: " + e.getLocalizedMessage());
        }

        items.sort(Comparator.comparing(StorageItem::getName));

        return  items;
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
