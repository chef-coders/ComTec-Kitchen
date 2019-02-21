package de.unikassel.chefcoders.codecampkitchen.communication;

import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.ErrorMessageBuilder;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Map;

public class OkHttpConnection implements HttpConnection
{
	private OkHttpClient client;
	private ErrorMessageBuilder errorMessageBuilder;

	public OkHttpConnection()
	{
		errorMessageBuilder = new ErrorMessageBuilder();
		client = new OkHttpClient();
	}

	@Override
	public String get(String url, Map<String, String> headers)
	{
		Request request = HttpRequestBuilder.createGetRequestFor(url, headers);

		return executeRequestAndReturnResponseString(request);
	}

	@Override
	public String post(String url, String jsonBody, Map<String, String> headers)
	{
		Request request = HttpRequestBuilder.createPostRequestFor(url, jsonBody, headers);

		return executeRequestAndReturnResponseString(request);
	}

	@Override
	public String put(String url, String jsonBody, Map<String, String> headers)
	{
		Request request = HttpRequestBuilder.createPutRequestFor(url, jsonBody, headers);

		return executeRequestAndReturnResponseString(request);
	}

	@Override
	public String delete(String url, Map<String, String> headers)
	{
		Request request = HttpRequestBuilder.createDeleteRequestFor(url, headers);

		return executeRequestAndReturnResponseString(request);
	}

	private String executeRequestAndReturnResponseString(Request request)
	{
		Call call = createCallFromRequest(request);

		return tryExecuteCall(call);
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
			throw new HttpConnectionException("Error: invalid ULR or Timeout");
		}

		String responseString = tryGetResponseString(response);
		int responseCode = response.code();

		errorMessageBuilder.requestResponse = responseString;
		errorMessageBuilder.responseCode = responseCode;

		if (!isValid(responseCode))
		{
			String errorMessage = errorMessageBuilder.buildErrorMessage();

			throw new HttpConnectionException(errorMessage);
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
		errorMessageBuilder.requestType = request.method();
		errorMessageBuilder.requestUrl = request.url().toString();
		errorMessageBuilder.requestBody = readBodyFromRequest(request);

		return client.newCall(request);
	}
}
