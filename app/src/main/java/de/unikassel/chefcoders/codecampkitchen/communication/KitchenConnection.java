package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.HashMap;
import java.util.Map;

public class KitchenConnection
{
	// =============== Constants ===============

	public static final String USER_KEY  = "QXrnDvfLLy0RdS";
	public static final String ADMIN_KEY = "UkQ4wtmOoWU9Ws";

	// =============== Fields ===============

	private HttpConnection connection;

	// =============== Constructors ===============

	public KitchenConnection(HttpConnection connection)
	{
		this.connection = connection;
	}

	// =============== Static Methods ===============

	private static Map<String, String> createHeaders()
	{
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	// =============== Methods ===============

	// --------------- Server Info ---------------

	public String getServerInfo()
	{
		return this.connection.get("");
	}

	// --------------- Users ---------------

	public String getUser(String userId)
	{
		return this.connection.get("/users/" + userId);
	}

	public String getAllUsers()
	{
		return this.connection.get("/users");
	}

	public String createRegularUser(String userJson)
	{
		final Map<String, String> headers = createHeaders();
		headers.put("key", USER_KEY);
		return this.connection.post("/users", userJson, headers);
	}

	public String createAdminUser(String userJson)
	{
		final Map<String, String> headers = createHeaders();
		headers.put("key", ADMIN_KEY);
		return this.connection.post("/users", userJson, headers);
	}

	public String deleteUser(String userId)
	{
		return this.connection.delete("/users/" + userId);
	}

	public String updateUser(String userToken, String userJson)
	{
		return this.connection.put("/users/" + userToken, userJson, createHeaders());
	}

	// --------------- Users ---------------

	public String getPurchase(String purchaseId)
	{
		return this.connection.get("/purchases/" + purchaseId);
	}

	public String getAllPurchases()
	{
		return this.connection.get("/purchases");
	}

	public String getPurchasesForUser(String userId)
	{
		return this.connection.get("/purchases/" + userId);
	}

	public String buyItem(String buyingDataJson)
	{
		return this.connection.post("/purchases", buyingDataJson, createHeaders());
	}

	public String deletePurchase(String purchaseId)
	{
		return this.connection.delete("/purchases/" + purchaseId);
	}

	public String updatePurchase(String purchaseId, String purchaseJson)
	{
		return this.connection.put("/purchases/" + purchaseId, purchaseJson, createHeaders());
	}

	// --------------- Items ---------------

	public String getItem(String itemId)
	{
		return this.connection.get("/items/" + itemId);
	}

	public String getAllItems()
	{
		return this.connection.get("/items");
	}

	public String createItem(String itemJson)
	{
		return this.connection.post("/items", itemJson, createHeaders());
	}

	public String deleteItem(String itemId)
	{
		return this.connection.delete("/items/" + itemId);
	}

	public String updateItem(String itemId, String itemJson)
	{
		return this.connection.put("/items/" + itemId, itemJson, createHeaders());
	}
}
