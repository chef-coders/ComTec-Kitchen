package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KitchenManager
{
	// =============== Fields ===============

	private final KitchenConnection connection;

	private final Cart      cart      = new Cart(this);
	private final Users     users     = new Users(this);
	private final Session   session   = new Session(this);
	private final Purchases purchases = new Purchases(this);
	private final Items     items     = new Items(this);

	// =============== Constructor ===============

	public KitchenManager(KitchenConnection connection)
	{
		this.connection = connection;
	}

	// =============== Static Methods ===============

	public static KitchenManager create()
	{
		return new KitchenManager(new KitchenConnection(new OkHttpConnection()));
	}

	// =============== Properties ===============

	public KitchenConnection getConnection()
	{
		return this.connection;
	}

	public Cart cart()
	{
		return this.cart;
	}

	public Users users()
	{
		return this.users;
	}

	public Session session()
	{
		return this.session;
	}

	public Purchases purchases()
	{
		return this.purchases;
	}

	public Items items()
	{
		return this.items;
	}

	// =============== Methods ===============

	// --------------- Items ---------------

	@Deprecated
	public List<Item> getItems()
	{
		return this.items().getAll();
	}

	@Deprecated
	public void refreshItems()
	{
		this.items().refreshAll();
	}

	@Deprecated
	public Map<String, List<Item>> getGroupedItems()
	{
		return this.items().getGrouped();
	}

	@Deprecated
	public void createItem(String id, String name, double price, int amount, String kind)
	{
		this.items().createItem(id, name, price, amount, kind);
	}

	@Deprecated
	public void updateItem(String id, String name, double price, int amount, String kind)
	{
		this.items().updateItem(id, name, price, amount, kind);
	}

	@Deprecated
	public void deleteItem(String id)
	{
		this.items().delete(id);
	}

	@Deprecated
	public boolean containsItem(String id)
	{
		return this.items().exists(id);
	}

	@Deprecated
	public Item getItemById(String id)
	{
		return this.items().get(id);
	}

	// --------------- Helpers Methods---------------

	static <K, V> Map<K, List<V>> group(Collection<V> items, Function<? super V, ? extends K> keyExtractor,
		Comparator<? super V> comparator)
	{
		final Map<K, List<V>> grouped = items.stream().collect(
			Collectors.groupingBy(keyExtractor, TreeMap::new, Collectors.toList()));
		grouped.values().forEach(l -> l.sort(comparator));
		return grouped;
	}
}
