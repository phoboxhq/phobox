package de.milchreis.phobox.server.services;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.actions.SyncAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.SystemStatus;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.Schedulable;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.storage.SpaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
@Service
public class StorageService implements IStorageService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Thread backupDirectory(File targetPath) throws FileNotFoundException {

        if(!targetPath.isDirectory()) {
            throw new FileNotFoundException("Directory not found");
        }

        PhoboxModel model = Phobox.getModel();

        Thread thread = new Thread(() -> {
            File storage = new File(model.getStoragePath());
            new FileProcessor().foreachFile(
                    storage,
                    null,
                    new SyncAction(
                            storage,
                            model.getBackupPath()),
                    true);
        });

        thread.start();

        return thread;
    }

    @Override
    public Schedulable reimportIncomingFiles() {
        Schedulable schedulable = new ImportScheduler(1, 0);
        schedulable.start();
        return schedulable;
    }

    @Override
    public Schedulable startThumbnailing() {

        Schedulable schedulable = new StorageScanScheduler(
                StorageScanScheduler.IMMEDIATELY,
                new File(Phobox.getModel().getStoragePath()),
                itemRepository, true);

        schedulable.start();
        return schedulable;
    }

    @Override
    public SystemStatus getSystemStatus() {
        String storagePath = Phobox.getModel().getStoragePath();
        FileProcessor fileProcessor = Phobox.getImportProcessor();

        SystemStatus status = new SystemStatus();
        status.setImportStatus(fileProcessor.getStatus());
        status.setState(fileProcessor.getState());
        status.setFile(FilenameUtils.getName(fileProcessor.getCurrentfile()));
        status.setFreespace(SpaceInfo.getFreeSpaceMB(storagePath));
        status.setMaxspace(SpaceInfo.getMaxSpaceMB(storagePath));
        status.setRemainingfiles(Phobox.getOperations().getRemainingFiles().size());
        status.setNumberOfPictures(itemRepository.count());
        return status;
    }
}
