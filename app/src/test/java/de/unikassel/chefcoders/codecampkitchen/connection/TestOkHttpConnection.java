package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.*;
import de.unikassel.chefcoders.codecampkitchen.model.*;
import org.junit.Before;
import org.junit.Test;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

public class TestOkHttpConnection
{
	private HttpConnection connection;

	private String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YzZjMGE0YzBiZTNkOTAwMWM4MjQ3ODMiLCJyb2xlIjoiYWRtaW4iLCJuYW1lIjoiYWRtaW4iLCJtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJpYXQiOjE1NTA1ODQzOTZ9.PnElrI0KvPPoNaMrEaFGHdsW2bhI43ryGuQVbAAo6dA";

	@Before
	public void setup()
	{
		connection = new OkHttpConnection();
	}

	@Test
	public void testGetSimple()
	{
		String result = connection.get(KitchenConnection.BASE_URL, headers());

		System.out.println(result);
	}

	@Test
	public void testPost()
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", ADMIN_TOKEN);
		headers.put("key", KitchenConnection.USER_KEY);

		User user = new User().setMail("user@example.com").setName("user");

		String result = connection.post(KitchenConnection.BASE_URL + "/users", JsonTranslator.toJson(user), headers());

		System.out.println(result);
	}

	@Test
	public void testPut()
	{
		User user = new User().setMail("user@web.com").setName("altUser");
		String userJson = connection.post(KitchenConnection.BASE_URL + "/users", JsonTranslator.toJson(user), headers());
		user = JsonTranslator.toUser(userJson);

		String result = connection.put(KitchenConnection.BASE_URL + "/users/" + user.get_id(), JsonTranslator.toJson(user), headers());

		System.out.println(result);

		connection.delete(KitchenConnection.BASE_URL + "/users/" + user.get_id(), headers());
	}

	@Test
	public void testDelete()
	{
		Item item = new Item().setKind("item").setPrice(20).setName("item").setAmount(1);
		String itemJson = connection.post(KitchenConnection.BASE_URL + "/items", JsonTranslator.toJson(item), headers());
		item = JsonTranslator.toItem(itemJson);

		itemJson = connection.delete(KitchenConnection.BASE_URL + "/items/" + item.get_id(), headers());

		System.out.println(itemJson);
	}

	private Map<String, String> headers()
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", ADMIN_TOKEN);
		headers.put("key", KitchenConnection.USER_KEY);

		return headers;
	}
}
