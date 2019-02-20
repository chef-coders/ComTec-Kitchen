package de.unikassel.chefcoders.codecampkitchen.communication;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttpConnection implements HttpConnection
{
	@Override
	public String get(String url, Map<String, String> headers)
	{
		try
		{
			Request request = createRequest(url, headers);

			Call call = createCallFromRequest(request);

			Response response = tryExecuteCall(call);

			return response.body().string();
		} catch (IOException e)
		{
			throw new HttpConnectionException();
		}
	}

	private Call createCallFromRequest(Request request)
	{
		OkHttpClient client = new OkHttpClient();

		return client.newCall(request);
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
		} catch (IOException e)
		{
			throw new HttpConnectionException();
		}
		return response;
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
		} catch (IOException e)
		{
			throw new HttpConnectionException();
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
		} catch (IOException e)
		{
			throw new HttpConnectionException();
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
		} catch (IOException e)
		{
			throw new HttpConnectionException();
		}
	}

	private Request createRequestDelete(String url, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder()
				.url(url);

		setHeadersOnBuilder(headers, builder);

		builder.delete();

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
