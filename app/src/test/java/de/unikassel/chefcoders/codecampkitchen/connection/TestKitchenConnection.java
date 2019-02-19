package de.unikassel.chefcoders.codecampkitchen.connection;

import de.unikassel.chefcoders.codecampkitchen.communication.HttpConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestKitchenConnection
{
	private KitchenConnection kitchenConnection;
	private Mockery           context;
	private HttpConnection    httpConnection;

	private final String USER_TOKEN = "userToken";

	@Before
	public void setup()
	{
		this.context = new Mockery();
		this.httpConnection = this.context.mock(HttpConnection.class);
		this.kitchenConnection = new KitchenConnection(this.httpConnection);
		kitchenConnection.setUserToken(USER_TOKEN);
	}

	@Test
	public void testServerInfo()
	{
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get("", headers());
		}});

		this.kitchenConnection.getServerInfo();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetUser()
	{
		final String userToken = "abc";
		final String url = "/users/" + userToken;
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url, headers());
		}});

		this.kitchenConnection.getUser(userToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllUsers()
	{
		final String url = "/users";
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url, headers());
		}});

		this.kitchenConnection.getAllUsers();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateRegularUser()
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = headers();
		headers.put("key", KitchenConnection.USER_KEY);

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, userJson, headers);
		}});

		this.kitchenConnection.createRegularUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateAdminUser()
	{
		final String url = "/users";
		final String userJson = "json";
		final Map<String, String> headers = headers();
		headers.put("key", KitchenConnection.ADMIN_KEY);

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, userJson, headers);
		}});

		this.kitchenConnection.createAdminUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void deleteUser()
	{
		final String url = "/users";
		final String userJson = "json";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + userJson, headers());
		}});

		this.kitchenConnection.deleteUser(userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void updateUser()
	{
		final String url = "/users";
		final String userJson = "json";
		final String userToken = "userToken";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + userToken, userJson, headers);
		}});

		this.kitchenConnection.updateUser(userToken, userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void getPurchase()
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + purchaseToken, headers());
		}});

		this.kitchenConnection.getPurchase(purchaseToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllPurchases()
	{
		final String url = "/purchases";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url, headers());
		}});

		this.kitchenConnection.getAllPurchases();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetPurchasesForUser()
	{
		final String url = "/purchases";
		final String userToken = "user";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + userToken, headers());
		}});

		this.kitchenConnection.getPurchasesForUser(userToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeletePurchase()
	{
		final String url = "/purchases";
		final String purchaseToken = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + purchaseToken, headers());
		}});

		this.kitchenConnection.deletePurchase(purchaseToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testBuyItem()
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, purchaseJson, headers);
		}});

		this.kitchenConnection.buyItem(purchaseJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testUpdatePurchase()
	{
		final String url = "/purchases";
		final String purchaseJson = "json";
		final String purchaseToken = "purchase";
		final Map<String, String> headers = headers();
		headers().put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + purchaseToken, purchaseJson, headers);
		}});

		this.kitchenConnection.updatePurchase(purchaseToken, purchaseJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetItem()
	{
		final String url = "/items";
		final String itemToken = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + itemToken, headers());
		}});

		this.kitchenConnection.getItem(itemToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllItems()
	{
		final String url = "/items";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url, headers());
		}});

		this.kitchenConnection.getAllItems();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testCreateItem()
	{
		final String url = "/items";
		final String itemJson = "json";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).post(url, itemJson, headers);
		}});

		this.kitchenConnection.createItem(itemJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeleteItem()
	{
		final String url = "/items";
		final String itemToken = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + itemToken, headers());
		}});

		this.kitchenConnection.deleteItem(itemToken);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testUpdateItem()
	{
		final String url = "/items";
		final String itemJson = "json";
		final String itemToken = "purchase";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + itemToken, itemJson, headers);
		}});

		this.kitchenConnection.updateItem(itemToken, itemJson);

		this.context.assertIsSatisfied();
	}

	private Map<String, String> headers()
	{
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", USER_TOKEN);
		return headers;
	}
}
