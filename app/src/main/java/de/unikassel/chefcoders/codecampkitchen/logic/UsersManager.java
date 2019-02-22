package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UsersManager
{
	private final KitchenManager kitchenManager;

	public UsersManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	public List<User> getAll()
	{
		return new ArrayList<>(this.kitchenManager.getLocalDataStore().getUsers().values());
	}

	public Map<String, List<User>> getGrouped()
	{
		return KitchenManager.group(this.kitchenManager.getLocalDataStore().getUsers().values(),
		                            u -> u.getName().substring(0, 1).toUpperCase(),
		                            Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER));
	}

	public void refreshAll()
	{
		final Map<String, User> users = this.kitchenManager.getLocalDataStore().getUsers();
		users.clear();
		JsonTranslator.toUsers(this.kitchenManager.getConnection().getAllUsers())
		              .forEach(user -> users.put(user.get_id(), user));
	}

	public boolean delete(User user)
	{
		this.kitchenManager.getConnection().deleteUser(user.get_id());
		return true;
	}

	public User get(String userId)
	{
		return this.kitchenManager.getLocalDataStore().getUsers().get(userId);
	}

	public void update(User user)
	{
		this.kitchenManager.getConnection().updateUser(user.get_id(), JsonTranslator.toJson(user));
	}
}
