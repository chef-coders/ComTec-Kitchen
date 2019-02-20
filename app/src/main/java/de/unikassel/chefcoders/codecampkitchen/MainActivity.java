package de.unikassel.chefcoders.codecampkitchen;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.User;
import de.unikassel.chefcoders.codecampkitchen.ui.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.LoginActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.MyPurchasesFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.BarcodeScannerActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.CreateItemActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public static KitchenManager kitchenManager
            = KitchenManager.create();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ResultAsyncTask.exeResultAsyncTask(() -> kitchenManager.tryLogin(this), (isLoggedIn) ->
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
        });

    }

    private void startLogin()
    {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initFragment()
    {
        AllItemsFragment fragment = new AllItemsFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        fragment.changeToolbar(toolbar.getMenu());
        transaction.replace(R.id.headlines_fragment, fragment);
        transaction.commit();

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



        Menu menu = toolbar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i)
                    .setVisible(false);
        }
        ResultAsyncTask.exeResultAsyncTask(() -> MainActivity.kitchenManager.isAdmin(), (value) ->
                menu.findItem(R.id.action_create).setVisible(value)
        );

    }

    private void initNavDrawer()
    {
        this.drawerLayout = this.findViewById(R.id.main_drawer_layout);

        navigationView = this.findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);

        User user = kitchenManager.getLoggedInUser();

        if (user != null) {
            if (user.getRole().equals("admin")) {
                textViewUsername.setText(user.getName()+" (Admin)");
            } else {
                textViewUsername.setText(user.getName());
            }
        }

        checkAllItemsMenuItem(true);

        navigationView.setNavigationItemSelectedListener(
                menuItem ->
                {
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
                        case R.id.nav_clear_user_data:
                            kitchenManager.clearUserData(MainActivity.this);
                            startLogin();
                            break;
                    }

                    return true;
                }
        );
    }


    public void changeFragment(KitchenFragment fragment)
    {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        fragment.changeToolbar(toolbar.getMenu());

        if (fragment instanceof AllItemsFragment) {
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
        } else {
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        }

        transaction.replace(R.id.headlines_fragment, fragment);

        transaction.commit();
    }

    public void checkAllItemsMenuItem(boolean check)
    {
        Menu menuNav = navigationView.getMenu();
        MenuItem item = menuNav.findItem(R.id.nav_all_items);
        item.setChecked(check);
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().findFragmentById(R.id.headlines_fragment)
                instanceof AllItemsFragment) {
            super.onBackPressed();
        } else {
            changeFragment(new AllItemsFragment());
            checkAllItemsMenuItem(true);
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
                startActivity(new Intent(MainActivity.this, BarcodeScannerActivity.class));
                return true;
            case R.id.action_create:
                startActivity(new Intent(MainActivity.this, CreateItemActivity.class));
                return true;
            case R.id.action_clear_all:
                kitchenManager.clearCart();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
