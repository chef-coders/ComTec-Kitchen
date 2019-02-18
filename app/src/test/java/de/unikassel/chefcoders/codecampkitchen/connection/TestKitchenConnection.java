package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.SyncHttpMethodException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestKitchenConnection
{
	KitchenConnection kitchenConnection;
	Mockery context;
	HttpConnection httpConnection;

	@Before
	public void setup()
	{
		context = new Mockery();
		httpConnection = context.mock(HttpConnection.class);
		kitchenConnection = new KitchenConnection(httpConnection);
	}

	@Test
	public void testServerInfo() throws SyncHttpMethodException
	{
		final String url = "/api";
		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get("");
		}});

		kitchenConnection.getServerInfo();

		context.assertIsSatisfied();
	}

	@Test
	public void testGetUser() throws SyncHttpMethodException
	{
		final String userToken = "abc";
		final String url = "/users/" + userToken;
		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url);
		}});

		kitchenConnection.getUser(userToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetAllUsers() throws SyncHttpMethodException
	{
		final String url = "/users";
		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url);
		}});

		kitchenConnection.getAllUsers();

		context.assertIsSatisfied();
	}

	@Test
	public void testCreateRegularUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("key", KitchenConnection.USER_KEY);

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).post(url, userJson, headers);
		}});

		kitchenConnection.createRegularUser(userJson);

		context.assertIsSatisfied();
	}

	@Test
	public void testCreateAdminUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("key", KitchenConnection.ADMIN_KEY);

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).post(url, userJson, headers);
		}});

		kitchenConnection.createAdminUser(userJson);

		context.assertIsSatisfied();
	}

	@Test
	public void deleteUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).delete(url + "/" + userJson);
		}});

		kitchenConnection.deleteUser(userJson);

		context.assertIsSatisfied();
	}

	@Test
	public void updateUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final String userToken = "userToken";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).put(url + "/" + userToken, userJson, headers);
		}});

		kitchenConnection.updateUser(userToken, userJson);

		context.assertIsSatisfied();
	}

	@Test
	public void getPurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url + "/" + purchaseToken);
		}});

		kitchenConnection.getPurchase(purchaseToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetAllPurchases() throws SyncHttpMethodException
	{
		final String url = "/purchases";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url);
		}});

		kitchenConnection.getAllPurchases();

		context.assertIsSatisfied();
	}

	@Test
	public void testGetPurchasesForUser() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String userToken = "user";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url + "/" + userToken);
		}});

		kitchenConnection.getPurchasesForUser(userToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testDeletePurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).delete(url + "/" + purchaseToken);
		}});

		kitchenConnection.deletePurchase(purchaseToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testBuyItem() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).post(url, purchaseJson, headers);
		}});

		kitchenConnection.buyItem(purchaseJson);

		context.assertIsSatisfied();
	}

	@Test
	public void testUpdatePurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final String purchaseToken = "purchase";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).put(url + "/" + purchaseToken, purchaseJson, headers);
		}});

		kitchenConnection.updatePurchase(purchaseToken, purchaseJson);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemToken = "item";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url + "/" + itemToken);
		}});

		kitchenConnection.getItem(itemToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testGetAllItems() throws SyncHttpMethodException
	{
		final String url = "/items";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).get(url);
		}});

		kitchenConnection.getAllItems();

		context.assertIsSatisfied();
	}

	@Test
	public void testCreateItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).post(url, itemJson, headers);
		}});

		kitchenConnection.createItem(itemJson);

		context.assertIsSatisfied();
	}

	@Test
	public void testDeleteItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemToken = "item";

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).delete(url + "/" + itemToken);
		}});

		kitchenConnection.deleteItem(itemToken);

		context.assertIsSatisfied();
	}

	@Test
	public void testUpdateItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemJson = "json";
		final String itemToken = "purchase";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		context.checking(new Expectations()
		{{
			oneOf(httpConnection).put(url + "/" + itemToken, itemJson, headers);
		}});

		kitchenConnection.updateItem(itemToken, itemJson);

		context.assertIsSatisfied();
	}

}
