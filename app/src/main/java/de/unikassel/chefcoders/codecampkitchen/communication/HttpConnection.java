package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.Map;

public interface HttpConnection {
		public String get(String url);

		public String post(String url, String jsonBody, Map<String, String> headers);

		public String put(String url, String jsonBody, Map<String, String> headers);

		public String delete(String url);
}
