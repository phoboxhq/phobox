package de.milchreis.phobox.core.model;

public class Status {
	
	public static final String OK = "OK";
	public static final String IS_RUNNING = "IS_RUNNING";
	public static final String DIRECTORY_NOT_FOUND = "DIRECTORY NOT FOUND";
	public static final String ERROR = "ERROR";
	
	protected String status;
	
	public Status() {}
	
	public Status(String status) {
		setStatus(status);
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
