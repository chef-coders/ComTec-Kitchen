package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

import java.util.*;

public class UsersManager
{
	// =============== Fields ===============

	private final KitchenManager kitchenManager;

	private final Map<String, User> users = new HashMap<>();

	// =============== Constructors ===============

	public UsersManager(KitchenManager kitchenManager)
	{
		this.kitchenManager = kitchenManager;
	}

	// =============== Methods ===============

	public User get(String id)
	{
		return this.users.get(id);
	}

	public void updateLocal(User user)
	{
		this.users.put(user.get_id(), user);
	}

	public List<User> getAll()
	{
		return new ArrayList<>(this.users.values());
	}

	public Map<String, List<User>> getGrouped()
	{
		return KitchenManager.group(this.users.values(), u -> u.getName().substring(0, 1).toUpperCase(),
		                            Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER));
	}

	// --------------- Involving Communication ---------------

	public void refreshAll()
	{
		this.users.clear();
		JsonTranslator.toUsers(this.kitchenManager.getConnection().getAllUsers()).forEach(this::updateLocal);
	}

	public void update(User user)
	{
		this.kitchenManager.getConnection().updateUser(user.get_id(), JsonTranslator.toJson(user));
		this.updateLocal(user);
	}

	public boolean delete(User user)
	{
		this.kitchenManager.getConnection().deleteUser(user.get_id());
		this.users.remove(user.get_id());
		return true;
	}
}
