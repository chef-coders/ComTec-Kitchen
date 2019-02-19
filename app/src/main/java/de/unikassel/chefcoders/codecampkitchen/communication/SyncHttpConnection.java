package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import java.util.Map;
import org.json.JSONObject;

public class SyncHttpConnection implements HttpConnection {
		private static final String BASE_URL = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

		private SyncHttpClient client;
		private String lastResult;

		public SyncHttpConnection() {
			client = new SyncHttpClient();
		}

		public String get(String relativeUrl) throws SyncHttpMethodException {
			try {
				String url = createUrl(relativeUrl);
				client.get(url, null, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						lastResult = response.toString();
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
						lastResult = null;
					}
				});
			} catch (Exception ex) {
				lastResult = null;
				throw new SyncHttpMethodException(ex, HttpMethod.Get);
			}
			if (lastResult == null) {
				throw new SyncHttpMethodException(null, HttpMethod.Get);
			}
			return lastResult;
		}

		public String post(String relativeUrl, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException {
			try {
				RequestParams parameters = new RequestParams();
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					parameters.add(entry.getKey(), entry.getValue());
				}

				String url = createUrl(relativeUrl);
				client.post(createUrl(url), parameters, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						lastResult = response.toString();
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
						lastResult = null;
					}
				});
			} catch (Exception ex) {
				throw new SyncHttpMethodException(ex, HttpMethod.Post);
			}
			if (lastResult == null) {
				throw new SyncHttpMethodException(null, HttpMethod.Get);
			}
			return lastResult;
		}

		public String put(String relativeUrl, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException {
			try {
				RequestParams parameters = new RequestParams();
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					parameters.add(entry.getKey(), entry.getValue());
				}

				String url = createUrl(relativeUrl);
				client.put(createUrl(url), parameters, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						lastResult = response.toString();
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
						lastResult = null;
					}
				});
			} catch (Exception ex) {
				throw new SyncHttpMethodException(ex, HttpMethod.Put);
			}
			if (lastResult == null) {
				throw new SyncHttpMethodException(null, HttpMethod.Get);
			}
			return lastResult;
		}

		public String delete(String relativeUrl) throws SyncHttpMethodException {
			try {
				String url = createUrl(relativeUrl);
				client.delete(createUrl(url), null, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						lastResult = response.toString();
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
						lastResult = null;
					}
				});
			} catch (Exception ex) {
				throw new SyncHttpMethodException(ex, HttpMethod.Delete);
			}
			if (lastResult == null) {
				throw new SyncHttpMethodException(null, HttpMethod.Get);
			}
			return lastResult;
		}

		private String createUrl(String relativeUrl) {
			return BASE_URL + relativeUrl;
		}
}
