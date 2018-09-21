package de.milchreis.phobox.server.api;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.actions.SyncAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.SystemStatus;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.utils.SpaceInfo;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
	
	@Autowired private ItemRepository itemRepository;

	@RequestMapping(value = "backup/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status backup( @PathVariable("dir") String directory ) {

		Status response = new Status();
		PhoboxModel model = Phobox.getModel();
		File target = new File(directory);
		
		if(!target.isDirectory()) {
			response.setStatus(Status.DIRECTORY_NOT_FOUND);			
		} else {
			new Thread(() -> {
				File storage = new File(model.getStoragePath());
				new FileProcessor().foreachFile(
						storage,
						null, 
						new SyncAction(
								storage, 
								model.getBackupPath()),
						true);	
			}).start();

			response.setStatus(Status.OK);
		}
		
		return response;
	}

	@RequestMapping(value = "reimport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status reimport() {
		
		Status resp = new Status();
		
		if(Phobox.getImportProcessor().isActive()) {
			resp.setStatus(Status.IS_RUNNING);
			
		} else {
			new ImportScheduler(1).run();
			resp.setStatus(Status.OK);
		}
		
		return resp;
	}
	
	@RequestMapping(value = "rethumb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rethumb() {
		
		Status resp = new Status();
		PhoboxModel model = Phobox.getModel();
		
		new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, new File(model.getStoragePath()), itemRepository, true).start();
		
		resp.setStatus(Status.OK);
		return resp;
	}
	
	@RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status status() {
		PhoboxModel model = Phobox.getModel();
		PhoboxOperations operations = Phobox.getOperations();
		
		FileProcessor fileProcessor = Phobox.getImportProcessor();
		SystemStatus status = new SystemStatus();
		status.setImportStatus(fileProcessor.getStatus());
		status.setState(fileProcessor.getState());
		status.setFile(FilenameUtils.getName(fileProcessor.getCurrentfile()));
		status.setFreespace(SpaceInfo.getFreeSpaceMB(model.getStoragePath()));
		status.setMaxspace(SpaceInfo.getMaxSpaceMB(model.getStoragePath()));
		status.setRemainingfiles(operations.getRemainingFiles().size());
		status.setNumberOfPictures(itemRepository.count());
		return status;
	}
}
