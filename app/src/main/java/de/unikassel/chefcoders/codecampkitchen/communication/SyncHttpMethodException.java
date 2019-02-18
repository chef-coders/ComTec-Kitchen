package de.unikassel.chefcoders.codecampkitchen.communication;

public class SyncHttpMethodException extends Exception {
	private Exception innerException;

	public SyncHttpMethodException(Exception exception) {
		this.innerException = exception;
	}

	public Exception getInnerException() {
		return innerException;
	}
}
