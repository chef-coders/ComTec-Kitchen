package de.unikassel.chefcoders.codecampkitchen.communication.errorhandling;

public class HttpExceptionBuilder
{
	private int errorCode;
	private String responseString;
	private String requestBodyString;
	private String method;
	private String requestUrl;

	public HttpExceptionBuilder withErrorCode(int errorCode)
	{
		this.errorCode = errorCode;

		return this;
	}

	public HttpExceptionBuilder withResponseString(String responseString)
	{
		this.responseString = responseString;

		return this;
	}

	public HttpExceptionBuilder withRequestBodyString(String requestString)
	{
		this.requestBodyString = requestString;

		return this;
	}

	public HttpExceptionBuilder withRequestMethod(String method)
	{
		this.method = method;

		return this;
	}

	public HttpExceptionBuilder withRequestUrl(String url)
	{
		this.requestUrl = url;

		return this;
	}

	public HttpConnectionException build()
	{
		return new HttpConnectionException(this.requestUrl, this.method, this.errorCode, this.requestBodyString, this.responseString);
	}
}
