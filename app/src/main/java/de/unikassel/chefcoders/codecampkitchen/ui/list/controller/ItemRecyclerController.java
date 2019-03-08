package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.EditItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class ItemRecyclerController extends GroupedRecyclerController<Item, RowViewHolder>
{
	@Override
	public void reload()
	{
		this.fill(MainActivity.kitchenManager.items().getGrouped());
	}

	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.items().refreshAll();
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int itemIndex)
	{
		Populator.populateItemList(v, this.get(section, itemIndex));
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
			MainActivity mainActivity = (MainActivity) MainActivity.getActivity(itemView);
			if (mainActivity != null)
			{
				EditItemFragment editItemFragment = EditItemFragment.newInstance(item.get_id());
				mainActivity.changeFragmentForward(editItemFragment);
			}
		});
		return true;
	}

	@Override
	public boolean onSwiped(RowViewHolder viewHolder, int section, int itemIndex)
	{
		if (MainActivity.editMode)
		{
			final Item clickedItem = this.get(section, itemIndex);
			MainActivity.kitchenManager.items().delete(clickedItem);
			return true;
		}
		else
		{
			MainActivity.kitchenManager.cart().remove(this.get(section, itemIndex));
			return false;
		}
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
