package de.unikassel.chefcoders.codecampkitchen.communication;

import java.util.HashMap;
import java.util.Map;

public class KitchenConnection
{
	public static final String USER_KEY = "QXrnDvfLLy0RdS";
	public static final String ADMIN_KEY = "UkQ4wtmOoWU9Ws";

	private HttpConnection connection;

	public KitchenConnection(HttpConnection connection)
	{
		this.connection = connection;
	}

	// Server info
	public String getServerInfo()
	{
		return connection.get("");
	}

	// Users
	public String getUser(String userToken)
	{
		return connection.get("/users/" + userToken);
	}

	public String getAllUsers()
	{
		return connection.get("/users");
	}

	public String createRegularUser(String userJson)
	{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("key", USER_KEY);
		return connection.post("/users", userJson, headers);
	}

	public String createAdminUser(String userJson)
	{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("key", ADMIN_KEY);
		headers.put("Content-Type", "application/json");
		return connection.post("/users", userJson, headers);
	}

	public String deleteUser(String userToken)
	{
		return connection.delete("/users/" + userToken);
	}

	public String updateUser(String userToken, String userJson)
	{

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return connection.put("/users/" + userToken, userJson, headers);
	}

	// Purchases
	public String getPurchase(String purchaseToken)
	{
		return connection.get("/purchases/" + purchaseToken);
	}

	public String getAllPurchases()
	{
		return connection.get("/purchases");
	}

	public String getPurchasesForUser(String userToken)
	{
		return connection.get("/purchases/" + userToken);
	}

	public String buyItem(String buyingDataJson)
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return connection.post("/purchases", buyingDataJson, headers);
	}

	public String deletePurchase(String purchaseToken)
	{
		return connection.delete("/purchases/" + purchaseToken);
	}

	public String updatePurchase(String purchaseToken, String purchaseJson)
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return connection.put("/purchases/" + purchaseToken, purchaseJson, headers);
	}

	// Items
	public String getItem(String itemToken)
	{
		return connection.get("/items/" + itemToken);
	}

	public String getAllItems()
	{
		return connection.get("/items");
	}

	public String createItem(String itemJson)
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return connection.post("/items", itemJson, headers);
	}

	public String deleteItem(String itemToken)
	{
		return connection.delete("/items/" + itemToken);
	}

	public String updateItem(String itemToken, String itemJson)
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return connection.put("/items/" + itemToken, itemJson, headers);
	}
}
