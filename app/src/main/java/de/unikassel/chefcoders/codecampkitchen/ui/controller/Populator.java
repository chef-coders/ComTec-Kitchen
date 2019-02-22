package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

class Populator
{
	// --------------- Items ---------------

	static void populateItemList(RowViewHolder v, Item item)
	{
		final int numInCart = MainActivity.kitchenManager.cart().getAmount(item);
		final double price = item.getPrice();

		populateItem(v, item.getName(), numInCart, price, numInCart * price);
		populateAvailable(v, item.getAmount());
	}

	private static void populateItem(RowViewHolder v, String name, int amount, double price, double total)
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

	private static void populateAvailable(RowViewHolder v, int numAvailable)
	{
		if (numAvailable != 0)
		{
			String amountAvailable = v.itemView.getContext().getString(R.string.item_amount_available, numAvailable);
			v.subTitleTextView.setText(amountAvailable);
		}
		else
		{
			v.subTitleTextView.setText(R.string.item_amount_not_available);
		}
	}

	// --------------- Purchases ---------------

	static void populatePurchaseCart(RowViewHolder v, Purchase purchase)
	{
		populatePurchase(v, purchase);

		final Item item = MainActivity.kitchenManager.items().get(purchase.getItem_id());
		populateAvailable(v, item != null ? item.getAmount() : 0);
	}

	static void populatePurchaseHistory(RowViewHolder v, Purchase purchase)
	{
		populatePurchase(v, purchase);

		final String created = purchase.getCreated();
		if (created != null && created.length() >= 11)
		{
			v.subTitleTextView.setText(created.substring(11));
		}
		else
		{
			v.subTitleTextView.setText("");
		}
	}

	private static void populatePurchase(RowViewHolder v, Purchase purchase)
	{
		final String itemId = purchase.getItem_id();
		final Item item = MainActivity.kitchenManager.items().get(itemId);
		final int amount = purchase.getAmount();
		final double total = purchase.getPrice();
		final String name = item != null ? item.getName() : itemId;

		populateItem(v, name, amount, total / amount, total);
	}

	// --------------- Users ---------------

	static void populateUser(RowViewHolder v, User user)
	{
		v.titleTextView.setText(user.getName());
		v.subTitleTextView.setText(user.getMail());
		v.topRightView.setText(v.itemView.getContext().getString(R.string.item_price, user.getCredit()));
		v.bottomRightTextView.setText(user.getRole());

		final int color = ContextCompat.getColor(v.itemView.getContext(), getColor(user));
		v.titleTextView.setTextColor(color);
	}

	private static int getColor(User user)
	{
		final boolean isLoggedIn = user.get_id()
		                               .equals(MainActivity.kitchenManager.session().getLoggedInUser().get_id());
		final boolean isAdmin = "admin".equals(user.getRole());

		return isLoggedIn ?
			       isAdmin ? android.R.color.holo_orange_dark : android.R.color.holo_green_dark :
			       isAdmin ? android.R.color.holo_red_dark : R.color.colorPrimaryText;
	}
}
