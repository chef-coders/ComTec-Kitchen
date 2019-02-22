package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.content.Context;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

class Populator
{
	static void populate(RowViewHolder v, Item item)
	{
		final int numInCart = MainActivity.kitchenManager.getCartAmount(item);
		final double price = item.getPrice();
		populate(v, item.getName(), numInCart, price, numInCart * price);

		if (item.getAmount() != 0)
		{
			String amountAvailable = v.itemView.getContext()
			                                   .getString(R.string.item_amount_available, item.getAmount());
			v.subTitleTextView.setText(amountAvailable);
		}
		else
		{
			v.subTitleTextView.setText(R.string.item_amount_not_available);
		}
	}

	static void populate(RowViewHolder v, Purchase purchase)
	{
		String itemId = purchase.getItem_id();
		Item item = MainActivity.kitchenManager.getItemById(itemId);
		final int amount = purchase.getAmount();
		final double total = purchase.getPrice();
		final String name = item != null ? item.getName() : itemId;

		populate(v, name, amount, total / amount, total);
		v.subTitleTextView.setText(purchase.getCreated().substring(11));
	}

	static void populate(RowViewHolder v, String name, int amount, double price, double total)
	{
		v.titleTextView.setText(name);

		final Context context = v.itemView.getContext();

		switch (amount)
		{
		case 0:
			v.topRightView.setText(context.getString(R.string.item_price, price));
			v.bottomRightTextView.setText("");
			break;
		case 1:
			v.topRightView.setText(context.getString(R.string.item_amount_price, amount, price));
			v.bottomRightTextView.setText("");
			break;
		default:
			v.topRightView.setText(context.getString(R.string.item_amount_price, amount, price));
			v.bottomRightTextView.setText(context.getString(R.string.item_price, total));
			break;
		}
	}

	static void populate(RowViewHolder v, User user)
	{
		v.titleTextView.setText(user.getName());
		v.subTitleTextView.setText(user.getMail());
		v.topRightView.setText(v.itemView.getContext().getString(R.string.item_price, user.getCredit()));
		v.bottomRightTextView.setText(user.getRole());

		final int color = v.itemView.getContext().getResources().getColor(getColor(user));
		v.titleTextView.setTextColor(color);
	}

	private static int getColor(User user)
	{
		final boolean isLoggedIn = user.get_id().equals(MainActivity.kitchenManager.getLoggedInUser().get_id());
		final boolean isAdmin = "admin".equals(user.getRole());

		return isLoggedIn ?
			       isAdmin ? android.R.color.holo_orange_dark : android.R.color.holo_green_dark :
			       isAdmin ? android.R.color.holo_red_dark : android.R.color.black;
	}
}
