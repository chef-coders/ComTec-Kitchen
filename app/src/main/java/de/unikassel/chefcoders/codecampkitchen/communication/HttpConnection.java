package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.Map;

public interface HttpConnection {
    public String get(String url);

    public void post(String url, String jsonBody, Map<String, String>  headers);

    public void put(String url, String jsonBody, Map<String, String> headers);

    public void delete(String url);
}
