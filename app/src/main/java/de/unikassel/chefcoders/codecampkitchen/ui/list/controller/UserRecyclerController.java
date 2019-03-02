package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.EditUserFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class UserRecyclerController extends GroupedRecyclerController<User, RowViewHolder>
{
	@Override
	public void reload()
	{
		this.fill(MainActivity.kitchenManager.users().getGrouped());
	}

	@Override
	public void refresh()
	{
		MainActivity.kitchenManager.users().refreshAll();
	}

	@Override
	public RowViewHolder create(View view)
	{
		return new RowViewHolder(view);
	}

	@Override
	public void populate(RowViewHolder v, int section, int item)
	{
		Populator.populateUser(v, this.get(section, item));
	}

	@Override
	public boolean onClick(RowViewHolder v, int section, int item)
	{
		final User user = this.get(section, item);
		final View itemView = v.itemView;
		itemView.post(() -> {
			final MainActivity mainActivity = (MainActivity) MainActivity.getActivity(itemView);
			if (mainActivity != null)
			{
				mainActivity.changeFragment(EditUserFragment.newInstance(user.get_id()));
			}
		});
		return false;
	}

	@Override
	public boolean onSwiped(RowViewHolder v, int section, int item)
	{
		return MainActivity.kitchenManager.users().delete(this.get(section, item));
	}

	@Override
	public boolean swipeIsSupported()
	{
		return MainActivity.kitchenManager.session().isAdmin();
	}
}
