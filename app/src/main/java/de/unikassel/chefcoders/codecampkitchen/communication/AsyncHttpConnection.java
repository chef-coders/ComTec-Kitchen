package de.unikassel.chefcoders.codecampkitchen.communication;

import com.loopj.android.http.*;

public class AsyncHttpConnection implements HttpConnection {
    private static final Sting BASE_URL = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

    private AsyncHttpClient client;

    public AsyncHttpConnection() {
        client = new AsyncHttpClient();
    }

    public String get(String url) {
        String result;
        await(client.get(createUrl(url), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                result = new String(bytes, StandardCharsets.UTF_8);
            }
        }));
        return result;
    }

    public void post(String url, String jsonBody) {
        client.post(createUrl(url), params, responseHandler);
    }

    public void put(String url, String jsonBody) {

    }

    public void delete(String url) {

    }

    private String createUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
