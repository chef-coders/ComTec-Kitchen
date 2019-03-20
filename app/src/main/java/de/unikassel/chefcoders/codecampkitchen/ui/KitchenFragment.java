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
	@StringRes
	private int titleRes;
	private boolean showSaveButton;

	public KitchenFragment(@StringRes int titleRes, boolean showSaveButton)
	{
		this.titleRes = titleRes;
		this.showSaveButton = showSaveButton;
	}

	protected final MainActivity getMainActivity()
	{
		return (MainActivity) this.getActivity();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		this.title = this.getString(this.titleRes);

		if (container != null)
		{
			this.hideKeyboard(container);
		}

		MainActivity mainActivity = (MainActivity) this.getActivity();
		if (mainActivity != null)
		{
			Toolbar toolbar = mainActivity.getToolbar();
			this.changeToolbar(toolbar);
		}
		return null;
	}

	private void hideKeyboard(View view)
	{
		InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		view.clearFocus();
	}

	private void changeToolbar(Toolbar toolbar)
	{
		if (toolbar == null)
		{
			return;
		}

		toolbar.setTitle(this.titleRes);

		this.saveButton = toolbar.findViewById(R.id.saveButton);
		if (this.saveButton != null)
		{
			if (this.showSaveButton)
			{
				this.saveButton.setVisibility(View.VISIBLE);
			}
			else
			{
				this.saveButton.setVisibility(View.GONE);
			}
			this.saveButton.setOnClickListener(this::handleClickedOnSave);
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

	public void updateToolbar(Toolbar toolbar)
	{
	}

	protected void handleClickedOnSave(View view)
	{
	}
}
