package de.unikassel.chefcoders.codecampkitchen.logic;

import de.unikassel.chefcoders.codecampkitchen.communication.KitchenConnection;
import de.unikassel.chefcoders.codecampkitchen.model.JsonTranslator;
import de.unikassel.chefcoders.codecampkitchen.model.User;

import java.util.*;

public class Users
{
	// =============== Static Fields ===============

	public static final Users shared = new Users(KitchenConnection.shared);

	// =============== Fields ===============

	private final KitchenConnection connection;

	private final Map<String, User> users = new HashMap<>();

	// =============== Constructors ===============

	public Users(KitchenConnection connection)
	{
		this.connection = connection;
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
		return StreamHelper.group(this.users.values().stream(), u -> u.getName().substring(0, 1).toUpperCase(),
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
		final String resultJson = this.connection.getAllUsers();
		final List<User> resultUsers = JsonTranslator.toUsers(resultJson);

		// only clear when resultsUsers was successfully parsed!
		this.users.clear();
		resultUsers.forEach(this::updateLocal);
	}

	public void refresh(User user)
	{
		final String resultJson = this.connection.getUser(user.get_id());
		final User resultUser = JsonTranslator.toUser(resultJson);
		this.updateLocal(resultUser);
	}

	public void update(User user)
	{
		final String userJson = JsonTranslator.toJson(user);
		this.connection.updateUser(user.get_id(), userJson);
		this.updateLocal(user);
	}

	public boolean delete(User user)
	{
		this.connection.deleteUser(user.get_id());
		this.deleteLocal(user);
		return true;
	}
}
