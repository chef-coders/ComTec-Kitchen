package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
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

	// =============== Methods ===============

	// --------------- Items ---------------

	public List<Item> getItems()
	{
		return new ArrayList<>(this.localDataStore.getItems().values());
	}

	public void refreshItems()
	{
		final Map<String, Item> items = this.localDataStore.getItems();
		items.clear();
		JsonTranslator.toItems(this.connection.getAllItems()).forEach(item -> items.put(item.get_id(), item));
	}

	public Map<String, List<Item>> getGroupedItems()
	{
		return group(this.localDataStore.getItems().values(), Item::getKind,
		             Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER));
	}

	static <K, V> Map<K, List<V>> group(Collection<V> items, Function<? super V, ? extends K> keyExtractor,
		Comparator<? super V> comparator)
	{
		final Map<K, List<V>> grouped = items.stream().collect(
			Collectors.groupingBy(keyExtractor, TreeMap::new, Collectors.toList()));
		grouped.values().forEach(l -> l.sort(comparator));
		return grouped;
	}

	public void createItem(String id, String name, double price, int amount, String kind)
	{
		if (!sessionManager.isAdmin())
		{
			return;
		}

		final Item item = new Item().set_id(id).setName(name).setPrice(price).setAmount(amount).setKind(kind);
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson;

		resultJson = this.connection.createItem(itemJson);

		final Item createdItem = JsonTranslator.toItem(resultJson);

		this.localDataStore.getItems().put(createdItem.get_id(), createdItem);
	}

	public void updateItem(String id, String name, double price, int amount, String kind)
	{
		if (!sessionManager.isAdmin())
		{
			return;
		}

		final Item currentItem = this.getItemById(id);

		final Item item = new Item().setName(name).setPrice(price).setAmount(amount).setKind(kind);
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson;

		resultJson = this.connection.updateItem(id, itemJson);

		final Item updatedItem = JsonTranslator.toItem(resultJson);

		currentItem.setName(updatedItem.getName()).setPrice(updatedItem.getPrice()).setAmount(updatedItem.getAmount())
		           .setKind(updatedItem.getKind());
	}

	public void deleteItem(String id)
	{
		if (!sessionManager.isAdmin())
		{
			return;
		}

		Item currentItem = this.getItemById(id);

		this.connection.deleteItem(id);

		this.localDataStore.getItems().remove(currentItem);
	}

	public void buyItem(Item item, int amount)
	{
		final Purchase purchase = new Purchase().setUser_id(sessionManager.getLoggedInUser().get_id())
		                                        .setItem_id(item.get_id()).setAmount(amount);

		final Purchase createdPurchase = JsonTranslator
			                                 .toPurchase(this.connection.buyItem(JsonTranslator.toJson(purchase)));

		this.localDataStore.getPurchases().put(createdPurchase.get_id(), createdPurchase);
	}

	public boolean containsItem(String id)
	{
		return this.localDataStore.getItems().get(id) != null;
	}

	public Item getItemById(String id)
	{
		return this.localDataStore.getItems().get(id);
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
