package de.unikassel.chefcoders.codecampkitchen.communication;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class HttpRequestBuilder
{
	public static Request createGetRequestFor(String url, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder().url(url);

		setHeadersOnBuilder(headers, builder);

		builder.get();

		return builder.build();
	}

	public static Request createPostRequestFor(String url, String body, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder().url(url);

		setHeadersOnBuilder(headers, builder);

		builder.post(RequestBody.create(MediaType.parse(headers.get("Content-Type")), body));

		return builder.build();
	}

	public static Request createPutRequestFor(String url, String body, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder().url(url);

		setHeadersOnBuilder(headers, builder);

		builder.put(RequestBody.create(MediaType.parse(headers.get("Content-Type")), body));

		return builder.build();
	}

	public static Request createDeleteRequestFor(String url, Map<String, String> headers)
	{
		Request.Builder builder = new Request.Builder().url(url);

		setHeadersOnBuilder(headers, builder);

		builder.delete(RequestBody.create(MediaType.parse(headers.get("Content-Type")), "{}"));

		return builder.build();
	}

	private static void setHeadersOnBuilder(Map<String, String> headers, Request.Builder builder)
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
