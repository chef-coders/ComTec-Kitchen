package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.HashMap;
import java.util.Map;

public class KitchenConnection
{
	// =============== Constants ===============

	public static final String USER_KEY  = "QXrnDvfLLy0RdS";
	public static final String ADMIN_KEY = "UkQ4wtmOoWU9Ws";
	public static final String BASE_URL  = "http://srv8.comtec.eecs.uni-kassel.de:10800/api";

	// =============== Static Fields ===============

	public static final KitchenConnection shared = new KitchenConnection(OkHttpConnection.shared);

	// =============== Fields ===============

	private HttpConnection connection;
	private String         userToken;

	// =============== Constructors ===============

	public KitchenConnection(HttpConnection connection)
	{
		this.connection = connection;
	}

	// =============== Properties ===============

	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	// =============== Methods ===============

	// --------------- Server Info ---------------

	public String getServerInfo()
	{
		return this.connection.get(BASE_URL, this.headers());
	}

	// --------------- Users ---------------

	public String getUser(String userId)
	{
		return this.connection.get(BASE_URL + "/users/" + userId, this.headers());
	}

	public String getAllUsers()
	{
		return this.connection.get(BASE_URL + "/users", this.headers());
	}

	public String createRegularUser(String userJson)
	{
		final Map<String, String> headers = this.headers();
		headers.put("key", USER_KEY);
		return this.connection.post(BASE_URL + "/users", userJson, headers);
	}

	public String createAdminUser(String userJson)
	{
		final Map<String, String> headers = this.headers();
		headers.put("key", ADMIN_KEY);
		return this.connection.post(BASE_URL + "/users", userJson, headers);
	}

	public String updateUser(String userId, String userJson)
	{
		return this.connection.put(BASE_URL + "/users/" + userId, userJson, this.headers());
	}

	public String deleteUser(String userId)
	{
		return this.connection.delete(BASE_URL + "/users/" + userId, this.headers());
	}

	// --------------- Purchases ---------------

	public String getPurchase(String purchaseId)
	{
		return this.connection.get(BASE_URL + "/purchases/" + purchaseId, this.headers());
	}

	public String getAllPurchases()
	{
		return this.connection.get(BASE_URL + "/purchases", this.headers());
	}

	public String getMyPurchases()
	{
		return this.connection.get(BASE_URL + "/purchases/u", this.headers());
	}

	public String createPurchase(String buyingDataJson)
	{
		return this.connection.post(BASE_URL + "/purchases", buyingDataJson, this.headers());
	}

	public String updatePurchase(String purchaseId, String purchaseJson)
	{
		return this.connection.put(BASE_URL + "/purchases/" + purchaseId, purchaseJson, this.headers());
	}

	public String deletePurchase(String purchaseId)
	{
		return this.connection.delete(BASE_URL + "/purchases/" + purchaseId, this.headers());
	}

	// --------------- Items ---------------

	public String getItem(String itemId)
	{
		return this.connection.get(BASE_URL + "/items/" + itemId, this.headers());
	}

	public String getAllItems()
	{
		return this.connection.get(BASE_URL + "/items", this.headers());
	}

	public String createItem(String itemJson)
	{
		return this.connection.post(BASE_URL + "/items", itemJson, this.headers());
	}

	public String updateItem(String itemId, String itemJson)
	{
		return this.connection.put(BASE_URL + "/items/" + itemId, itemJson, this.headers());
	}

	public String deleteItem(String itemId)
	{
		return this.connection.delete(BASE_URL + "/items/" + itemId, this.headers());
	}

	// --------------- Helpers ---------------

	private Map<String, String> headers()
	{
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		if (this.userToken != null)
		{
			headers.put("Authorization", this.userToken);
		}
		return headers;
	}
}
