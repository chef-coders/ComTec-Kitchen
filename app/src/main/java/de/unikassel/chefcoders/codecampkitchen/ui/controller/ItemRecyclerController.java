package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.content.Intent;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.EditItemActivity;
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
		Populator.populate(v, this.items[section][itemIndex]);
	}

	@Override
	public boolean onClick(int section, int itemIndex)
	{
		if (!MainActivity.editMode) {
			final Item item = this.items[section][itemIndex];
			MainActivity.kitchenManager.addToCart(item);
			return true;
		} else {
			final Item item = this.items[section][itemIndex];
			Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
			intent.putExtra("itemId", item.get_id());
			startActivity(intent);
			return true;
		}
	}

	@Override
	public boolean onSwiped(int section, int itemIndex)
	{
		final Item item = this.items[section][itemIndex];
		MainActivity.kitchenManager.removeFromCart(item);
		return true;
	}
}
