package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

import java.util.List;
import java.util.Map;

public class PurchasesRecyclerController implements RecyclerController<RowViewHolder>
{
	private Purchase[][] purchases;
	private String[]     headers;

	@Override
	public int getSections()
	{
		return this.purchases.length;
	}

	@Override
	public int getItems(int section)
	{
		return this.purchases[section].length;
	}

	@Override
	public String getHeader(int section)
	{
		return this.headers[section];
	}

	@Override
	public void refresh()
	{

		MainActivity.kitchenManager.refreshMyPurchases();

		final Map<String, List<Purchase>> grouped = MainActivity.kitchenManager.getMyGroupedPurchases();
		final int numSections = grouped.size();

		this.purchases = new Purchase[numSections][];
		this.headers = new String[numSections];

		int section = 0;
		for (Map.Entry<String, List<Purchase>> entry : grouped.entrySet())
		{
			this.headers[section] = entry.getKey();
			this.purchases[section++] = entry.getValue().toArray(new Purchase[0]);
		}
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		Populator.populate(v, this.purchases[section][itemIndex]);
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
