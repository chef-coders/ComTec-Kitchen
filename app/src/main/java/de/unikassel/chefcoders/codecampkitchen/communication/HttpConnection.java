package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.Map;

public interface HttpConnection
{
	String get(String url) throws SyncHttpMethodException;

	String post(String url, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException;

	String put(String url, String jsonBody, Map<String, String> headers) throws SyncHttpMethodException;

	String delete(String url) throws SyncHttpMethodException;
}
