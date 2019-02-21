package de.unikassel.chefcoders.codecampkitchen.communication.errorhandling;

import okhttp3.Response;

public class ErrorMessageBuilder
{
	public String requestBody = "NONE";
	public String requestType = "NONE";
	public String requestUrl = "NONE";
	public String requestResponse = "NONE";
	public int responseCode = 0;

	public String buildErrorMessage()
	{
		StringBuilder errorMessageBuilder = new StringBuilder()
				.append("\nError: Responsecode was " + responseCode + ", trying to execute Request\n")
				.append("\tof Type \"" + requestType + "\"\n")
				.append("\ton URL \"" + requestUrl + "\"\n")
				.append("\twith Body \"" + requestBody + "\"\n")
				.append("\twith Response \"" + requestResponse + "\"\n");

		return errorMessageBuilder.toString();
	}
}
