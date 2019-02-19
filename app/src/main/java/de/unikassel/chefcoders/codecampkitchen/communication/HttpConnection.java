package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.Map;

public interface HttpConnection
{
	String get(String url, Map<String, String> headers);

	String post(String url, String jsonBody, Map<String, String> headers);

	String put(String url, String jsonBody, Map<String, String> headers);

	String delete(String url, Map<String, String> headers);
}
