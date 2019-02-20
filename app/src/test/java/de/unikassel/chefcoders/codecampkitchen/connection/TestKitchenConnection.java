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
			this.oneOf(TestKitchenConnection.this.httpConnection).get(KitchenConnection.BASE_URL, headers());
		}});

		this.kitchenConnection.getServerInfo();

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetUser()
	{
		final String userId = "abc";
		final String url = KitchenConnection.BASE_URL + "/users/" + userId;
		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url, headers());
		}});

		this.kitchenConnection.getUser(userId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllUsers()
	{
		final String url = KitchenConnection.BASE_URL + "/users";
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
		final String url = KitchenConnection.BASE_URL + "/users";
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
		final String url = KitchenConnection.BASE_URL + "/users";
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
		final String url = KitchenConnection.BASE_URL + "/users";
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
		final String url = KitchenConnection.BASE_URL + "/users";
		final String userJson = "json";
		final String userId = "userId";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + userId, userJson, headers);
		}});

		this.kitchenConnection.updateUser(userId, userJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void getPurchase()
	{
		final String url = KitchenConnection.BASE_URL + "/purchases";
		final String purchaseId = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + purchaseId, headers());
		}});

		this.kitchenConnection.getPurchase(purchaseId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllPurchases()
	{
		final String url = KitchenConnection.BASE_URL + "/purchases";

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
		final String url = KitchenConnection.BASE_URL + "/purchases";
		final String userId = "user";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + userId, headers());
		}});

		this.kitchenConnection.getPurchasesForUser(userId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testDeletePurchase()
	{
		final String url = KitchenConnection.BASE_URL + "/purchases";
		final String purchaseId = "purchase";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + purchaseId, headers());
		}});

		this.kitchenConnection.deletePurchase(purchaseId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testBuyItem()
	{
		final String url = KitchenConnection.BASE_URL + "/purchases";
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
		final String url = KitchenConnection.BASE_URL + "/purchases";
		final String purchaseJson = "json";
		final String purchaseId = "purchase";
		final Map<String, String> headers = headers();
		headers().put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + purchaseId, purchaseJson, headers);
		}});

		this.kitchenConnection.updatePurchase(purchaseId, purchaseJson);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetItem()
	{
		final String url = KitchenConnection.BASE_URL + "/items";
		final String itemId = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).get(url + "/" + itemId, headers());
		}});

		this.kitchenConnection.getItem(itemId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testGetAllItems()
	{
		final String url = KitchenConnection.BASE_URL + "/items";

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
		final String url = KitchenConnection.BASE_URL + "/items";
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
		final String url = KitchenConnection.BASE_URL + "/items";
		final String itemId = "item";

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).delete(url + "/" + itemId, headers());
		}});

		this.kitchenConnection.deleteItem(itemId);

		this.context.assertIsSatisfied();
	}

	@Test
	public void testUpdateItem()
	{
		final String url = KitchenConnection.BASE_URL + "/items";
		final String itemJson = "json";
		final String itemId = "purchase";
		final Map<String, String> headers = headers();
		headers.put("Content-Type", "application/json");

		this.context.checking(new Expectations()
		{{
			this.oneOf(TestKitchenConnection.this.httpConnection).put(url + "/" + itemId, itemJson, headers);
		}});

		this.kitchenConnection.updateItem(itemId, itemJson);

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
