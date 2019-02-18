package de.unikassel.chefcoders.codecampkitchen;

import android.os.Bundle;

import de.unikassel.chefcoders.codecampkitchen.ui.NavigationDrawerActivity;

public class MainActivity extends NavigationDrawerActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
