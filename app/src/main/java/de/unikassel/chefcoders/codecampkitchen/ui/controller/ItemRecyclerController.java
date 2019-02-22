package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.EditItemActivity;
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
	public boolean onClick(RowViewHolder v, int section, int itemIndex)
	{
		final Item item = this.get(section, itemIndex);
		if (!MainActivity.editMode)
		{
			return MainActivity.kitchenManager.cart().add(item) > 0;
		}

		final View itemView = v.itemView;
		itemView.post(() -> {
			final Context context = itemView.getContext();
			Intent intent = new Intent(context, EditItemActivity.class);
			intent.putExtra("itemId", item.get_id());
			context.startActivity(intent);
		});
		return true;
	}

	@Override
	public boolean onSwiped(RowViewHolder viewHolder, int section, int itemIndex)
	{
		if (MainActivity.editMode)
		{
			final Item clickedItem = this.get(section, itemIndex);
			MainActivity.kitchenManager.deleteItem(clickedItem.get_id());
			return true;
		}
		else
		{
			return MainActivity.kitchenManager.cart().remove(this.get(section, itemIndex));
		}
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
