package de.unikassel.chefcoders.codecampkitchen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import de.unikassel.chefcoders.codecampkitchen.ui.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.MyPurchasesFragment;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null){
            this.initFragment();
        }

        this.initToolbar();
        this.initNavDrawer();
    }

    private void initFragment(){
        changeFragment(new AllItemsFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate(R.menu.toolbar_view, menu);
        return true;
    }

    private void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
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

        checkAllItemsMenuItem(true);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                    {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_all_items:
                                changeFragment(new AllItemsFragment());
                                break;
                            case R.id.nav_my_purcheses:
                                changeFragment(new MyPurchasesFragment());
                                break;
                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

                        return true;
                    }
                }
        );
    }


    private void changeFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.headlines_fragment, fragment)
                .commit();
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
                // TODO - Open fragment/activity to scan code
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
