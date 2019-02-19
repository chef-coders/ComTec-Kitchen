package de.unikassel.chefcoders.codecampkitchen.connection;

import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class TestSyncHttpConnection
{
	HttpConnection connection;
	private String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YzZjMGE0YzBiZTN" +
								 "kOTAwMWM4MjQ3ODMiLCJyb2xlIjoiYWRtaW4iLCJuYW1lIjoiYWRtaW4iLCJtYWl" +
								 "sIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJpYXQiOjE1NTA1ODQzOTZ9.PnElrI0KvP" +
								 "PoNaMrEaFGHdsW2bhI43ryGuQVbAAo6dA";
	Map<String, String> headers;

	@Before
	public void setup()
	{
		connection = new SyncHttpConnection();

		headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", ADMIN_TOKEN);
	}

	@Test
	public void testGetServerInfo()
	{
		String jsonResponse = connection.get("http://srv8.comtec.eecs.uni-kassel.de:10800/api", headers);

		Assert.assertTrue(jsonResponse.contains("\"version\": \"1.0\""));
	}
}
