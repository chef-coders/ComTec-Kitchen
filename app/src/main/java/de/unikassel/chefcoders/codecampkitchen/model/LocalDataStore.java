package de.unikassel.chefcoders.codecampkitchen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalDataStore
{
	// =============== Fields ===============

	private String loginToken;
	private String loginId;

	private final List<Purchase> shoppingCart = new ArrayList<>();

	private final Map<String, User>     users     = createMap();
	private final Map<String, Item>     items     = createMap();
	private final Map<String, Purchase> purchases = createMap();

	// =============== Static Methods ===============

	private static <K, V> Map<K, V> createMap()
	{
		// TODO maybe WeakHashMap
		return new HashMap<>();
	}

	// =============== Properties ===============

	public String getLoginToken()
	{
		return this.loginToken;
	}

	public void setLoginToken(String loginToken)
	{
		this.loginToken = loginToken;
	}

	public String getLoginId()
	{
		return this.loginId;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public List<Purchase> getShoppingCart()
	{
		return this.shoppingCart;
	}

	// =============== Methods ===============

	public Map<String, User> getUsers()
	{
		return this.users;
	}

	public Map<String, Item> getItems()
	{
		return this.items;
	}

	public Map<String, Purchase> getPurchases()
	{
		return this.purchases;
	}
}
