package de.milchreis.phobox.server.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.milchreis.phobox.core.model.Status;
import de.milchreis.phobox.server.services.IApprovalService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/approval")
public class ApprovalController {
	
	@Autowired private IApprovalService approvalService;
	
	@RequestMapping(value = "accept", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status accept(@RequestBody String webFilePath) {
		try {
			approvalService.moveFileToIncoming(webFilePath);
			return new Status(Status.OK);
		} catch (Exception e) {
			log.error("Error while approval -> accept", e);
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "decline", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Status decline(@RequestBody String webFilePath) {
		try {
			approvalService.deleteFile(webFilePath);
			return new Status(Status.OK);
		} catch (Exception e) {
			log.error("Error while approval -> decline", e);
			return new Status(Status.ERROR);
		}
	}
	
	@RequestMapping(value = "scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> scanDirectory() {
		return approvalService.getAllImagePathInApproval();
	}
	
}