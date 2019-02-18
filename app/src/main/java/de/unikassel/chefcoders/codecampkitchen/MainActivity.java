package de.unikassel.chefcoders.codecampkitchen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
	DrawerLayout drawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.initToolbar();
		this.initNavDrawer();
	}

	private void initToolbar()
	{
		Toolbar toolbar = findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
	}

	private void initNavDrawer()
	{
		this.drawerLayout = this.findViewById(R.id.main_drawer_layout);

		NavigationView navigationView = this.findViewById(R.id.nav_view);
		Menu menuNav = navigationView.getMenu();
		MenuItem item = menuNav.findItem(R.id.nav_all_items);
		item.setChecked(true);

		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener()
				{
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
					{
						menuItem.setChecked(true);
						drawerLayout.closeDrawers();

						// TODO - Manage swap of fragments

						return true;
					}
				}
		);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				this.drawerLayout.openDrawer(GravityCompat.START);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
