package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Items;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

import java.util.List;

public class ShoppingCartRecyclerController implements RecyclerController<RowViewHolder>
{
	private List<Purchase> shoppingCart;

	@Override
	public int getSections()
	{
		return this.shoppingCart != null && !this.shoppingCart.isEmpty() ? 1 : 0;
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
	public void reload()
	{
		this.shoppingCart = Cart.shared.getPurchases();
	}

	@Override
	public void refresh()
	{
		Cart.shared.refreshAll();
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		Populator.populatePurchaseHistory(v, this.shoppingCart.get(itemIndex));
	}

	@Override
	public boolean onClick(RowInfo<RowViewHolder> row)
	{
		final Purchase purchase = this.shoppingCart.get(row.getRowIndex());
		final Item item = Items.shared.get(purchase.getItem_id());
		// TODO add(Purchase, int)
		return item == null || Cart.shared.add(item) > 0;
	}

	@Override
	public boolean onSwiped(RowInfo<RowViewHolder> row)
	{
		final Purchase purchase = this.shoppingCart.get(row.getRowIndex());
		final Item item = Items.shared.get(purchase.getItem_id());
		// TODO remove(Purchase)
		return item != null && Cart.shared.remove(item);
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
