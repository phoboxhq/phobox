package de.milchreis.phobox.server.api;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.actions.SyncAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;


@Path("/storage")
public class StorageService {

	@GET
	@Path("backup/{dir}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status backup( @PathParam("dir") String directory ) {

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

	@GET
	@Path("reimport")
	@Produces(MediaType.APPLICATION_JSON)
	public Status reimort() {
		
		Status resp = new Status();
		
		if(Phobox.getImportProcessor().isActive()) {
			resp.setStatus(Status.IS_RUNNING);
			
		} else {
			new ImportScheduler(1).run();
			resp.setStatus(Status.OK);
		}
		
		return resp;
	}
	
	@GET
	@Path("rethumb")
	@Produces(MediaType.APPLICATION_JSON)
	public Status rethumb() {
		
		Status resp = new Status();
		PhoboxModel model = Phobox.getModel();
		
		new StorageScanScheduler(StorageScanScheduler.IMMEDIATELY, new File(model.getStoragePath()), true).start();
		
		resp.setStatus(Status.OK);
		return resp;
	}
	
	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public Status status() {
		return Phobox.getOperations().getStatus();
	}
}
