package de.unikassel.chefcoders.codecampkitchen.communication;

public class SyncHttpMethodException extends Exception {
	private Exception innerException;
	private HttpMethod httpMethod;

	public SyncHttpMethodException(Exception exception, HttpMethod httpMethod) {
		this.innerException = exception;
		this.httpMethod = httpMethod;
	}

	public Exception getInnerException() {
		return innerException;
	}

	public HttpMethod getHttpMethod() { return httpMethod; }
}
