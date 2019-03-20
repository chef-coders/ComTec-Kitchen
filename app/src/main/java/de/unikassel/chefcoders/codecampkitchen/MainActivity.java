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
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Session;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.LoginActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.SettingsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.StatisticsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
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
	// =============== Fields ===============

	private Toolbar        toolbar;
	private DrawerLayout   drawerLayout;
	private NavigationView navigationView;

	private boolean editMode;

	// =============== Properties ===============

	public boolean isEditMode()
	{
		return this.editMode;
	}

	public void setEditMode(boolean editMode)
	{
		if (this.editMode == editMode)
		{
			return;
		}

		this.editMode = editMode;

		final Toolbar toolbar = this.getToolbar();
		toolbar.setBackgroundColor(ContextCompat.getColor(this, editMode ? R.color.colorAccent : R.color.colorPrimary));
		this.getKitchenFragment().updateToolbar(toolbar);
	}

	public KitchenFragment getKitchenFragment()
	{
		return (KitchenFragment) this.getSupportFragmentManager().getFragments().stream()
		                             .filter(f -> f instanceof KitchenFragment).findFirst().orElse(null);
	}

	// =============== Methods ===============

	// --------------- Initialization ---------------

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.setTheme();

		super.onCreate(savedInstanceState);

		ResultAsyncTask.execute(() -> {
			return Session.shared.tryLogin(this);
		}, loginSuccess -> {
			if (!loginSuccess)
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
		}, exception -> {
			this.startLogin();
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
		if (this.getIntent().hasExtra("settings"))
		{
			this.selectMenuItem(R.id.nav_settings);
		}
		else if (this.getIntent().hasExtra("barcode"))
		{
			this.changeFragmentForward(PurchaseItemFragment.newInstance(this.getIntent().getStringExtra("barcode")));
		}
		else if (this.getIntent().hasExtra("barcodeCreate"))
		{
			this.changeFragmentForward(
				CreateItemFragment.newInstance(this.getIntent().getStringExtra("barcodeCreate")));
		}
		else
		{
			this.selectMenuItem(R.id.nav_all_items);
		}
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

	// --------------- Toolbar ---------------

	public Toolbar getToolbar()
	{
		return this.toolbar;
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
			this.changeFragmentForward(new CreateItemFragment());
			return true;
		case R.id.action_edit:
			this.setEditMode(!this.editMode);
			return true;
		case R.id.action_clear_all:
			Cart.shared.clear();
			this.changeFragmentBack();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// --------------- Navigation Drawer ---------------

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
		item.setVisible(Session.shared.isAdmin());

		this.selectMenuItem(R.id.nav_all_items);

		this.navigationView.setNavigationItemSelectedListener(this::selectMenuItem);
	}

	private void updatedDrawerHeader()
	{
		User user = Session.shared.getLoggedInUser();

		View headerView = this.navigationView.getHeaderView(0);
		TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
		TextView textViewCredit = headerView.findViewById(R.id.textViewCredit);
		TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);
		ImageButton buttonEditUser = headerView.findViewById(R.id.buttonEditUser);

		if (user != null && "admin".equals(user.getRole()))
		{
			buttonEditUser.setOnClickListener((v) -> {
				this.setEditMode(false);
				this.drawerLayout.closeDrawers();
				this.changeFragmentForward(EditUserFragment.newInstance(user.get_id()));
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

	private void selectMenuItem(@IdRes int menuItemRes)
	{
		this.selectMenuItem(this.navigationView.getMenu().findItem(menuItemRes));
	}

	private boolean selectMenuItem(MenuItem menuItem)
	{
		this.setEditMode(false);
		switch (menuItem.getItemId())
		{
		case R.id.nav_all_items:
			this.selectFragment(new AllItemsFragment());
			break;
		case R.id.nav_my_purcheses:
			this.selectFragment(new MyPurchasesFragment());
			break;
		case R.id.nav_all_users:
			this.selectFragment(new AllUserFragment());
			break;
		case R.id.nav_statistics:
			this.selectFragment(new StatisticsFragment());
			break;
		case R.id.nav_settings:
			this.selectFragment(new SettingsFragment());
			break;
		case R.id.nav_clear_user_data:
			Session.shared.clearUserData(this);
			this.startLogin();
			break;
		}

		menuItem.setChecked(true);
		this.drawerLayout.closeDrawers();

		return true;
	}

	private void selectFragment(KitchenFragment fragment)
	{
		this.getSupportFragmentManager().beginTransaction().replace(R.id.headlines_fragment, fragment).commit();
	}

	// --------------- Navigation ---------------

	public void changeFragmentForward(KitchenFragment fragment)
	{
		this.getSupportFragmentManager().beginTransaction()
		    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
		                         R.anim.slide_out_right).replace(R.id.headlines_fragment, fragment)
		    .addToBackStack(fragment.getTag()).commit();
	}

	public void changeFragmentBack()
	{
		this.onBackPressed();
	}

	@Override
	public void onBackPressed()
	{
		if (this.drawerLayout.isDrawerOpen(GravityCompat.START))
		{
			this.drawerLayout.closeDrawers();
		}
		else
		{
			super.onBackPressed();
		}
	}

	// =============== Static Methods ===============

	// --------------- Helper Methods ---------------

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
