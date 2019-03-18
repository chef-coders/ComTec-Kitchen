package de.unikassel.chefcoders.codecampkitchen.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

public class Session
{
	// =============== Static Fields ===============

	public static final Session shared = new Session(KitchenConnection.shared, Users.shared);

	// =============== Fields ===============

	private final KitchenConnection connection;
	private final Users             users;

	private String loginToken;
	private String loginId;

	// =============== Constructors ===============

	public Session(KitchenConnection connection, Users users)
	{
		this.connection = connection;
		this.users = users;
	}

	// =============== Methods ===============

	// --------------- Login and Signup ---------------

	public boolean tryLogin(Context context)
	{
		if (!this.loadUserInfo(context))
		{
			return false;
		}

		final User userData = JsonTranslator.toUser(this.connection.getUser(this.loginId));
		this.users.updateLocal(userData);
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

		this.users.updateLocal(createdUser);
	}

	public void clearUserData(Context context)
	{
		this.setUserInfo(null, null);
		this.saveUserInfo(context);
	}

	// --------------- Logged-In User ---------------

	public User getLoggedInUser()
	{
		return this.users.get(this.loginId);
	}

	public void refreshLoggedInUser()
	{
		this.users.refresh(this.getLoggedInUser());
	}

	public boolean isAdmin()
	{
		return "admin".equals(this.getLoggedInUser().getRole());
	}

	// --------------- Helpers ---------------

	private void setUserInfo(String userToken, String userId)
	{
		this.loginToken = userToken;
		this.loginId = userId;
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
		preferences.edit().putString("userId", this.loginId).putString("userToken", this.loginToken).apply();
	}
}
