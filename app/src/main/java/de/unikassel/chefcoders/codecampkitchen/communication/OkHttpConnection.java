package de.unikassel.chefcoders.codecampkitchen.communication;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Map;

public class OkHttpConnection implements HttpConnection
{
	private String CURRENT_REQUEST_BODY = "NONE";
	private String CURRENT_REQUEST_TYPE = "NONE";
	private String CURRENT_REQUEST_URL = "NONE";

	private OkHttpClient client;

	public OkHttpConnection()
	{
		client = new OkHttpClient();
	}

	@Override
	public String get(String url, Map<String, String> headers)
	{
		try
		{
			Request request = createRequest(url, headers);

			Call call = createCallFromRequest(request);

			Response response = tryExecuteCall(call);

			return response.body().string();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("Error extracting String from Response in OkHttpConnection::get");
		}
	}

	private Call createCallFromRequest(Request request)
	{
		CURRENT_REQUEST_TYPE = request.method();
		CURRENT_REQUEST_URL = request.url().toString();
		CURRENT_REQUEST_BODY = readBodyFromRequest(request);

		return client.newCall(request);
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

	private Request createRequest(String url, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder()
				.url(url);

		setHeadersOnBuilder(headers, builder);

		builder.get();

		return builder.build();
	}

	private Response tryExecuteCall(Call call)
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

		if (!isValid(response.code()))
		{
			throw new HttpConnectionException("Error: Responsecode was " + response.code() + ", trying to execute Request" +
					" of Type " + CURRENT_REQUEST_TYPE + "\non URL " + CURRENT_REQUEST_URL + "\nwith Body " + CURRENT_REQUEST_BODY);
		}

		return response;
	}

	private boolean isValid(int code)
	{
		int firstDigit = code / 100;
		return firstDigit == 2;
	}

	@Override
	public String post(String url, String jsonBody, Map<String, String> headers)
	{
		try
		{
			Request request = createRequestPost(url, jsonBody, headers);

			Call call = createCallFromRequest(request);

			Response response = tryExecuteCall(call);

			return response.body().string();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("Error extracting String from Response in OkHttpConnection::post");
		}
	}

	private Request createRequestPost(String url, String body, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder()
				.url(url);

		setHeadersOnBuilder(headers, builder);

		builder.post(RequestBody.create(MediaType.parse(headers.get("Content-Type")), body));

		return builder.build();
	}

	@Override
	public String put(String url, String jsonBody, Map<String, String> headers)
	{
		try
		{
			Request request = createRequestPut(url, jsonBody, headers);

			Call call = createCallFromRequest(request);

			Response response = tryExecuteCall(call);

			return response.body().string();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("Error extracting String from Response in OkHttpConnection::put");
		}
	}

	private Request createRequestPut(String url, String body, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder()
				.url(url);

		setHeadersOnBuilder(headers, builder);

		builder.put(RequestBody.create(MediaType.parse(headers.get("Content-Type")), body));

		return builder.build();
	}

	@Override
	public String delete(String url, Map<String, String> headers)
	{
		try
		{
			Request request = createRequestDelete(url, headers);

			Call call = createCallFromRequest(request);

			Response response = tryExecuteCall(call);

			return response.body().string();
		}
		catch (IOException e)
		{
			throw new HttpConnectionException("Error extracting String from Response in OkHttpConnection::delete");
		}
	}

	private Request createRequestDelete(String url, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder()
				.url(url);

		setHeadersOnBuilder(headers, builder);

		builder.delete(RequestBody.create(MediaType.parse(headers.get("Content-Type")), "{}"));

		return builder.build();
	}

	private void setHeadersOnBuilder(Map<String, String> headers, Request.Builder builder)
	{
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			if (entry.getValue() != null)
			{
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

}
