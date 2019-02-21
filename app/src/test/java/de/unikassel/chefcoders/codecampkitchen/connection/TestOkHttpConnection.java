package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestOkHttpConnection
{
	final String URL = "http://httpbin.org";
	private HttpConnection connection;

	@Before
	public void setup()
	{
		connection = new OkHttpConnection();
	}

	@Test
	public void validGet()
	{
		connection.get(URL + "/get", headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidGet()
	{
		connection.get(URL + "6984098409849", headers());
	}

	@Test
	public void validPost()
	{
		connection.post(URL + "/post", "", headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidPost()
	{
		connection.post(URL, "", headers());
	}

	@Test
	public void validPut()
	{
		connection.put(URL + "/put", "", headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidPut()
	{
		connection.put(URL, "", headers());
	}

	@Test
	public void validDelete()
	{
		connection.delete(URL + "/delete", headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidDelete()
	{
		connection.delete(URL, headers());
	}

	private Map<String, String> headers()
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}


}
