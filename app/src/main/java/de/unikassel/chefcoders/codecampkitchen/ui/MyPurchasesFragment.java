package de.unikassel.chefcoders.codecampkitchen.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.PurchasesRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.GeneralRecyclerView;
import de.unikassel.chefcoders.codecampkitchen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchasesFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
{

    private ProgressBar progressBar;

    public MyPurchasesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_purchases, container, false);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.initRecyclerView(view);
        return view;
    }


    private void initRecyclerView(View view)
    {
        new GeneralRecyclerView(view.findViewById(R.id.allItemsRecView),
                new PurchasesRecyclerController(),
                view.findViewById(R.id.allItemsSwipeRefreshLayout),
                this);
    }

    // --- --- --- Handle user interactions --- --- ---
    @Override
    public void handleRecViewLoadFinished()
    {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void handleRecViewScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy)
    {

    }

    @Override
    public void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy)
    {
    }

    @Override
    public void handleRecViewItemTouched(View view, int position)
    {
        // TODO - Handle touch
    }

}
