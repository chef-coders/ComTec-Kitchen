package de.unikassel.chefcoders.codecampkitchen.model;

import java.util.HashMap;
import java.util.Map;

public class LocalDataStore
{
	// =============== Fields ===============

	private final Map<String, Item>     items     = createMap();
	private final Map<String, Purchase> purchases = createMap();

	// =============== Static Methods ===============

	private static <K, V> Map<K, V> createMap()
	{
		// TODO maybe WeakHashMap
		return new HashMap<>();
	}

	// =============== Methods ===============

	public Map<String, Item> getItems()
	{
		return this.items;
	}

	public Map<String, Purchase> getPurchases()
	{
		return this.purchases;
	}
}
