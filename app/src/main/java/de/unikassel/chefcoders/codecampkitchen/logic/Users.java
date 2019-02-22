package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

import java.util.*;

public class Users
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private final Map<String, User> users = new HashMap<>();

	// =============== Constructors ===============

	public Users(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	// --------------- Access ---------------

	public User get(String id)
	{
		return this.users.get(id);
	}

	public List<User> getAll()
	{
		return new ArrayList<>(this.users.values());
	}

	public Map<String, List<User>> getGrouped()
	{
		return KitchenManager.group(this.users.values().stream(), u -> u.getName().substring(0, 1).toUpperCase(),
		                            Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER));
	}

	// --------------- Modification ---------------

	public void updateLocal(User user)
	{
		this.users.put(user.get_id(), user);
	}

	public void deleteLocal(User user)
	{
		this.users.remove(user.get_id());
	}

	// --------------- Communication ---------------

	public void refreshAll()
	{
		this.users.clear();
		final String resultJson = this.kitchenManager.getConnection().getAllUsers();
		final List<User> resultUsers = JsonTranslator.toUsers(resultJson);
		resultUsers.forEach(this::updateLocal);
	}

	public void update(User user)
	{
		final String userJson = JsonTranslator.toJson(user);
		this.kitchenManager.getConnection().updateUser(user.get_id(), userJson);
		this.updateLocal(user);
	}

	public boolean delete(User user)
	{
		this.kitchenManager.getConnection().deleteUser(user.get_id());
		this.deleteLocal(user);
		return true;
	}
}
