package de.unikassel.chefcoders.codecampkitchen.communication.errorhandling;

public class HttpConnectionException extends RuntimeException
{
	public HttpConnectionException(String message)
	{
		super(message);
	}
}
