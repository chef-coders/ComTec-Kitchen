package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.Map;

public interface HttpConnection
{
	String get(String url);

	String post(String url, String jsonBody, Map<String, String> headers);

	String put(String url, String jsonBody, Map<String, String> headers);

	String delete(String url);
}
