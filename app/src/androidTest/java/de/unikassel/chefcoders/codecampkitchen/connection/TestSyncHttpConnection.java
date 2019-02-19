package de.unikassel.chefcoders.codecampkitchen.connection;

import android.support.test.runner.AndroidJUnit4;
import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class TestSyncHttpConnection
{
	HttpConnection connection;
	Map<String, String> headers;
	private String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YzZjMGE0YzBiZTN" +
			"kOTAwMWM4MjQ3ODMiLCJyb2xlIjoiYWRtaW4iLCJuYW1lIjoiYWRtaW4iLCJtYWl" +
			"sIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJpYXQiOjE1NTA1ODQzOTZ9.PnElrI0KvP" +
			"PoNaMrEaFGHdsW2bhI43ryGuQVbAAo6dA";

	String itemID;

	@Before
	public void setup()
	{
		connection = new SyncHttpConnection();

		headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", ADMIN_TOKEN);

		String itemJson = connection.post("/item", "{\n" +
				"\t\"name\":\"Apfelsaft\",\n" +
				"\t\"price\":\"0.50\",\n" +
				"\t\"amount\":\"999\",\n" +
				"\t\"kind\":\"Saft\"\n" +
				"}", headers);


		itemID = JsonTranslator.toItem(itemJson).get_id();
	}

	@Test
	public void testGet()
	{
		connection.get("", headers);
	}

	@Test
	public void testPost()
	{
		connection.post("/item", "{\n" +
				"\t\"name\":\"Orangensaft\",\n" +
				"\t\"price\":\"0.50\",\n" +
				"\t\"amount\":\"999\",\n" +
				"\t\"kind\":\"Saft\"\n" +
				"}", headers);
	}

	@Test
	public void testPut()
	{
		connection.put("/item", itemID, headers);
	}

	@Test
	public void testDelete() {
		connection.delete("/item/" + itemID, headers);
	}
}
