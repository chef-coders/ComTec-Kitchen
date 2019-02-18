package de.unikassel.chefcoders.codecampkitchen.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database
{
	// =============== Fields ===============

	private final Map<String, User>     users     = createMap();
	private final Map<String, Item>     items     = createMap();
	private final Map<String, Purchase> purchases = createMap();

	// =============== Static Methods ===============

	private static <K, V> Map<K, V> createMap()
	{
		// TODO maybe WeakHashMap
		return new HashMap<>();
	}

	// =============== Methods ===============

	// @formatter:off
	public void addUser(User user) { this.users.put(user.get_id(), user); }
	public User getUser(String id) { return this.users.get(id); }
	public Collection<User> getUsers() { return this.users.values(); }

	public void addItem(Item item) { this.items.put(item.get_id(), item); }
	public Item getItem(String id) { return this.items.get(id); }
	public Collection<Item> getItems() { return this.items.values(); }

	public void addPurchase(Purchase purchase) { this.purchases.put(purchase.get_id(), purchase); }
	public Purchase getPurchase(String id) { return this.purchases.get(id); }
	public Collection<Purchase> getPurchases() { return this.purchases.values(); }
	// @formatter:on
}
