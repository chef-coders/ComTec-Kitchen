package de.unikassel.chefcoders.codecampkitchen.communication.errorhandling;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class HttpConnectionException extends RuntimeException
{
	private int    responseCode;
	private String responseString;
	private String requestBodyString;
	private String method;
	private String requestUrl;

	private String simpleMessage;

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

	public String fullErrorMessage()
	{
		StringBuilder errorMessageBuilder = new StringBuilder().append(
			"\nError: Responsecode was " + this.responseCode + ", trying to execute Request\n")
		                                                       .append("\tof Type \"" + this.method + "\"\n")
		                                                       .append("\ton URL \"" + this.requestUrl + "\"\n")
		                                                       .append("\twith Body \"" + this.requestBodyString
		                                                               + "\"\n")
		                                                       .append("\twith Response \"" + this.responseString
		                                                               + "\"\n");

		return errorMessageBuilder.toString();
	}

	public String smallErrorMessage()
	{
		String innerMessage = "";
		try
		{
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(this.responseString);

			JsonObject outerMessage = (JsonObject) jsonObject.get("message");
			innerMessage = outerMessage.get("message").getAsString();
		}
		catch (JsonSyntaxException e)
		{

		}

		this.simpleMessage = innerMessage;

		return this.simpleMessage;
	}
}
