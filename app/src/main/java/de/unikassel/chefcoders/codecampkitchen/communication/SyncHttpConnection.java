package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.Map;

public class SyncHttpConnection implements HttpConnection
{
	// =============== Constants ===============

	private static final String BASE_URL = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

	// =============== Fields ===============

	private final SyncHttpClient           client;
	private final AsyncHttpResponseHandler responseHandler;

	private String lastResult;

	// =============== Constructors ===============

	public SyncHttpConnection()
	{
		this.client = new SyncHttpClient();

		this.responseHandler = new TextHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, String response)
			{
				SyncHttpConnection.this.lastResult = response;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
			{
				throw new SyncHttpMethodException(statusCode, responseString, throwable);
			}
		};
	}

	// =============== Methods ===============

	@Override
	public String get(String relativeUrl, Map<String, String> headers)
	{
		this.client.get(createURL(relativeUrl), createParams(headers), this.responseHandler);
		return this.lastResult;
	}

	@Override
	public String post(String relativeUrl, String jsonBody, Map<String, String> headers)
	{
		this.client.post(createURL(relativeUrl), createParams(headers), this.responseHandler);
		return this.lastResult;
	}

	@Override
	public String put(String relativeUrl, String jsonBody, Map<String, String> headers)
	{
		this.client.put(createURL(relativeUrl), createParams(headers), this.responseHandler);
		return this.lastResult;
	}

	@Override
	public String delete(String relativeUrl, Map<String, String> headers)
	{
		this.client.delete(createURL(relativeUrl), createParams(headers), this.responseHandler);
		return this.lastResult;
	}

	// =============== Static Methods ===============

	// --------------- Helper Methods ---------------

	private static String createURL(String relativeUrl)
	{
		return BASE_URL + relativeUrl;
	}

	private static RequestParams createParams(Map<String, String> headers)
	{
		final RequestParams parameters = new RequestParams();
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			parameters.add(entry.getKey(), entry.getValue());
		}
		return parameters;
	}
}
