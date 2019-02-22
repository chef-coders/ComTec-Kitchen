package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KitchenManager
{
	// =============== Fields ===============

	private final LocalDataStore    localDataStore;
	private final KitchenConnection connection;

	private final CartManager     cartManager     = new CartManager(this);
	private final UsersManager    usersManager    = new UsersManager(this);
	private final SessionManager  sessionManager  = new SessionManager(this);
	private final PurchaseManager purchaseManager = new PurchaseManager(this);
	private final ItemManager     itemManager     = new ItemManager(this);

	// =============== Constructor ===============

	public KitchenManager(LocalDataStore localDataStore, KitchenConnection connection)
	{
		this.localDataStore = localDataStore;
		this.connection = connection;
	}

	// =============== Static Methods ===============

	public static KitchenManager create()
	{
		return new KitchenManager(new LocalDataStore(), new KitchenConnection(new OkHttpConnection()));
	}

	// =============== Properties ===============

	public LocalDataStore getLocalDataStore()
	{
		return this.localDataStore;
	}

	public KitchenConnection getConnection()
	{
		return this.connection;
	}

	public CartManager cart()
	{
		return this.cartManager;
	}

	public UsersManager users()
	{
		return this.usersManager;
	}

	public SessionManager session()
	{
		return this.sessionManager;
	}

	public PurchaseManager purchases()
	{
		return this.purchaseManager;
	}

	public ItemManager items()
	{
		return this.itemManager;
	}

	// =============== Methods ===============

	// --------------- Items ---------------

	@Deprecated
	public List<Item> getItems()
	{
		return this.items().getItems();
	}

	@Deprecated
	public void refreshItems()
	{
		this.items().refreshItems();
	}

	@Deprecated
	public Map<String, List<Item>> getGroupedItems()
	{
		return this.items().getGroupedItems();
	}

	static <K, V> Map<K, List<V>> group(Collection<V> items, Function<? super V, ? extends K> keyExtractor,
		Comparator<? super V> comparator)
	{
		final Map<K, List<V>> grouped = items.stream().collect(
			Collectors.groupingBy(keyExtractor, TreeMap::new, Collectors.toList()));
		grouped.values().forEach(l -> l.sort(comparator));
		return grouped;
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
		this.items().deleteItem(id);
	}

	@Deprecated
	public void buyItem(Item item, int amount)
	{
		this.items().buyItem(item, amount);
	}

	@Deprecated
	public boolean containsItem(String id)
	{
		return this.items().containsItem(id);
	}

	@Deprecated
	public Item getItemById(String id)
	{
		return this.items().getItemById(id);
	}

	// --------------- Purchases ---------------

	static Predicate<Purchase> userFilter(String userId)
	{
		return p -> userId.equals(p.getUser_id());
	}

	@Deprecated
	public List<Purchase> getAllPurchases()
	{
		return this.purchases().getAll();
	}

	@Deprecated
	public void refreshAllPurchases()
	{
		this.purchases().refreshAll();
	}

	@Deprecated
	public List<Purchase> getMyPurchases()
	{
		return this.purchases().getMine();
	}

	@Deprecated
	public Map<String, List<Purchase>> getMyGroupedPurchases()
	{
		return this.purchases().getMineGrouped();
	}

	@Deprecated
	public void refreshMyPurchases()
	{
		this.purchases().refreshMine();
	}

	// --------------- Cart ---------------

	static Predicate<Purchase> itemFilter(String itemId)
	{
		return p -> itemId.equals(p.getItem_id());
	}
}
