package de.milchreis.phobox.core.model;

public class SystemStatus extends Status {
	
	protected String importStatus;
	protected String state;
	protected String file;
	protected double freespace;
	protected double maxspace;
	protected int remainingfiles;
	
	public String getStatus() {
		return status;
	}

	public String getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(String importStatus) {
		this.importStatus = importStatus;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public double getFreespace() {
		return freespace;
	}

	public void setFreespace(Double freespace) {
		this.freespace = freespace;
	}

	public double getMaxspace() {
		return maxspace;
	}

	public void setMaxspace(double maxspace) {
		this.maxspace = maxspace;
	}

	public int getRemainingfiles() {
		return remainingfiles;
	}

	public void setRemainingfiles(Integer remainingfiles) {
		this.remainingfiles = remainingfiles;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
