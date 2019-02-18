package de.unikassel.chefcoders.codecampkitchen.communication;

public class SyncHttpMethodException extends Exception
{
	private HttpMethod httpMethod;

	public SyncHttpMethodException(Throwable cause, HttpMethod httpMethod)
	{
		super("", cause);

		this.httpMethod = httpMethod;
	}

	public HttpMethod getHttpMethod()
	{
		return this.httpMethod;
	}
}
