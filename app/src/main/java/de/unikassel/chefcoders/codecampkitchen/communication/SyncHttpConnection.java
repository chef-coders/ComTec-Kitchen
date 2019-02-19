package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
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
	private       String                   lastResult;
	private       ResponseHandlerInterface responseHandler;

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
				SyncHttpConnection.this.lastResult = null;
			}
		};
	}

	// =============== Methods ===============

	@Override
	public String get(String relativeUrl)
	{
		try
		{
			this.client.get(createURL(relativeUrl), null, this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.GET);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.GET);
		}
		return this.lastResult;
	}

	@Override
	public String post(String relativeUrl, String jsonBody, Map<String, String> headers)
	{
		try
		{
			this.client.post(createURL(relativeUrl), createParams(headers), this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.POST);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.POST);
		}
		return this.lastResult;
	}

	@Override
	public String put(String relativeUrl, String jsonBody, Map<String, String> headers)
	{
		try
		{
			this.client.put(createURL(relativeUrl), createParams(headers), this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.PUT);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.PUT);
		}
		return this.lastResult;
	}

	@Override
	public String delete(String relativeUrl)
	{
		try
		{
			this.client.delete(createURL(relativeUrl), this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.DELETE);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.DELETE);
		}
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
