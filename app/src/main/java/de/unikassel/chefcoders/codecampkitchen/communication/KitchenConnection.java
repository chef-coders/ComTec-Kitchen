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

	public String getUser(String userToken)
	{
		return this.connection.get("/users/" + userToken);
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

	public String deleteUser(String userToken)
	{
		return this.connection.delete("/users/" + userToken);
	}

	public String updateUser(String userToken, String userJson)
	{
		return this.connection.put("/users/" + userToken, userJson, createHeaders());
	}

	// --------------- Users ---------------

	public String getPurchase(String purchaseToken)
	{
		return this.connection.get("/purchases/" + purchaseToken);
	}

	public String getAllPurchases()
	{
		return this.connection.get("/purchases");
	}

	public String getPurchasesForUser(String userToken)
	{
		return this.connection.get("/purchases/" + userToken);
	}

	public String buyItem(String buyingDataJson)
	{
		return this.connection.post("/purchases", buyingDataJson, createHeaders());
	}

	public String deletePurchase(String purchaseToken)
	{
		return this.connection.delete("/purchases/" + purchaseToken);
	}

	public String updatePurchase(String purchaseToken, String purchaseJson)
	{
		return this.connection.put("/purchases/" + purchaseToken, purchaseJson, createHeaders());
	}

	// --------------- Items ---------------

	public String getItem(String itemToken)
	{
		return this.connection.get("/items/" + itemToken);
	}

	public String getAllItems()
	{
		return this.connection.get("/items");
	}

	public String createItem(String itemJson)
	{
		return this.connection.post("/items", itemJson, createHeaders());
	}

	public String deleteItem(String itemToken)
	{
		return this.connection.delete("/items/" + itemToken);
	}

	public String updateItem(String itemToken, String itemJson)
	{
		return this.connection.put("/items/" + itemToken, itemJson, createHeaders());
	}
}
