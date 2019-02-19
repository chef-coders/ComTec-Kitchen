package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.HashMap;
import java.util.Map;

public class KitchenConnection
{
	// =============== Constants ===============

	public static final String USER_KEY = "QXrnDvfLLy0RdS";
	public static final String ADMIN_KEY = "UkQ4wtmOoWU9Ws";

	// =============== Fields ===============

	private HttpConnection connection;
	private String userToken;

	// =============== Constructors ===============

	public KitchenConnection(HttpConnection connection)
	{
		this.connection = connection;
	}

	// =============== Methods ===============

	// --------------- Server Info ---------------

	public String getServerInfo()
	{
		return this.connection.get("", headers());
	}

	// --------------- Users ---------------

	public String getUser(String userId)
	{
		return this.connection.get("/users/" + userId, headers());
	}

	public String getAllUsers()
	{
		return this.connection.get("/users", headers());
	}

	public String createRegularUser(String userJson)
	{
		final Map<String, String> headers = headers();
		headers.put("key", USER_KEY);
		return this.connection.post("/users", userJson, headers);
	}

	public String createAdminUser(String userJson)
	{
		final Map<String, String> headers = headers();
		headers.put("key", ADMIN_KEY);
		return this.connection.post("/users", userJson, headers);
	}

	public String deleteUser(String userId)
	{
		return this.connection.delete("/users/" + userId, headers());
	}

	public String updateUser(String userToken, String userJson)
	{
		return this.connection.put("/users/" + userToken, userJson, headers());
	}

	// --------------- Users ---------------

	public String getPurchase(String purchaseId)
	{
		return this.connection.get("/purchases/" + purchaseId, headers());
	}

	public String getAllPurchases()
	{
		return this.connection.get("/purchases", headers());
	}

	public String getPurchasesForUser(String userId)
	{
		return this.connection.get("/purchases/" + userId, headers());
	}

	public String buyItem(String buyingDataJson)
	{
		return this.connection.post("/purchases", buyingDataJson, headers());
	}

	public String deletePurchase(String purchaseId)
	{
		return this.connection.delete("/purchases/" + purchaseId, headers());
	}

	public String updatePurchase(String purchaseId, String purchaseJson)
	{
		return this.connection.put("/purchases/" + purchaseId, purchaseJson, headers());
	}

	// --------------- Items ---------------

	public String getItem(String itemId)
	{
		return this.connection.get("/items/" + itemId, headers());
	}

	public String getAllItems()
	{
		return this.connection.get("/items", headers());
	}

	public String createItem(String itemJson)
	{
		return this.connection.post("/items", itemJson, headers());
	}

	public String deleteItem(String itemId)
	{
		return this.connection.delete("/items/" + itemId, headers());
	}

	public String updateItem(String itemId, String itemJson)
	{
		return this.connection.put("/items/" + itemId, itemJson, headers());
	}

	private Map<String, String> headers()
	{
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", this.userToken);
		return headers;
	}

	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}
}
