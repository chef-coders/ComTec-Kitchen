package de.unikassel.chefcoders.codecampkitchen.model;

import com.google.gson.Gson;

public class JsonTranslator
{
	// =============== Static Fields ===============

	private static final Gson gson = new Gson();

	static
	{
		// Gson adapters...
	}

	// =============== Static Methods ===============

	// @formatter:off
	public static String toJson(User user) { return gson.toJson(user); }
	public static User toUser(String json) { return gson.fromJson(json, User.class); }

	public static String toJson(Item item) { return gson.toJson(item); }
	public static Item toItem(String json) { return gson.fromJson(json, Item.class); }

	public static String toJson(Purchase purchase) { return gson.toJson(purchase); }
	public static Purchase toPurchase(String json) { return gson.fromJson(json, Purchase.class); }
	// @formatter:on
}
