package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public class KitchenFragment extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		MainActivity mainActivity = (MainActivity) this.getActivity();
		if (mainActivity != null)
		{
			this.changeToolbar(mainActivity.getToolbar());
		}
		return null;
	}

	public void changeToolbar(Toolbar toolbar)
	{
		if (toolbar == null)
		{
			return;
		}

		if (MainActivity.editMode)
		{
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorAccent));
		}
		else
		{
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorPrimary));
		}

		Menu menu = toolbar.getMenu();
		if (menu.size() <= 0)
		{
			return;
		}
		for (int i = 0; i < menu.size(); i++)
		{
			menu.getItem(i).setVisible(false);
		}

		this.updateToolbar(toolbar);
	}

	protected void updateToolbar(Toolbar toolbar)
	{

	}
}
