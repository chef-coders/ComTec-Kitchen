package de.unikassel.chefcoders.codecampkitchen.communication;

public interface HttpConnection {
    public String get(String url);

    public void post(String url, String jsonBody);

    public void put(String url, String jsonBody);

    public void delete(String url);
}
