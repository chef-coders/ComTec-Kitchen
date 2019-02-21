package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;

public class KitchenFragment extends Fragment
{

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity != null) {
			changeToolbar(mainActivity.getToolbar().getMenu());
		}
	}

	public void changeToolbar(Menu menu)
	{
		if (menu.size() <= 0) {
			return;
		}
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i)
					.setVisible(false);
		}

		showToolbarMenu(menu);
	}

	protected void showToolbarMenu(Menu menu)
	{

	}
}
