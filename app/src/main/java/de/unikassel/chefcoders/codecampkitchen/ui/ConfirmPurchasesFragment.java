package de.unikassel.chefcoders.codecampkitchen.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import de.unikassel.chefcoders.codecampkitchen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPurchasesFragment extends KitchenFragment
{

    public ConfirmPurchasesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_confirm_purchases, container, false);

        return view;
    }

}
