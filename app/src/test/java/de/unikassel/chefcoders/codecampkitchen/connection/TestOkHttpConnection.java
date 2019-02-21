package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.*;
import de.unikassel.chefcoders.codecampkitchen.communication.errorhandling.HttpConnectionException;
import de.unikassel.chefcoders.codecampkitchen.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestOkHttpConnection
{
	private HttpConnection connection;

	private String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YzZlOTk5MzJmYzA0ODAwMjYyYTczNmYiLCJyb2xlIjoiYWRtaW4iLCJuYW1lIjoiYWRtaW4iLCJtYWlsIjoiYWRtaW4iLCJpYXQiOjE1NTA3NTIxNDd9.rqdysDdly4megsjYnfqyWpyiI6WMMvWkaL3Kz_HD2JQ";

	@Before
	public void setup()
	{
		connection = new OkHttpConnection();
	}

	@Test
	public void validGet()
	{
		connection.get(KitchenConnection.BASE_URL, headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidGet()
	{
		connection.get(KitchenConnection.BASE_URL + "/items", basicHeaders());
	}

	@Test
	public void validPost()
	{
		User user = new User().setMail("user@example.com").setName("user");

		connection.post(KitchenConnection.BASE_URL + "/users", JsonTranslator.toJson(user), headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidPost()
	{
		User user = new User().setMail("user@example.com").setName("user");

		String result = connection.post(KitchenConnection.BASE_URL + "/users", JsonTranslator.toJson(user), basicHeaders());
	}

	@Test
	public void validPut()
	{
		User user = new User().setMail("user@web.com").setName("altUser");
		String userJson = connection.post(KitchenConnection.BASE_URL + "/users", JsonTranslator.toJson(user), headers());
		user = JsonTranslator.toUser(userJson);

		String result = connection.put(KitchenConnection.BASE_URL + "/users/" + user.get_id(), JsonTranslator.toJson(user), headers());

		connection.delete(KitchenConnection.BASE_URL + "/users/" + user.get_id(), headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidPut()
	{
		String result = connection.put(KitchenConnection.BASE_URL + "/users/" + 123456789, "", basicHeaders());
	}

	@Test
	public void validDelete()
	{
		Item item = new Item().setKind("Saft").setPrice(0.5).setName("Maracujasaft").setAmount(1).set_id("12345");
		String itemJson = connection.post(KitchenConnection.BASE_URL + "/items", JsonTranslator.toJson(item), headers());
		item = JsonTranslator.toItem(itemJson);

		itemJson = connection.delete(KitchenConnection.BASE_URL + "/items/" + item.get_id(), headers());
	}

	@Test(expected = HttpConnectionException.class)
	public void invalidDelete()
	{
		connection.delete(KitchenConnection.BASE_URL + "/items/" + "invalid_id", basicHeaders());
	}

	private Map<String, String> headers()
	{
		Map<String, String> headers = basicHeaders();
		headers.put("Authorization", ADMIN_TOKEN);
		headers.put("key", KitchenConnection.USER_KEY);

		return headers;
	}

	private HashMap<String, String> basicHeaders()
	{
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}
}
