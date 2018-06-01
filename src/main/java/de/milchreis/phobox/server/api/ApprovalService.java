package de.milchreis.phobox.server.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxConfigs;
import de.milchreis.phobox.core.PhoboxOperations;
import de.milchreis.phobox.core.file.filter.DirectoryFilter;
import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.core.model.StorageStatus;
import de.milchreis.phobox.utils.ListHelper;
import de.milchreis.phobox.utils.PathConverter;


@Path("/approval/")
public class ApprovalService {
	private static Logger log = Logger.getLogger(ApprovalService.class);
	
	@POST
	@Path("accept/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status accept(@PathParam("path") String filename) {
		
		filename = PathConverter.decode(filename);
		File file = new File(Phobox.getModel().getApprovalPath(), filename);
		
		Status status = new Status();
		
		try {
			FileUtils.moveFileToDirectory(file, Phobox.getModel().getIncomingPath(), false);
			status.setStatus(Status.OK);

		} catch (IOException e) {
			log.error("Error while approval -> accept", e);
			status.setStatus(Status.ERROR);
		}

		return status;
	}
	
	@POST
	@Path("decline/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status decline(@PathParam("path") String filename) {
		
		filename = PathConverter.decode(filename);
		File file = new File(Phobox.getModel().getApprovalPath(), filename);
		
		Status status = new Status();
		
		try {
			file.delete();
			status.setStatus(Status.OK);

		} catch (Exception e) {
			log.error("Error while approval -> decline", e);
			status.setStatus(Status.ERROR);
		}

		return status;
	}
	
	@GET
	@Path("scan")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> scanDirectory() {

		PhoboxOperations ops = Phobox.getOperations();
		
		File directory = Phobox.getModel().getApprovalPath();
		
		List<String> files = new ArrayList<>();
		
		DirectoryStream<java.nio.file.Path> stream = null;
		try {
			stream = Files.newDirectoryStream(directory.toPath());
			for(java.nio.file.Path path : stream) {
				File file = path.toFile();
				
				// select supported formats and directories only
				if(file.isFile() && ListHelper.endsWith(file.getName(), PhoboxConfigs.SUPPORTED_VIEW_FORMATS)) {
					files.add(ops.getWebPath(file.getPath()));
				}
			}
		} catch(IOException e) {
			log.warn("Error while scanning directory: " + e.getLocalizedMessage());
		}
		
		return files;
	}
	
}