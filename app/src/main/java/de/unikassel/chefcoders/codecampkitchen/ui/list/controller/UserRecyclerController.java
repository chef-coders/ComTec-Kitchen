package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.logic.Session;
import de.unikassel.chefcoders.codecampkitchen.logic.Users;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.EditUserFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowViewHolder;

public class UserRecyclerController extends GroupedRecyclerController<User, RowViewHolder>
{
	@Override
	public void reload()
	{
		this.fill(Users.shared.getGrouped());
	}

	@Override
	public void refresh()
	{
		Users.shared.refreshAll();
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
	public boolean onClick(RowInfo<RowViewHolder> row)
	{
		final User user = this.get(row);
		final View itemView = row.getViewHolder().itemView;
		itemView.post(() -> {
			final MainActivity mainActivity = (MainActivity) MainActivity.getActivity(itemView);
			if (mainActivity != null)
			{
				mainActivity.changeFragmentForward(EditUserFragment.newInstance(user.get_id()));
			}
		});
		return false;
	}

	@Override
	public boolean onSwiped(RowInfo<RowViewHolder> row)
	{
		return Users.shared.delete(this.get(row));
	}

	@Override
	public boolean swipeIsSupported()
	{
		return Session.shared.isAdmin();
	}
}
