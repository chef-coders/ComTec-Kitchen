package de.unikassel.chefcoders.codecampkitchen.model;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class JsonTranslator
{
	// =============== Static Fields ===============

	private static final Gson gson = new Gson();

	// =============== Static Methods ===============

	public static <T> String toJson(T object, Class<T> type)
	{
		return gson.toJson(object);
	}

	public static <T> T fromJson(String json, Class<T> type)
	{
		return gson.fromJson(json, type);
	}

	public static <T> List<T> fromJsons(String jsonArray, Class<T[]> type)
	{
		return Arrays.asList(gson.fromJson(jsonArray, type));
	}

	// @formatter:off
	// --------------- Users ---------------
	public static String toJson(User user) { return toJson(user, User.class); }
	public static User toUser(String json) { return fromJson(json, User.class); }
	public static List<User> toUsers(String json) { return fromJsons(json, User[].class); }
	// --------------- Items ---------------
	public static String toJson(Item item) { return toJson(item, Item.class); }
	public static Item toItem(String json) { return fromJson(json, Item.class); }
	public static List<Item> toItems(String json) { return fromJsons(json, Item[].class); }
	// --------------- Purchases ---------------
	public static String toJson(Purchase purchase) { return toJson(purchase, Purchase.class); }
	public static Purchase toPurchase(String json) { return fromJson(json, Purchase.class); }
	public static List<Purchase> toPurchases(String json) { return fromJsons(json, Purchase[].class); }
	// @formatter:on
}
