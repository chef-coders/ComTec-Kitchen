package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

public class PurchasesRecyclerController extends GroupedRecyclerController<Purchase, RowViewHolder>
{
	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.refreshMyPurchases();
		this.fill(MainActivity.kitchenManager.getMyGroupedPurchases());
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		Populator.populate(v, this.get(section, itemIndex));
	}

	@Override
	public boolean onClick(int section, int item)
	{
		return false;
	}

	@Override
	public boolean onSwiped(int section, int item)
	{
		// TODO - Handle swipe
		return true;
	}
}
