package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.communication.OkHttpConnection;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.LocalDataStore;
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
		return new KitchenManager(new LocalDataStore(), new KitchenConnection(new SyncHttpConnection()));
	}

	// =============== Methods ===============

	// --------------- Login and Signup ---------------

	public boolean tryLogin(Context context)
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

		this.connection.setUserToken(userToken);

		// only possible after setting token!
		final User user = JsonTranslator.toUser(this.connection.getUser(userId));
		this.localDataStore.setLoginId(user.get_id());

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
		this.localDataStore.setLoginId(createdUser.get_id());
		this.connection.setUserToken(createdUser.getToken());

		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString("userId", user.get_id()).putString("userToken", createdUser.getToken()).apply();
	}

	// --------------- Users ---------------

	public User getLoggedInUser()
	{
		return this.localDataStore.getUser(this.localDataStore.getLoginId());
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
}
