package de.unikassel.chefcoders.codecampkitchen.communication;

public class SyncHttpMethodException extends RuntimeException
{
	public SyncHttpMethodException(int statusCode, String errorString, Throwable cause)
	{
		super(statusCode + ": " + errorString, cause);
	}
}
