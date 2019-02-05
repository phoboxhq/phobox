package de.milchreis.phobox.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Status {
	
	public static final String OK = "OK";
	public static final String IS_RUNNING = "IS_RUNNING";
	public static final String DIRECTORY_NOT_FOUND = "DIRECTORY NOT FOUND";
	public static final String ERROR = "ERROR";

	@Getter @Setter
	protected String status;
	@Getter @Setter
	protected String message;

	public Status(String status) {
		this.status = status;
	}

	public Status(String status, String message) {
		this.status = status;
		this.message = message;
	}

}
