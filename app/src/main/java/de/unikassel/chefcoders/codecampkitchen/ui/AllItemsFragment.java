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
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AllItemsFragment extends KitchenFragment
{
	private FloatingActionButton floatingActionButton;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
	    View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

	    this.initSwipeRefreshLayout(allItemsView);
	    this.initFloatingActionButton(allItemsView);
	    this.initRecyclerView(allItemsView);

	    return allItemsView;
    }

    private void initSwipeRefreshLayout(View allItemsView)
    {
    	this.swipeRefreshLayout = allItemsView.findViewById(R.id.allItemsSwipeRefreshLayout);
    	this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
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
	    this.recyclerView = allItemsView.findViewById(R.id.allItemsRecView);
        ProgressBar progressBar = allItemsView.findViewById(R.id.progressBar);

	    this.recyclerView.setHasFixedSize(true);

	    this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
	    {
		    @Override
		    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
		    {
			    handleOnScrolled(recyclerView, dx, dy);
		    }
	    });

	    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
	    this.recyclerView.setLayoutManager(layoutManager);

	    ItemAdapter itemAdapter = new ItemAdapter();

	    ResultAsyncTask.<List<Item>>exeResultAsyncTask(
			    ()                 -> {
				    MainActivity.kitchenManager.refreshItems();
				    return MainActivity.kitchenManager.getItems();
			    }, (items) ->
                {
                    progressBar.setVisibility(View.GONE);
                    itemAdapter.setItems(items);
                });

	    this.recyclerView.setAdapter(itemAdapter);
    }

	@Override
	protected void showToolbarMenu(Menu menu)
	{
		menu.findItem(R.id.action_scan_code).setVisible(true);
	}

	// --- --- --- Handle user interactions --- --- ---
	private void handleOnSwipeRefresh()
	{
		ResultAsyncTask.<List<Item>>exeResultAsyncTask(
				()                 -> {
					MainActivity.kitchenManager.refreshItems();
					return MainActivity.kitchenManager.getItems();
				},
				(List<Item> items) -> {
					ItemAdapter itemAdapter = (ItemAdapter)this.recyclerView.getAdapter();
					itemAdapter.setItems(items);
				});

		swipeRefreshLayout.setRefreshing(false);
	}

	private void handleOnScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		if(dy > 0)
		{
			// scrolls down
			floatingActionButton.hide();
		}
		else
		{
			// scrolls up
			floatingActionButton.show();
		}
	}
}
