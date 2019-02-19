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
	Mockery           context;
	HttpConnection    httpConnection;

	@Before
	public void setup()
	{
		this.context = new Mockery();
		this.httpConnection = this.context.mock(HttpConnection.class);
		this.kitchenConnection = new KitchenConnection(this.httpConnection);
	}

	@Test
	public void testServerInfo() throws SyncHttpMethodException
	{
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get("");
		}});

		this.kitchenConnection.getServerInfo();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetUser() throws SyncHttpMethodException
	{
		final String userToken = "abc";
		final String url = "/users/" + userToken;
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url);
		}});

		this.kitchenConnection.getUser(userToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllUsers() throws SyncHttpMethodException
	{
		final String url = "/users";
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url);
		}});

		this.kitchenConnection.getAllUsers();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateRegularUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("key", KitchenConnection.USER_KEY);

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, userJson, headers);
		}});

		this.kitchenConnection.createRegularUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateAdminUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("key", KitchenConnection.ADMIN_KEY);

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, userJson, headers);
		}});

		this.kitchenConnection.createAdminUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void deleteUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + userJson);
		}});

		this.kitchenConnection.deleteUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void updateUser() throws SyncHttpMethodException
	{
		final String url = "/users";
		final String userJson = "json";
		final String userToken = "userToken";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + userToken, userJson, headers);
		}});

		this.kitchenConnection.updateUser(userToken, userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void getPurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + purchaseToken);
		}});

		this.kitchenConnection.getPurchase(purchaseToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllPurchases() throws SyncHttpMethodException
	{
		final String url = "/purchases";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url);
		}});

		this.kitchenConnection.getAllPurchases();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetPurchasesForUser() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String userToken = "user";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + userToken);
		}});

		this.kitchenConnection.getPurchasesForUser(userToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeletePurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + purchaseToken);
		}});

		this.kitchenConnection.deletePurchase(purchaseToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testBuyItem() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, purchaseJson, headers);
		}});

		this.kitchenConnection.buyItem(purchaseJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testUpdatePurchase() throws SyncHttpMethodException
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final String purchaseToken = "purchase";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + purchaseToken, purchaseJson, headers);
		}});

		this.kitchenConnection.updatePurchase(purchaseToken, purchaseJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemToken = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + itemToken);
		}});

		this.kitchenConnection.getItem(itemToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllItems() throws SyncHttpMethodException
	{
		final String url = "/items";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url);
		}});

		this.kitchenConnection.getAllItems();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemJson = "json";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, itemJson, headers);
		}});

		this.kitchenConnection.createItem(itemJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeleteItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemToken = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + itemToken);
		}});

		this.kitchenConnection.deleteItem(itemToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testUpdateItem() throws SyncHttpMethodException
	{
		final String url = "/items";
		final String itemJson = "json";
		final String itemToken = "purchase";
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + itemToken, itemJson, headers);
		}});

		this.kitchenConnection.updateItem(itemToken, itemJson);

		this.context.assertIsSatisfied();
	}
}
