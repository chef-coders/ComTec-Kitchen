package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.Map;

public class SyncHttpConnection implements HttpConnection
{
	// =============== Constants ===============

	private static final String BASE_URL = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

	// =============== Fields ===============

	private final SyncHttpClient          client;
	private       String                  lastResult;
	private       JsonHttpResponseHandler responseHandler;

	// =============== Constructors ===============

	public SyncHttpConnection()
	{
		this.client = new SyncHttpClient();

		this.responseHandler = new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				SyncHttpConnection.this.lastResult = response.toString();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse)
			{
				SyncHttpConnection.this.lastResult = null;
			}
		};
	}

	// =============== Methods ===============

	@Override
	public String get(String relativeUrl) throws SyncHttpMethodException
	{
		try
		{
			this.client.get(createUrl(relativeUrl), null, this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.Get);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.Get);
		}
		return this.lastResult;
	}

	@Override
	public String post(String relativeUrl, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException
	{
		try
		{
			this.client.post(createUrl(relativeUrl), createParams(headers), this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.Post);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.Get);
		}
		return this.lastResult;
	}

	@Override
	public String put(String relativeUrl, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException
	{
		try
		{
			this.client.put(createUrl(relativeUrl), createParams(headers), this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.Put);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.Get);
		}
		return this.lastResult;
	}

	@Override
	public String delete(String relativeUrl) throws SyncHttpMethodException
	{
		try
		{
			this.client.delete(createUrl(relativeUrl), null, this.responseHandler);
		}
		catch (Exception ex)
		{
			throw new SyncHttpMethodException(ex, HttpMethod.Delete);
		}
		if (this.lastResult == null)
		{
			throw new SyncHttpMethodException(null, HttpMethod.Get);
		}
		return this.lastResult;
	}

	// =============== Static Methods ===============

	// --------------- Helper Methods ---------------

	private static String createUrl(String relativeUrl)
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
