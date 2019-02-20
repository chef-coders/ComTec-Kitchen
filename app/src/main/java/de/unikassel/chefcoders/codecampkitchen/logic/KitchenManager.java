package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;

import java.util.ArrayList;
import java.util.List;

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

	public User getLoggedInUser()
	{
		return this.localDataStore.getUser(this.localDataStore.getLoginId());
	}

	public boolean isAdmin() {
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
		for (Item item : JsonTranslator.toItems(connection.getAllItems()))
		{
			this.localDataStore.addItem(item);
		}
	}

	public void createItem(String id, String name, double price, int amount, String kind) {
		if (!this.isAdmin()) { return; }

		final Item item = new Item().set_id(id).setName(name).setPrice(price).setAmount(amount).setKind(kind);
		final String itemJson = JsonTranslator.toJson(item);
		final String resultJson;

		resultJson = this.connection.createItem(itemJson);

		final Item createdItem = JsonTranslator.toItem(resultJson);

		this.localDataStore.addItem(createdItem);
	}

	public void buyItem(Item item, int amount) {
		final Purchase purchase = new Purchase().setUser_id(getLoggedInUser().get_id()).setItem_id(item.get_id()).setAmount(amount);
		final String purchaseJson = JsonTranslator.toJson(purchase);
		final String resultJson;

		resultJson = this.connection.buyItem(purchaseJson);

		final Purchase createdPurchase = JsonTranslator.toPurchase(resultJson);

		this.localDataStore.addPurchase(createdPurchase);
	}

	public boolean containsItem(String id) {
		return this.localDataStore.getItem(id) != null;
	}

	public Item getItemById(String id) {
		return this.localDataStore.getItem(id);
	}
}
