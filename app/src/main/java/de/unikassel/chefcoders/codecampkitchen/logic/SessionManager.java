package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

public class SessionManager
{
	private final KitchenManager kitchenManager;

	public SessionManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	// --------------- Login and Signup ---------------

	public boolean tryLogin(Context context)
	{

		if (!this.kitchenManager.session().loadUserInfo(context))
		{
			return false;
		}

		final User userData = JsonTranslator.toUser(
			this.kitchenManager.getConnection().getUser(this.kitchenManager.getLocalDataStore().getLoginId()));
		this.kitchenManager.users().updateLocal(userData);
		return true;
	}

	public void register(Context context, String username, String email, boolean admin)
	{
		final User user = new User().setName(username).setMail(email);
		final String userJson = JsonTranslator.toJson(user);
		final String resultJson;

		if (admin)
		{
			resultJson = this.kitchenManager.getConnection().createAdminUser(userJson);
		}
		else
		{
			resultJson = this.kitchenManager.getConnection().createRegularUser(userJson);
		}

		final User createdUser = JsonTranslator.toUser(resultJson);

		this.kitchenManager.session().setUserInfo(createdUser.getToken(), createdUser.get_id());
		this.kitchenManager.session().saveUserInfo(context);

		this.kitchenManager.users().updateLocal(createdUser);
	}

	public void clearUserData(Context context)
	{
		this.kitchenManager.session().setUserInfo(null, null);
		this.kitchenManager.session().saveUserInfo(context);
	}

	// --------------- Logged-In User ---------------

	public User getLoggedInUser()
	{
		return this.kitchenManager.users().get(this.kitchenManager.getLocalDataStore().getLoginId());
	}

	public void refreshLoggedInUser()
	{
		this.kitchenManager.users().update(this.kitchenManager.session().getLoggedInUser());
	}

	public boolean isAdmin()
	{
		return "admin".equals(this.kitchenManager.session().getLoggedInUser().getRole());
	}

	// --------------- Helpers ---------------

	private void setUserInfo(String userToken, String userId)
	{
		this.kitchenManager.getLocalDataStore().setLoginToken(userToken);
		this.kitchenManager.getLocalDataStore().setLoginId(userId);
		this.kitchenManager.getConnection().setUserToken(userToken);
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

		this.kitchenManager.session().setUserInfo(userToken, userId);
		return true;
	}

	private void saveUserInfo(Context context)
	{
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putString("userId", this.kitchenManager.getLocalDataStore().getLoginId())
		           .putString("userToken", this.kitchenManager.getLocalDataStore().getLoginToken()).apply();
	}
}
