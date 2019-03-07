package de.unikassel.chefcoders.codecampkitchen;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
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
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.LoginActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.SettingsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.StatisticsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.BarcodeScannerActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.PurchaseItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.CreateItemFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.edit.EditUserFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.AllUserFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.MyPurchasesFragment;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
	private Toolbar        toolbar;
	private DrawerLayout   drawerLayout;
	private NavigationView navigationView;

	public static KitchenManager kitchenManager = KitchenManager.create();

	public static boolean editMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.setTheme();

		super.onCreate(savedInstanceState);

		ResultAsyncTask.execute(this.getApplicationContext(), () -> {
			try
			{

				return kitchenManager.session().tryLogin(this);
			}
			catch (Exception ex)
			{
				return false;
			}
		}, (isLoggedIn) -> {
			if (!isLoggedIn)
			{
				this.startLogin();
			}
			else
			{
				setContentView(R.layout.activity_main);
				this.initToolbar();
				this.initNavDrawer();
				this.initShortCuts();

				if (savedInstanceState == null)
				{
					this.initFragment();
				}
			}
		});
	}

	private void setTheme()
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean darkMode = sharedPreferences.getBoolean("darkMode", false);

		if (darkMode)
		{
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}
		else
		{
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
	}

	private void startLogin()
	{
		this.finish();
		this.startActivity(new Intent(this, LoginActivity.class));
	}

	private void initFragment()
	{
		KitchenFragment fragment;

		if (this.getIntent().hasExtra("settings"))
		{
			fragment = new SettingsFragment();
			this.setMenuItem(R.id.nav_settings, true);
		}
		else if (this.getIntent().hasExtra("barcode"))
		{
			fragment = PurchaseItemFragment.newInstance(this.getIntent().getStringExtra("barcode"));
		}
		else if (this.getIntent().hasExtra("barcodeCreate"))
		{
			fragment = CreateItemFragment.newInstance(this.getIntent().getStringExtra("barcodeCreate"));
		}
		else if (this.getIntent().hasExtra("barcodeFailed"))
		{
			fragment = new AllItemsFragment();
			Toast.makeText(this.getApplicationContext(), R.string.item_amount_not_available, Toast.LENGTH_LONG).show();
		}
		else
		{
			fragment = new AllItemsFragment();
		}

		fragment.changeToolbar(this.toolbar);

		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.headlines_fragment, fragment);
		transaction.commitAllowingStateLoss();
	}

	private void initShortCuts()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
		{

			ShortcutManager shortcutManager = this.getSystemService(ShortcutManager.class);

			Intent intent = new Intent(this, BarcodeScannerActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1").setShortLabel("Barcode Scanner")
			                                                             .setLongLabel("Barcode Scanner").setIcon(
					Icon.createWithResource(this, android.R.drawable.ic_menu_camera)).setIntent(intent).build();

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
		this.toolbar = this.findViewById(R.id.main_toolbar);

		this.toolbar.setTitleTextColor(this.getColor(android.R.color.white));

		this.setSupportActionBar(this.toolbar);
		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
		}
	}

	private void initNavDrawer()
	{
		this.drawerLayout = this.findViewById(R.id.main_drawer_layout);
		this.navigationView = this.findViewById(R.id.nav_view);

		this.drawerLayout
			.addDrawerListener(new ActionBarDrawerToggle(this, this.drawerLayout, R.string.open, R.string.close)
			{
				@Override
				public void onDrawerOpened(View drawerView)
				{
					super.onDrawerOpened(drawerView);
					MainActivity.this.updatedDrawerHeader();
				}
			});
		this.updatedDrawerHeader();

		MenuItem item = this.navigationView.getMenu().findItem(R.id.nav_all_users);
		item.setVisible(kitchenManager.session().isAdmin());

		this.setMenuItem(R.id.nav_all_items, true);

		this.navigationView.setNavigationItemSelectedListener(menuItem -> {
			if (MainActivity.editMode)
			{
				this.setEditMode(false);
			}

			switch (menuItem.getItemId())
			{
			case R.id.nav_all_items:
				this.changeFragment(new AllItemsFragment());
				menuItem.setChecked(true);
				this.drawerLayout.closeDrawers();
				break;
			case R.id.nav_my_purcheses:
				this.changeFragment(new MyPurchasesFragment());
				menuItem.setChecked(true);
				this.drawerLayout.closeDrawers();
				break;
			case R.id.nav_all_users:
				this.changeFragment(new AllUserFragment());
				menuItem.setChecked(true);
				this.drawerLayout.closeDrawers();
				break;
			case R.id.nav_statistics:
				this.changeFragment(new StatisticsFragment());
				menuItem.setChecked(true);
				this.drawerLayout.closeDrawers();
				break;
			case R.id.nav_settings:
				this.changeFragment(new SettingsFragment());
				menuItem.setChecked(true);
				this.drawerLayout.closeDrawers();
				break;
			case R.id.nav_clear_user_data:
				kitchenManager.session().clearUserData(this);
				this.startLogin();
				break;
			}

			return true;
		});
	}

	private void updatedDrawerHeader()
	{
		User user = kitchenManager.session().getLoggedInUser();

		View headerView = this.navigationView.getHeaderView(0);
		TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
		TextView textViewCredit = headerView.findViewById(R.id.textViewCredit);
		TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);
		ImageButton buttonEditUser = headerView.findViewById(R.id.buttonEditUser);

		if (user != null && "admin".equals(user.getRole()))
		{
			buttonEditUser.setOnClickListener((v) -> {
				if (MainActivity.editMode)
				{
					this.setEditMode(false);
				}
				this.drawerLayout.closeDrawers();
				this.changeFragment(EditUserFragment.newInstance(user.get_id()));
			});
		}
		else
		{
			buttonEditUser.setVisibility(View.GONE);
		}

		if (user != null)
		{
			if (user.getRole().equals("admin"))
			{
				textViewUsername.setText(String.format("%s (Admin)", user.getName()));
			}
			else
			{
				textViewUsername.setText(user.getName());
			}
			textViewEmail.setText(user.getMail());
			textViewCredit.setText(getString(R.string.credit_value, user.getCredit()));
		}
	}

	public void changeFragment(KitchenFragment fragment)
	{
		if (fragment instanceof AllItemsFragment)
		{
			this.changeFragmentBack(fragment);
		}
		else
		{
			this.changeFragmentForward(fragment);
		}
	}

	public void changeFragmentForward(KitchenFragment fragment)
	{
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

		fragment.changeToolbar(this.toolbar);

		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

		transaction.replace(R.id.headlines_fragment, fragment);

		transaction.commit();
	}

	public void changeFragmentBack(KitchenFragment fragment)
	{

		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

		fragment.changeToolbar(this.toolbar);

		transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

		transaction.replace(R.id.headlines_fragment, fragment);

		transaction.commit();
	}

	public void setMenuItem(@IdRes int menuItemRes, boolean check)
	{
		Menu menuNav = this.navigationView.getMenu();
		MenuItem item = menuNav.findItem(menuItemRes);
		item.setChecked(check);
	}

	@Override
	public void onBackPressed()
	{
		Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.headlines_fragment);
		if (this.drawerLayout.isDrawerOpen(GravityCompat.START))
		{
			this.drawerLayout.closeDrawers();
		}
		else if (!(currentFragment instanceof AllItemsFragment))
		{
			if (currentFragment instanceof EditUserFragment)
			{
				this.changeFragment(new AllUserFragment());
				this.setMenuItem(R.id.nav_all_users, true);
			}
			else
			{
				this.changeFragment(new AllItemsFragment());
				this.setMenuItem(R.id.nav_all_items, true);
			}
		}
		else if (MainActivity.editMode)
		{
			this.setEditMode(false);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			this.drawerLayout.openDrawer(GravityCompat.START);
			return true;
		case R.id.action_scan_code:
			this.finish();
			this.startActivity(new Intent(MainActivity.this, BarcodeScannerActivity.class));
			return true;
		case R.id.action_create:
			this.changeFragment(new CreateItemFragment());
			return true;
		case R.id.action_edit:
			this.setEditMode(!editMode);
			return true;
		case R.id.action_clear_all:
			kitchenManager.cart().clear();
			this.changeFragment(new AllItemsFragment());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setEditMode(boolean editMode)
	{

		MainActivity.editMode = editMode;

		if (MainActivity.editMode)
		{
			this.getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
			this.getToolbar().setTitle(R.string.edit_items);
		}
		else
		{
			this.getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
			this.getToolbar().setTitle(R.string.shop);
		}

		this.getToolbar().getMenu().findItem(R.id.action_scan_code)
		    .setVisible(!MainActivity.editMode && this.isAllItemsFragmentVisible());
		this.getToolbar().getMenu().findItem(R.id.action_create)
		    .setVisible(MainActivity.editMode && this.isAllItemsFragmentVisible());

		//updateLayout();
	}

	boolean isAllItemsFragmentVisible()
	{
		return this.getSupportFragmentManager().findFragmentById(R.id.headlines_fragment) instanceof AllItemsFragment;
	}

	public Toolbar getToolbar()
	{
		return this.toolbar;
	}

	public static Activity getActivity(View view)
	{
		Context context = view.getContext();
		while (context instanceof ContextWrapper)
		{
			if (context instanceof Activity)
			{
				return (Activity) context;
			}
			context = ((ContextWrapper) context).getBaseContext();
		}
		return null;
	}
}
