package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

public class UserRecyclerController extends GroupedRecyclerController<User, RowViewHolder>
{
	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.refreshAllUsers();
		this.fill(MainActivity.kitchenManager.getGroupedUsers());
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int item)
	{
		Populator.populate(v, this.get(section, item));
	}

	@Override
	public boolean onClick(RowViewHolder v, int section, int item)
	{
		return false;
	}

	@Override
	public boolean onSwiped(RowViewHolder v, int section, int item)
	{
		return MainActivity.kitchenManager.deleteUser(this.get(section, item));
	}

	@Override
	public boolean swipeIsSupported()
	{
		return MainActivity.kitchenManager.isAdmin();
	}
}
