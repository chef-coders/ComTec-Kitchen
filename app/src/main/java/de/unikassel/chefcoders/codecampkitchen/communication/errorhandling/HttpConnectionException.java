package de.unikassel.chefcoders.codecampkitchen.communication.errorhandling;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpConnectionException extends RuntimeException
{
	// =============== Fields ===============

	private int    responseCode;
	private String responseString;
	private String requestBodyString;
	private String method;
	private String requestUrl;

	private String simpleMessage;

	// =============== Constructors ===============

	public HttpConnectionException(String requestUrl, String method, int responseCode, String requestBodyString,
		String responseString)
	{
		this.requestUrl = requestUrl;
		this.method = method;
		this.responseCode = responseCode;
		this.requestBodyString = requestBodyString;
		this.responseString = responseString;
	}

	public HttpConnectionException(String message)
	{
		this.simpleMessage = message;
	}

	// =============== Methods ===============

	public String fullErrorMessage()
	{
		return "\nError: Responsecode was " + this.responseCode + ", trying to execute Request\n" + "\tof Type \""
		       + this.method + "\"\n" + "\ton URL \"" + this.requestUrl + "\"\n" + "\twith Body \""
		       + this.requestBodyString + "\"\n" + "\twith Response \"" + this.responseString + "\"\n";
	}

	public String smallErrorMessage()
	{
		if (this.simpleMessage != null)
		{
			return this.simpleMessage;
		}

		String innerMessage = "";
		try
		{
			final JsonObject jsonObject = (JsonObject) new JsonParser().parse(this.responseString);
			final JsonObject outerMessage = (JsonObject) jsonObject.get("message");

			innerMessage = outerMessage.get("message").getAsString();
		}
		catch (Exception ignored)
		{
			// we don't want an "error occurred while displaying previous error", so just catch-all
		}

		return this.simpleMessage = innerMessage;
	}
}
