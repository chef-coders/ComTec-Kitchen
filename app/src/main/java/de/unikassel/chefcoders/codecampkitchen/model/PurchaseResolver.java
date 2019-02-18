package de.unikassel.chefcoders.codecampkitchen.model;

import java.util.Objects;

public class PurchaseResolver
{
	public static void resolve(Database database, Purchase purchase)
	{
		resolveUser(database, purchase);
		resolveItem(database, purchase);
	}

	public static void resolveUser(Database database, Purchase purchase)
	{
		final String user_id = purchase.getUser_id();
		final User user = purchase.getUser();

		if (user_id == null && user == null)
		{
			throw new IllegalStateException("user_id and user.id are both null");
		}

		if (user == null)
		{
			final User databaseUser = database.getUser(user_id);
			if (databaseUser == null)
			{
				throw new IllegalStateException("unknown user_id '" + user_id + "'");
			}
			purchase.setUser(databaseUser);
			return;
		}

		if (user_id == null)
		{
			purchase.setUser_id(user.get_id());
			return;
		}

		if (!Objects.equals(user_id, user.get_id()))
		{
			throw new IllegalStateException("mismatching user_id '" + user_id + "' and user.id '" + user.get_id() + "'");
		}
	}

	public static void resolveItem(Database database, Purchase purchase)
	{
		final String item_id = purchase.getItem_id();
		final Item item = purchase.getItem();

		if (item_id == null && item == null)
		{
			throw new IllegalStateException("item_id and item.id are both null");
		}

		if (item == null)
		{
			final Item databaseItem = database.getItem(item_id);
			if (databaseItem == null)
			{
				throw new IllegalStateException("unknown item_id '" + item_id + "'");
			}
			purchase.setItem(databaseItem);
			return;
		}

		if (item_id == null)
		{
			purchase.setItem_id(item.get_id());
			return;
		}

		if (!Objects.equals(item_id, item.get_id()))
		{
			throw new IllegalStateException("mismatching item_id '" + item_id + "' and item.id '" + item.get_id() + "'");
		}
	}
}
