package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class PurchasesRecyclerController extends GroupedRecyclerController<Purchase, RowViewHolder>
{
	@Override
	public void reload()
	{
		this.fill(MainActivity.kitchenManager.purchases().getMineGrouped());
	}

	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.purchases().refreshMine();
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
	public boolean onClick(RowInfo<RowViewHolder> row)
	{
		return false;
	}

	@Override
	public boolean onSwiped(RowInfo<RowViewHolder> row)
	{
		return false;
	}

	@Override
	public boolean swipeIsSupported()
	{
		return false;
	}
}
