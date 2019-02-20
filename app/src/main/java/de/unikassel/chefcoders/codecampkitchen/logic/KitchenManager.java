package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KitchenManager
{
	// =============== Fields ===============

	private final LocalDataStore    localDataStore;
	private final KitchenConnection connection;

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

	// =============== Methods ===============

	// --------------- Login and Signup ---------------

	public boolean tryLogin(Context context)
	{
		if (!this.loadUserInfo(context))
		{
			return false;
		}

		final User userData = JsonTranslator.toUser(this.connection.getUser(this.localDataStore.getLoginId()));
		this.localDataStore.addUser(userData);
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

		this.localDataStore.addUser(createdUser);
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
		return new ArrayList<>(this.localDataStore.getUsers());
	}

	public void refreshAllUsers()
	{
		JsonTranslator.toUsers(this.connection.getAllUsers()).forEach(this.localDataStore::addUser);
	}

	public User getLoggedInUser()
	{
		return this.localDataStore.getUser(this.localDataStore.getLoginId());
	}

	public void refreshLoggedInUser()
	{
		this.localDataStore.addUser(JsonTranslator.toUser(this.connection.getUser(this.localDataStore.getLoginId())));
	}

	public boolean isAdmin()
	{
		User user = this.getLoggedInUser();
		return "admin".equals(user.getRole());
	}

	// --------------- Items ---------------

	public List<Item> getItems()
	{
		return new ArrayList<>(this.localDataStore.getItems());
	}

	public void refreshItems()
	{
		JsonTranslator.toItems(this.connection.getAllItems()).forEach(this.localDataStore::addItem);
	}

	public Map<String, List<Item>> getGroupedItems()
	{
		// group by kind (entries sorted case-sensitive)
		final TreeMap<String, List<Item>> grouped = this.localDataStore.getItems().stream().collect(
			Collectors.groupingBy(Item::getKind, TreeMap::new, Collectors.toList()));
		// sort by name (case-insensitive)
		grouped.values().forEach(l -> l.sort(Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER)));
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

		this.localDataStore.addItem(createdItem);
	}

	public void buyItem(Item item, int amount)
	{
		final Purchase purchase = new Purchase().setUser_id(this.getLoggedInUser().get_id()).setItem_id(item.get_id())
		                                        .setAmount(amount);

		final Purchase createdPurchase = JsonTranslator
			                                 .toPurchase(this.connection.buyItem(JsonTranslator.toJson(purchase)));

		this.localDataStore.addPurchase(createdPurchase);
	}

	public boolean containsItem(String id)
	{
		return this.localDataStore.getItem(id) != null;
	}

	public Item getItemById(String id)
	{
		return this.localDataStore.getItem(id);
	}

	// --------------- Purchases ---------------

	private static Predicate<Purchase> userFilter(String userId)
	{
		return p -> userId.equals(p.getUser_id());
	}

	public List<Purchase> getAllPurchases()
	{
		return new ArrayList<>(this.localDataStore.getPurchases());
	}

	public void refreshAllPurchases()
	{
		JsonTranslator.toPurchases(this.connection.getAllPurchases()).forEach(this.localDataStore::addPurchase);
	}

	public List<Purchase> getMyPurchases()
	{
		final String userId = this.localDataStore.getLoginId();
		return this.localDataStore.getPurchases().stream().filter(userFilter(userId)).collect(Collectors.toList());
	}

	public Map<String, List<Purchase>> getMyGroupedPurchases()
	{
		return Collections.singletonMap("All", this.getMyPurchases());
	}

	public void refreshMyPurchases()
	{
		final String userId = this.localDataStore.getLoginId();
		JsonTranslator.toPurchases(this.connection.getPurchasesForUser(userId))
		              .forEach(this.localDataStore::addPurchase);
	}

	// --------------- Cart ---------------

	private static Predicate<Purchase> itemFilter(String itemId)
	{
		return p -> itemId.equals(p.getItem_id());
	}

	public void clearCart()
	{
		this.localDataStore.getShoppingCart().clear();
	}

	public void submitCart()
	{
		for (Purchase purchase : this.localDataStore.getShoppingCart())
		{
			this.connection.buyItem(JsonTranslator.toJson(purchase));
		}

		this.localDataStore.getShoppingCart().clear();
	}

	public int getCartAmount(Item item)
	{
		final String itemId = item.get_id();
		return (int) this.localDataStore.getShoppingCart().stream().filter(itemFilter(itemId)).count();
	}

	public void addToCart(Item item)
	{
		this.addToCart(item, 1);
	}

	public void addToCart(Item item, int amount)
	{
		final String itemId = item.get_id();
		for (Purchase purchase : this.localDataStore.getShoppingCart())
		{
			if (itemId.equals(purchase.getItem_id()))
			{
				purchase.setAmount(purchase.getAmount() + amount);
				return;
			}
		}

		final String loginId = this.localDataStore.getLoginId();
		final Purchase purchase = new Purchase().setItem_id(itemId).setUser_id(loginId).setAmount(amount);
		this.localDataStore.getShoppingCart().add(purchase);
	}

	public void removeFromCart(Item item)
	{
		final String itemId = item.get_id();
		this.localDataStore.getShoppingCart().removeIf(itemFilter(itemId));
	}
}
