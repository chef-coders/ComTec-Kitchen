package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends KitchenFragment
{

	Switch switchDarkMode;

	public SettingsFragment()
	{
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_settings, container, false);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		this.switchDarkMode = view.findViewById(R.id.switchDarkMode);

		this.switchDarkMode.setChecked(sharedPreferences.getBoolean("darkMode", false));
		this.switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				sharedPreferences.edit().putBoolean("darkMode", isChecked).commit();
				SettingsFragment.this.restart();
			}
		});

		return view;
	}

	private void restart()
	{
		if (this.getActivity() == null)
		{
			return;
		}
		this.getActivity().finish();
		Intent intent = new Intent(this.getActivity(), MainActivity.class);
		intent.putExtra("settings", true);
		this.startActivity(intent);
	}

	@Override
	public void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.settings);
	}
}
