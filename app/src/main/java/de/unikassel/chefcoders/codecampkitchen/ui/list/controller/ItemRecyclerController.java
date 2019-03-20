package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Items;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.EditItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class ItemRecyclerController extends GroupedRecyclerController<Item, RowViewHolder>
{
	@Override
	public void reload()
	{
		this.fill(Items.shared.getGrouped());
	}

	@Override
	public void refresh()
	{
		Items.shared.refreshAll();
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
	public boolean onClick(RowInfo<RowViewHolder> row)
	{
		final View itemView = row.getViewHolder().itemView;
		MainActivity mainActivity = (MainActivity) MainActivity.getActivity(itemView);
		final Item item = this.get(row);

		if (!mainActivity.isEditMode())
		{
			return Cart.shared.add(item) > 0;
		}

		itemView.post(() -> mainActivity.changeFragmentForward(EditItemFragment.newInstance(item.get_id())));
		return true;
	}

	@Override
	public boolean onSwiped(RowInfo<RowViewHolder> row)
	{
		final View itemView = row.getViewHolder().itemView;
		MainActivity mainActivity = (MainActivity) MainActivity.getActivity(itemView);
		final Item item = this.get(row);

		if (mainActivity.isEditMode())
		{
			Items.shared.delete(item);
			return true;
		}
		else
		{
			Cart.shared.remove(item);
			return false;
		}
	}

	@Override
	public boolean swipeIsSupported()
	{
		return true;
	}
}
