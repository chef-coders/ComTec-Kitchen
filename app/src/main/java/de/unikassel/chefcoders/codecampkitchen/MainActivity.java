package de.unikassel.chefcoders.codecampkitchen;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.*;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.BarcodeScannerActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.CreateItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.EditItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.PurchaseItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

import java.util.Arrays;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity
{
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	public static KitchenManager kitchenManager
			= KitchenManager.create();

	public static boolean editMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme();

		super.onCreate(savedInstanceState);

		ResultAsyncTask.execute(
				this.getApplicationContext(),
				() ->
				{
					try {

						return kitchenManager.session().tryLogin(this);
					} catch (Exception ex) {
						return false;
					}
				},
				(isLoggedIn) ->
				{
					if (!isLoggedIn) {
						startLogin();
					} else {
						setContentView(R.layout.activity_main);
						this.initToolbar();
						this.initNavDrawer();
						this.initShortCuts();

						if (savedInstanceState == null) {
							this.initFragment();
						}
					}
				}
		);
	}

	private void setTheme()
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean darkMode = sharedPreferences.getBoolean("darkMode", false);

		if (darkMode) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}

	}

	private void startLogin()
	{
		finish();
		startActivity(new Intent(this, LoginActivity.class));
	}

	private void initFragment()
	{
		if (getIntent().hasExtra("settings")) {
			SettingsFragment fragment = new SettingsFragment();
			fragment.changeToolbar(toolbar);
			checkSettingsMenuItem(true);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.headlines_fragment, fragment);
			transaction.commitAllowingStateLoss();
		} else if (getIntent().hasExtra("barcode")) {
			PurchaseItemFragment fragment = PurchaseItemFragment.newInstance(getIntent().getStringExtra("barcode"));
			fragment.changeToolbar(toolbar);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.headlines_fragment, fragment);
			transaction.commitAllowingStateLoss();
		} else if (getIntent().hasExtra("barcodeCreate")) {
			CreateItemFragment fragment = CreateItemFragment.newInstance(getIntent().getStringExtra("barcodeCreate"));
			fragment.changeToolbar(toolbar);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.headlines_fragment, fragment);
			transaction.commitAllowingStateLoss();
		} else {
			AllItemsFragment fragment = new AllItemsFragment();
			fragment.changeToolbar(toolbar);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.headlines_fragment, fragment);
			transaction.commitAllowingStateLoss();
		}
	}

	private void initShortCuts()
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {

			ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

			Intent intent = new Intent(this, BarcodeScannerActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
					.setShortLabel("Barcode Scanner")
					.setLongLabel("Barcode Scanner")
					.setIcon(Icon.createWithResource(this, android.R.drawable.ic_menu_camera))
					.setIntent(intent)
					.build();

			shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.toolbar_view, menu);
		return true;
	}

	private void initToolbar()
	{
		this.toolbar = findViewById(R.id.main_toolbar);

		toolbar.setTitleTextColor(
				getColor(android.R.color.white)
		);

		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
		}

	}

	private void initNavDrawer()
	{
		this.drawerLayout = this.findViewById(R.id.main_drawer_layout);
		navigationView = this.findViewById(R.id.nav_view);

		drawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
		{
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				updatedDrawerHeader();
			}
		});
		updatedDrawerHeader();

		MenuItem item = navigationView.getMenu().findItem(R.id.nav_all_users);
		item.setVisible(kitchenManager.session().isAdmin());

		checkAllItemsMenuItem(true);

		navigationView.setNavigationItemSelectedListener(
				menuItem ->
				{
					if(MainActivity.editMode)
					{
						this.setEditMode(false);
					}

					switch (menuItem.getItemId()) {
						case R.id.nav_all_items:
							changeFragment(new AllItemsFragment());
							menuItem.setChecked(true);
							drawerLayout.closeDrawers();
							break;
						case R.id.nav_my_purcheses:
							changeFragment(new MyPurchasesFragment());
							menuItem.setChecked(true);
							drawerLayout.closeDrawers();
							break;
						case R.id.nav_all_users:
							changeFragment(new AllUserFragment());
							menuItem.setChecked(true);
							drawerLayout.closeDrawers();
							break;
						case R.id.nav_statistics:
							SimpleAsyncTask.execute(
									this.getApplicationContext(),
									() ->
									{
										if (kitchenManager.session().isAdmin()) {
											MainActivity.kitchenManager.purchases().refreshAll();
										} else {
											MainActivity.kitchenManager.purchases().refreshMine();
										}
									},
									() ->
									{
										changeFragment(new StatisticsFragment());
										menuItem.setChecked(true);
										drawerLayout.closeDrawers();
									}
							);
							break;
						case R.id.nav_settings:
							changeFragment(new SettingsFragment());
							menuItem.setChecked(true);
							drawerLayout.closeDrawers();
							break;
						case R.id.nav_clear_user_data:
							kitchenManager.session().clearUserData(this);
							startLogin();
							break;
					}

					return true;
				}
		);
	}

	private void updatedDrawerHeader()
	{
		User user = kitchenManager.session().getLoggedInUser();

		View headerView = navigationView.getHeaderView(0);
		TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
		TextView textViewCredit = headerView.findViewById(R.id.textViewCredit);
		TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);
		ImageButton buttonEditUser = headerView.findViewById(R.id.buttonEditUser);

		if(user != null && "admin".equals(user.getRole()))
		{
			buttonEditUser.setOnClickListener((v) ->
			{
				if(MainActivity.editMode)
				{
					this.setEditMode(false);
				}
				drawerLayout.closeDrawers();
				changeFragment(EditUserFragment.newInstance(user.get_id()));
			});
		}
		else
		{
			buttonEditUser.setVisibility(View.GONE);
		}

		if (user != null) {
			if (user.getRole().equals("admin")) {
				textViewUsername.setText(String.format("%s (Admin)", user.getName()));
			} else {
				textViewUsername.setText(user.getName());
			}
			textViewEmail.setText(user.getMail());
			textViewCredit.setText(getString(R.string.credit_value, user.getCredit()));
		}
	}


	public void changeFragment(KitchenFragment fragment)
	{
		if (fragment instanceof AllItemsFragment) {
			changeFragmentBack(fragment);
		} else {
			changeFragmentForward(fragment);
		}
	}

	public void changeFragmentForward(KitchenFragment fragment)
	{
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		fragment.changeToolbar(toolbar);

		transaction.setCustomAnimations(
				R.anim.slide_in_right,
				R.anim.slide_out_left
		);

		transaction.replace(R.id.headlines_fragment, fragment);

		transaction.commit();
	}

	public void changeFragmentBack(KitchenFragment fragment)
	{

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		fragment.changeToolbar(toolbar);

		transaction.setCustomAnimations(
				R.anim.slide_in_left,
				R.anim.slide_out_right
		);

		transaction.replace(R.id.headlines_fragment, fragment);

		transaction.commit();

	}

	public void checkAllItemsMenuItem(boolean check)
	{
		Menu menuNav = navigationView.getMenu();
		MenuItem item = menuNav.findItem(R.id.nav_all_items);
		item.setChecked(check);
	}

	public void checkAllUsersMenuItem(boolean check)
	{
		Menu menuNav = navigationView.getMenu();
		MenuItem item = menuNav.findItem(R.id.nav_all_users);
		item.setChecked(check);
	}

	public void checkSettingsMenuItem(boolean check)
	{
		Menu menuNav = navigationView.getMenu();
		MenuItem item = menuNav.findItem(R.id.nav_settings);
		item.setChecked(check);
	}

	@Override
	public void onBackPressed()
	{
		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawers();
		} else if (!(currentFragment instanceof AllItemsFragment)) {
			changeFragment(new AllItemsFragment());
			checkAllItemsMenuItem(true);
		} else if (MainActivity.editMode) {
			setEditMode(false);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case android.R.id.home:
				this.drawerLayout.openDrawer(GravityCompat.START);
				return true;
			case R.id.action_scan_code:
				finish();
				startActivity(new Intent(MainActivity.this, BarcodeScannerActivity.class));
				return true;
			case R.id.action_create:
				changeFragment(new CreateItemFragment());
				return true;
			case R.id.action_edit:
				setEditMode(!editMode);
				return true;
			case R.id.action_clear_all:
				SimpleAsyncTask.execute(this.getApplicationContext(), () -> kitchenManager.cart().clear(), () ->
				{
				});
				changeFragment(new AllItemsFragment());
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setEditMode(boolean editMode)
	{

		MainActivity.editMode = editMode;

		if (MainActivity.editMode) {
			getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
			getToolbar().setTitle(R.string.edit_items);
		} else {
			getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
			getToolbar().setTitle(R.string.shop);
		}

		getToolbar().getMenu()
				.findItem(R.id.action_scan_code)
				.setVisible(!MainActivity.editMode && isAllItemsFragmentVisible());
		getToolbar().getMenu()
				.findItem(R.id.action_create)
				.setVisible(MainActivity.editMode && isAllItemsFragmentVisible());

		//updateLayout();
	}

	boolean isAllItemsFragmentVisible()
	{
		return getSupportFragmentManager().findFragmentById(R.id.headlines_fragment) instanceof AllItemsFragment;
	}

	public Toolbar getToolbar()
	{
		return toolbar;
	}

	public static Activity getActivity(View view)
	{
		Context context = view.getContext();
		while (context instanceof ContextWrapper) {
			if (context instanceof Activity) {
				return (Activity) context;
			}
			context = ((ContextWrapper) context).getBaseContext();
		}
		return null;
	}
}
