package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

import java.util.List;

public class ShoppingCartRecyclerController implements RecyclerController<RowViewHolder>
{
	private List<Purchase> shoppingCart;

	@Override
	public int getSections()
	{
		return 1;
	}

	@Override
	public int getItems(int section)
	{
		return this.shoppingCart.size();
	}

	@Override
	public String getHeader(int section)
	{
		return "";
	}

	@Override
	public void refresh()
	{
		this.shoppingCart = MainActivity.kitchenManager.cart().getPurchases();
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		final Purchase purchase = this.shoppingCart.get(itemIndex);
		final Item item = MainActivity.kitchenManager.getItemById(purchase.getItem_id());

		Populator.populateItemList(v, item);
	}

	@Override
	public boolean onClick(RowViewHolder v, int section, int itemIndex)
	{
		final Purchase purchase = this.shoppingCart.get(itemIndex);
		final Item item = MainActivity.kitchenManager.getItemById(purchase.getItem_id());
		MainActivity.kitchenManager.cart().add(item);
		return true;
	}

	@Override
	public boolean onSwiped(RowViewHolder v, int section, int itemIndex)
	{
		final Purchase purchase = this.shoppingCart.get(itemIndex);
		final Item item = MainActivity.kitchenManager.getItemById(purchase.getItem_id());
		MainActivity.kitchenManager.cart().remove(item);
		return false;
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
