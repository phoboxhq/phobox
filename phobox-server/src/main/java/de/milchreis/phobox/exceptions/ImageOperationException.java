package de.milchreis.phobox.exceptions;

public class ImageOperationException extends Exception {
	private static final long serialVersionUID = -7830556651453875559L;
	
	public ImageOperationException() {
		super();
	}
	
	public ImageOperationException(String message) {
		super(message);
	}
	
	public ImageOperationException(String message, Throwable nestedException) {
		super(message, nestedException);
	}
	
}
