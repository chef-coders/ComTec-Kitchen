package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RowViewHolder;

import java.util.List;

public class UserRecyclerController implements RecyclerController<RowViewHolder>
{
	private List<User> users;

	@Override
	public int getSections()
	{
		return 1;
	}

	@Override
	public int getItems(int sections)
	{
		return this.users.size();
	}

	@Override
	public String getHeader(int section)
	{
		return "Users";
	}

	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.refreshAllUsers();
		this.users = MainActivity.kitchenManager.getAllUsers();
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int item)
	{
		Populator.populate(v, this.users.get(item));
	}

	@Override
	public boolean onClick(int section, int item)
	{
		return false;
	}

	@Override
	public boolean onSwiped(int section, int item)
	{
		return false;
	}

	@Override
	public boolean swipeIsSupported()
	{
		return false;
	}
}
