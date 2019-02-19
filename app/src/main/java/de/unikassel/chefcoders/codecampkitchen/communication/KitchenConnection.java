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

	// =============== Methods ===============

	// Server info
	public String getServerInfo() throws SyncHttpMethodException
	{
		return this.connection.get("");
	}

	// Users
	public String getUser(String userToken) throws SyncHttpMethodException
	{
		return this.connection.get("/users/" + userToken);
	}

	public String getAllUsers() throws SyncHttpMethodException
	{
		return this.connection.get("/users");
	}

	public String createRegularUser(String userJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("key", USER_KEY);
		return this.connection.post("/users", userJson, headers);
	}

	public String createAdminUser(String userJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("key", ADMIN_KEY);
		headers.put("Content-Type", "application/json");
		return this.connection.post("/users", userJson, headers);
	}

	public String deleteUser(String userToken) throws SyncHttpMethodException
	{
		return this.connection.delete("/users/" + userToken);
	}

	public String updateUser(String userToken, String userJson) throws SyncHttpMethodException
	{

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return this.connection.put("/users/" + userToken, userJson, headers);
	}

	// Purchases
	public String getPurchase(String purchaseToken) throws SyncHttpMethodException
	{
		return this.connection.get("/purchases/" + purchaseToken);
	}

	public String getAllPurchases() throws SyncHttpMethodException
	{
		return this.connection.get("/purchases");
	}

	public String getPurchasesForUser(String userToken) throws SyncHttpMethodException
	{
		return this.connection.get("/purchases/" + userToken);
	}

	public String buyItem(String buyingDataJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return this.connection.post("/purchases", buyingDataJson, headers);
	}

	public String deletePurchase(String purchaseToken) throws SyncHttpMethodException
	{
		return this.connection.delete("/purchases/" + purchaseToken);
	}

	public String updatePurchase(String purchaseToken, String purchaseJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return this.connection.put("/purchases/" + purchaseToken, purchaseJson, headers);
	}

	// Items
	public String getItem(String itemToken) throws SyncHttpMethodException
	{
		return this.connection.get("/items/" + itemToken);
	}

	public String getAllItems() throws SyncHttpMethodException
	{
		return this.connection.get("/items");
	}

	public String createItem(String itemJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return this.connection.post("/items", itemJson, headers);
	}

	public String deleteItem(String itemToken) throws SyncHttpMethodException
	{
		return this.connection.delete("/items/" + itemToken);
	}

	public String updateItem(String itemToken, String itemJson) throws SyncHttpMethodException
	{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return this.connection.put("/items/" + itemToken, itemJson, headers);
	}
}
