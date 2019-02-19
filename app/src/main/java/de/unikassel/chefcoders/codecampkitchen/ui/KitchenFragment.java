package de.unikassel.chefcoders.codecampkitchen.ui;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import de.unikassel.chefcoders.codecampkitchen.R;

public class KitchenFragment extends Fragment
{

    public void changeToolbar(Menu menu)
    {
        if (menu.size() <= 0) {
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i)
                    .setVisible(false);
        }

        showToolbarMenu(menu);
    }

    protected void showToolbarMenu(Menu menu)
    {

    }
}
