package de.unikassel.chefcoders.codecampkitchen.model;

import java.util.HashMap;
import java.util.Map;

public class LocalDataStore
{
	// =============== Fields ===============

	private final Map<String, Item>     items     = createMap();

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
}
