package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class PurchasesRecyclerController extends GroupedRecyclerController<Purchase, RowViewHolder>
{
	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.purchases().refreshMine();
		this.fill(MainActivity.kitchenManager.purchases().getMineGrouped());
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		Populator.populatePurchaseHistory(v, this.get(section, itemIndex));
	}

	@Override
	public boolean onClick(RowViewHolder v, int section, int item)
	{
		return false;
	}

	@Override
	public boolean onSwiped(RowViewHolder v, int section, int item)
	{
		return false;
	}

	@Override
	public boolean swipeIsSupported()
	{
		return false;
	}
}
