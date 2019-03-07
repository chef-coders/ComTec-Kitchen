package de.unikassel.chefcoders.codecampkitchen.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public abstract class KitchenFragment extends Fragment
{
	protected Button saveButton;

	protected String title;
	private @StringRes int titleRes;
	private boolean showSaveButton;

	public KitchenFragment(@StringRes int titleRes, boolean showSaveButton)
	{
		this.titleRes = titleRes;
		this.showSaveButton = showSaveButton;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		MainActivity mainActivity = (MainActivity) this.getActivity();
		if (mainActivity != null)
		{
			Toolbar toolbar = mainActivity.getToolbar();
			this.changeToolbar(toolbar);
		}
		return null;
	}

	private void changeToolbar(Toolbar toolbar)
	{
		if (toolbar == null)
		{
			return;
		}

		toolbar.setTitle(titleRes);

		if (MainActivity.editMode)
		{
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorAccent));
		}
		else
		{
			toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorPrimary));
		}

		this.saveButton = toolbar.findViewById(R.id.saveButton);
		if(saveButton != null)
		{
			if(showSaveButton)
			{
				saveButton.setVisibility(View.VISIBLE);
			}
			else
			{
				saveButton.setVisibility(View.GONE);
			}
			saveButton.setOnClickListener(this::handleClickedOnSave);
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

	protected void updateToolbar(Toolbar toolbar) {}

	protected void handleClickedOnSave(View view) {}
}
