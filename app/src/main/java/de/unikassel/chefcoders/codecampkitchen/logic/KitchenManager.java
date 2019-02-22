package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KitchenManager
{
	// =============== Fields ===============

	private final LocalDataStore    localDataStore;
	private final KitchenConnection connection;

	private final CartManager       cartManager = new CartManager(this);

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

	// =============== Methods ===============

	// --------------- Login and Signup ---------------

	public boolean tryLogin(Context context)
	{
		if (!this.loadUserInfo(context))
		{
			return false;
		}

		final User userData = JsonTranslator.toUser(this.connection.getUser(this.localDataStore.getLoginId()));
		this.localDataStore.getUsers().put(userData.get_id(), userData);
		return true;
	}

	public void register(Context context, String username, String email, boolean admin)
	{
		final User user = new User().setName(username).setMail(email);
		final String userJson = JsonTranslator.toJson(user);
		final String resultJson;

		if (admin)
		{
			resultJson = this.connection.createAdminUser(userJson);
		}
		else
		{
			resultJson = this.connection.createRegularUser(userJson);
		}

		final User createdUser = JsonTranslator.toUser(resultJson);

		this.setUserInfo(createdUser.getToken(), createdUser.get_id());
		this.saveUserInfo(context);

		this.localDataStore.getUsers().put(createdUser.get_id(), createdUser);
	}

	public void clearUserData(Context context)
	{
		this.setUserInfo(null, null);
		this.saveUserInfo(context);
	}

	// --------------- User Info ---------------

	private void setUserInfo(String userToken, String userId)
	{
		this.localDataStore.setLoginToken(userToken);
		this.localDataStore.setLoginId(userId);
		this.connection.setUserToken(userToken);
	}

	private boolean loadUserInfo(Context context)
	{
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

		final String userToken = preferences.getString("userToken", null);
		if (userToken == null)
		{
			return false;
		}

		final String userId = preferences.getString("userId", null);
		if (userId == null)
		{
			return false;
		}

		this.setUserInfo(userToken, userId);
		return true;
	}

	private void saveUserInfo(Context context)
	{
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString("userId", this.localDataStore.getLoginId())
		           .putString("userToken", this.localDataStore.getLoginToken()).apply();
	}

	// --------------- Users ---------------

	public List<User> getAllUsers()
	{
		return new ArrayList<>(this.localDataStore.getUsers().values());
	}

	public Map<String, List<User>> getGroupedUsers()
	{
		return group(this.localDataStore.getUsers().values(), u -> u.getName().substring(0, 1).toUpperCase(),
		             Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER));
	}

	public void refreshAllUsers()
	{
		final Map<String, User> users = this.localDataStore.getUsers();
		users.clear();
		JsonTranslator.toUsers(this.connection.getAllUsers()).forEach(user -> users.put(user.get_id(), user));
	}

	public User getLoggedInUser()
	{
		return this.localDataStore.getUsers().get(this.localDataStore.getLoginId());
	}

	public void refreshLoggedInUser()
	{
		User user = JsonTranslator.toUser(this.connection.getUser(this.localDataStore.getLoginId()));
		this.localDataStore.getUsers().put(user.get_id(), user);
	}

	public boolean isAdmin()
	{
		User user = this.getLoggedInUser();
		return "admin".equals(user.getRole());
	}

	public boolean deleteUser(User user)
	{
		this.connection.deleteUser(user.get_id());
		return true;
	}

	public User getUserById(String userId)
	{
		return this.localDataStore.getUsers().get(userId);
	}

	public void updateUser(User user)
	{
		this.connection.updateUser(user.get_id(), JsonTranslator.toJson(user));
	}

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

	private static <K, V> Map<K, List<V>> group(Collection<V> items, Function<? super V, ? extends K> keyExtractor,
		Comparator<? super V> comparator)
	{
		final Map<K, List<V>> grouped = items.stream().collect(
			Collectors.groupingBy(keyExtractor, TreeMap::new, Collectors.toList()));
		grouped.values().forEach(l -> l.sort(comparator));
		return grouped;
	}

	public void createItem(String id, String name, double price, int amount, String kind)
	{
		if (!this.isAdmin())
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
		if (!this.isAdmin())
		{
			return;
		}

		final Item currentItem = getItemById(id);

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
		if (!this.isAdmin())
		{
			return;
		}

		Item currentItem = getItemById(id);

		this.connection.deleteItem(id);

		this.localDataStore.getItems().remove(currentItem);
	}

	public void buyItem(Item item, int amount)
	{
		final Purchase purchase = new Purchase().setUser_id(this.getLoggedInUser().get_id()).setItem_id(item.get_id())
		                                        .setAmount(amount);

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

	private static Predicate<Purchase> userFilter(String userId)
	{
		return p -> userId.equals(p.getUser_id());
	}

	public List<Purchase> getAllPurchases()
	{
		return new ArrayList<>(this.localDataStore.getPurchases().values());
	}

	public void refreshAllPurchases()
	{
		JsonTranslator.toPurchases(this.connection.getAllPurchases())
		              .forEach(purchase -> this.localDataStore.getPurchases().put(purchase.get_id(), purchase));
	}

	public List<Purchase> getMyPurchases()
	{
		final String userId = this.localDataStore.getLoginId();
		return this.localDataStore.getPurchases().values().stream().filter(userFilter(userId))
		                          .collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMyGroupedPurchases()
	{
		final Collection<Purchase> purchases = this.localDataStore.getPurchases().values();
		if (purchases.isEmpty())
		{
			return Collections.singletonMap("Nothing here", Collections.emptyList());
		}

		return purchases.stream().collect(Collectors.groupingBy(p -> p.getCreated().substring(0, 10)));
	}

	public void refreshMyPurchases()
	{
		final Map<String, Purchase> purchases = this.localDataStore.getPurchases();
		JsonTranslator.toPurchases(this.connection.getPurchasesForUser())
		              .forEach(purchase -> purchases.put(purchase.get_id(), purchase));
	}

	// --------------- Cart ---------------

	static Predicate<Purchase> itemFilter(String itemId)
	{
		return p -> itemId.equals(p.getItem_id());
	}
}
