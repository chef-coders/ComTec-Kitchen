package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.KitchenManager;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemAdapter;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemRecyclerView;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemSection;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.RecyclerTouchListener;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AllItemsFragment extends KitchenFragment implements ItemRecyclerView.RecViewEventHandler
{
	private FloatingActionButton floatingActionButton;
	private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
	    View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

	    this.progressBar = allItemsView.findViewById(R.id.progressBar);
	    this.initFloatingActionButton(allItemsView);
	    this.initRecyclerView(allItemsView);

	    return allItemsView;
    }

    private void initFloatingActionButton(View allItemsView)
    {
	    floatingActionButton = allItemsView.findViewById(R.id.buyItemButton);
	    floatingActionButton.setOnClickListener(v -> {
		    MainActivity mainActivity = (MainActivity) getActivity();
			if (mainActivity != null) {
				mainActivity.changeFragment(new ConfirmPurchasesFragment());
				mainActivity.checkAllItemsMenuItem(false);
			}
		});
    }

    private void initRecyclerView(View allItemsView)
    {
    	new ItemRecyclerView(allItemsView.findViewById(R.id.allItemsRecView),
			    allItemsView.findViewById(R.id.allItemsSwipeRefreshLayout),
			    this);
    }

	@Override
	protected void showToolbarMenu(Menu menu)
	{
		menu.findItem(R.id.action_scan_code).setVisible(true);
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
		this.floatingActionButton.hide();
	}

	@Override
	public void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		this.floatingActionButton.show();
	}

	@Override
	public void handleRecViewItemTouched(View view, int position)
	{
		// TODO - Handle touch
	}
}
