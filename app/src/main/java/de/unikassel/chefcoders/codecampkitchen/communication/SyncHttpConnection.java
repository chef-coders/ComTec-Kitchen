package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.Map;

public class SyncHttpConnection implements HttpConnection
{
	private static final String BASE_URL = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

	private SyncHttpClient client;
	private String lastResult;

	public SyncHttpConnection()
	{
		client = new SyncHttpClient();
	}

	public String get(String url)
	{
		client.get(createUrl(url), null, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				lastResult = response.toString();
			}
		});

		return lastResult;
	}

	public String post(String url, String jsonBody, Map<String, String> headers)
	{
		RequestParams parameters = new RequestParams();
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			parameters.add(entry.getKey(), entry.getValue());
		}

		client.post(createUrl(url), parameters, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				lastResult = response.toString();
			}
		});

		return lastResult;
	}

	public String put(String url, String jsonBody, Map<String, String> headers)
	{
		RequestParams parameters = new RequestParams();
		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			parameters.add(entry.getKey(), entry.getValue());
		}

		client.put(createUrl(url), parameters, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				lastResult = response.toString();
			}
		});

		return lastResult;
	}

	public String delete(String url)
	{
		client.delete(createUrl(url), null, new JsonHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				lastResult = response.toString();
			}
		});

		return lastResult;
	}

	private String createUrl(String relativeUrl)
	{
		return BASE_URL + relativeUrl;
	}
}
