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

	public static <T> String toJson(T object, Class<T> type)
	{
		return gson.toJson(object);
	}

	public static <T> T fromJson(String json, Class<T> type)
	{
		return gson.fromJson(json, type);
	}

	// @formatter:off
	public static String toJson(User user) { return toJson(user, User.class); }
	// public static void toUser(String json, User target) { fromJson(json, target); }
	public static User toUser(String json) { return fromJson(json, User.class); }

	public static String toJson(Item item) { return toJson(item, Item.class); }
	// public static void toItem(String json, Item target) { fromJson(json, target); }
	public static Item toItem(String json) { return fromJson(json, Item.class); }

	public static String toJson(Purchase purchase) { return toJson(purchase, Purchase.class); }
	// public static void toPurchase(String json, Purchase target) { fromJson(json, target); }
	public static Purchase toPurchase(String json) { return fromJson(json, Purchase.class); }
	// @formatter:on
}
