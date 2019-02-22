package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import de.unikassel.chefcoders.codecampkitchen.R;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;

public class KitchenFragment extends Fragment
{

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity != null) {
			changeToolbar(mainActivity.getToolbar());
		}
	}

	public void changeToolbar(Toolbar toolbar)
	{
		if (toolbar == null) {
			return;
		}

		if (MainActivity.editMode) {
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorAccent));
		} else {
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorPrimary));
		}

		Menu menu = toolbar.getMenu();
		if (menu.size() <= 0) {
			return;
		}
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i)
					.setVisible(false);
		}

		updateToolbar(toolbar);
	}

	protected void updateToolbar(Toolbar toolbar)
	{

	}
}
