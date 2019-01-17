package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.FileNotFoundException;

import de.milchreis.phobox.server.services.IStorageService;
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
	
	@Autowired private IStorageService storageService;

	@RequestMapping(value = "backup/{dir}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status backup( @PathVariable("dir") String directory ) {
		try {
			storageService.backupDirectory(new File(directory));
			return new Status(Status.OK);

		} catch (FileNotFoundException e) {
			return new Status(Status.DIRECTORY_NOT_FOUND);
		}
	}

	@RequestMapping(value = "reimport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status reimport() {
		if(Phobox.getImportProcessor().isActive()) {
			return new Status(Status.IS_RUNNING);
		} else {
			storageService.reimportIncomingFiles();
			return new Status(Status.OK);
		}
	}
	
	@RequestMapping(value = "rethumb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status rethumb() {
		storageService.startThumbnailing();
		return new Status(Status.OK);
	}
	
	@RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status status() {
		return storageService.getSystemStatus();
	}
}
