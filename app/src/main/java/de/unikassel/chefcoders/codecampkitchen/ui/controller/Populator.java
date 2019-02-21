package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

public class Populator
{

	static void populate(RowViewHolder v, Item item)
	{
		if (item.getName() != null)
		{
			v.titleTextView.setText(item.getName());
		}

		if (item.getPrice() > 0.0)
		{
			v.priceTextView.setText(v.itemView.getContext().getString(R.string.item_price, item.getPrice()));
		}
		else
		{
			v.priceTextView.setText("-");
		}

		if (item.getAmount() != 0)
		{
			String amountAvailable = v.itemView.getContext()
			                                   .getString(R.string.item_amount_available, item.getAmount());
			v.amountTextView.setText(amountAvailable);
		}
		else
		{
			v.amountTextView.setText(R.string.item_amount_not_available);
		}

		final int numInCart = MainActivity.kitchenManager.getCartAmount(item);
		final String numInCartText = v.itemView.getContext().getString(R.string.item_amount, numInCart);
		v.numSelectedTextView.setText(numInCartText);
	}
}
