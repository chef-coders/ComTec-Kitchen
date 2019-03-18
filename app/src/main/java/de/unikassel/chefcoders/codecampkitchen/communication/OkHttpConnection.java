package de.unikassel.chefcoders.codecampkitchen.communication;

import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpExceptionBuilder;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Map;

public class OkHttpConnection implements HttpConnection
{
	// =============== Static Fields ===============

	public static final OkHttpConnection shared = new OkHttpConnection();

	// =============== Fields ===============

	private final OkHttpClient         client;
	private final HttpExceptionBuilder httpExceptionBuilder;

	// =============== Constructors ===============

	public OkHttpConnection()
	{
		this.httpExceptionBuilder = new HttpExceptionBuilder();
		this.client = new OkHttpClient();
	}

	// =============== Methods ===============

	// --------------- Requests ---------------

	@Override
	public String get(String url, Map<String, String> headers)
	{
		Request request = HttpRequestFactory.createGetRequestFor(url, headers);

		return this.executeRequestAndReturnResponseString(request);
	}

	@Override
	public String post(String url, String jsonBody, Map<String, String> headers)
	{
		Request request = HttpRequestFactory.createPostRequestFor(url, jsonBody, headers);

		return this.executeRequestAndReturnResponseString(request);
	}

	@Override
	public String put(String url, String jsonBody, Map<String, String> headers)
	{
		Request request = HttpRequestFactory.createPutRequestFor(url, jsonBody, headers);

		return this.executeRequestAndReturnResponseString(request);
	}

	@Override
	public String delete(String url, Map<String, String> headers)
	{
		Request request = HttpRequestFactory.createDeleteRequestFor(url, headers);

		return this.executeRequestAndReturnResponseString(request);
	}

	// --------------- Helpers ---------------

	private String executeRequestAndReturnResponseString(Request request)
	{
		Call call = this.createCallFromRequest(request);

		return this.tryExecuteCall(call);
	}

	private String readBodyFromRequest(Request request)
	{
		try
		{
			Request copy = request.newBuilder().build();
			Buffer buffer = new Buffer();

			RequestBody body = copy.body();
			if (body != null)
			{
				body.writeTo(buffer);
			}

			return buffer.readUtf8();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("Error reading response Body");
		}
	}

	private String tryExecuteCall(Call call)
	{
		Response response;
		try
		{
			response = call.execute();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("ERROR: Server not available");
		}

		String responseString = this.tryGetResponseString(response);
		int responseCode = response.code();

		this.httpExceptionBuilder.withResponseString(responseString);
		this.httpExceptionBuilder.withErrorCode(responseCode);

		if (!this.isValid(responseCode))
		{
			throw this.httpExceptionBuilder.build();
		}

		return responseString;
	}

	private String tryGetResponseString(Response response)
	{
		try
		{
			return response.body().string();
		}
		catch (IOException e)
		{
			return "";
		}
	}

	private boolean isValid(int code)
	{
		int firstDigit = code / 100;
		return firstDigit == 2;
	}

	private Call createCallFromRequest(Request request)
	{
		this.httpExceptionBuilder.withRequestMethod(request.method());
		this.httpExceptionBuilder.withRequestUrl(request.url().toString());
		this.httpExceptionBuilder.withRequestBodyString(this.readBodyFromRequest(request));

		return this.client.newCall(request);
	}
}
