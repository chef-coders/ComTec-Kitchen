package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

public class ItemRecyclerController extends GroupedRecyclerController<Item, RowViewHolder>
{
	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.refreshItems();
		this.fill(MainActivity.kitchenManager.getGroupedItems());
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
	public boolean onClick(int section, int itemIndex)
	{
		if (!MainActivity.editMode)
		{
			return MainActivity.kitchenManager.addToCart(this.get(section, itemIndex)) > 0;
		}
		return false;
	}

	@Override
	public boolean onSwiped(int section, int itemIndex)
	{
		if (MainActivity.editMode)
		{
			final Item clickedItem = MainActivity.kitchenManager.getItem(section, itemIndex);
			MainActivity.kitchenManager.deleteItem(clickedItem.get_id());
			return true;
		}
		else
		{
			return MainActivity.kitchenManager.removeFromCart(this.get(section, itemIndex));
		}
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
