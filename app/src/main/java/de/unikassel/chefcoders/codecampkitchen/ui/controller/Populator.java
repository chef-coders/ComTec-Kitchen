package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.content.Context;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

public class Populator
{
	static void populate(RowViewHolder v, Item item)
	{
		final int numInCart = MainActivity.kitchenManager.getCartAmount(item);
		populate(v, item, numInCart);

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

		v.subTitleTextView.setText(purchase.getCreated().substring(11));

		populate(v, item, amount);
	}

	static void populate(RowViewHolder v, Item item, int amount)
	{
		v.titleTextView.setText(item.getName());

		final double price = item.getPrice();
		final double total = amount * price;
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
		v.titleTextView.setText(user.getName() + " (" + user.getRole() + ")");
		v.subTitleTextView.setText(user.getMail());
		v.topRightView.setText(v.itemView.getContext().getString(R.string.item_price, user.getCredit()));
		v.bottomRightTextView.setText(user.getCreated());
	}
}
