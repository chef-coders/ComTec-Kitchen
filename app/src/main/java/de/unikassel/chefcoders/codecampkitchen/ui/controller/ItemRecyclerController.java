package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

import java.util.List;
import java.util.Map;

public class ItemRecyclerController implements RecyclerController<RowViewHolder>
{
	private Item[][] items;
	private String[] headers;

	@Override
	public int getSections()
	{
		return this.items.length;
	}

	@Override
	public int getItems(int section)
	{
		return this.items[section].length;
	}

	@Override
	public String getHeader(int section)
	{
		return this.headers[section];
	}

	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.refreshItems();

		final Map<String, List<Item>> grouped = MainActivity.kitchenManager.getGroupedItems();
		final int numSections = grouped.size();

		this.items = new Item[numSections][];
		this.headers = new String[numSections];

		int section = 0;
		for (Map.Entry<String, List<Item>> entry : grouped.entrySet())
		{
			this.headers[section] = entry.getKey();
			this.items[section++] = entry.getValue().toArray(new Item[0]);
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
		final Item item = this.items[section][itemIndex];

		if (item.getName() != null)
		{
			v.titleTextView.setText(item.getName());
		}

		if (item.getPrice() > 0.0)
		{
			v.prizeTextView.setText(v.itemView.getContext().getString(R.string.item_price, item.getPrice()));
		}
		else
		{
			v.prizeTextView.setText("-");
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

		// TODO - Add numberSelected to data model
		v.numSelectedTextView.setText("x3");
	}

	@Override
	public void onClick(int section, int item)
	{
	}
}
