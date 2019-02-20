package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.RecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;

public class MasterRecyclerView
{
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecViewEventHandler eventHandler;
	private RecyclerController recyclerController;

	public interface RecViewEventHandler
	{
		void handleRecViewLoadFinished();
		void handleRecViewScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy);
		void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy);
		void handleRecViewItemTouched(View view, int position);
	}

	public MasterRecyclerView(RecyclerView recyclerView, RecyclerController recyclerController,
	                          SwipeRefreshLayout swipeRefreshLayout, RecViewEventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
		this.recyclerController = recyclerController;

		this.initRecyclerView(recyclerView);
		this.initSwipeRefreshLayout(swipeRefreshLayout);
	}

	private void initRecyclerView(RecyclerView recyclerView)
	{
		this.recyclerView = recyclerView;

		this.recyclerView.setHasFixedSize(true);

		this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
			{
				handleOnScrolled(recyclerView, dx, dy);
			}
		});

		this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
				recyclerView.getContext(), this.recyclerView, (v, p) -> this.eventHandler.handleRecViewItemTouched(v, p)));

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.recyclerView.getContext());
		this.recyclerView.setLayoutManager(layoutManager);

		ItemAdapter itemAdapter = new ItemAdapter();

		ResultAsyncTask.<List<Item>>exeResultAsyncTask(
				()                 -> {
					MainActivity.kitchenManager.refreshItems();
					return MainActivity.kitchenManager.getItems();
				}, (items) ->
				{
					itemAdapter.setItems(items);
					this.eventHandler.handleRecViewLoadFinished();
				});

		this.recyclerView.setAdapter(itemAdapter);
	}

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

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
					this.swipeRefreshLayout.setRefreshing(false);
				});
	}

	private void handleOnScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		if(dy > 0)
		{
			// scrolls down
			this.eventHandler.handleRecViewScrolledDown(recyclerView, dx, dy);
		}
		else
		{
			// scrolls up
			this.eventHandler.handleRecViewScrolledUp(recyclerView, dx, dy);
		}
	}
}
