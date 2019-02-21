package de.unikassel.chefcoders.codecampkitchen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.barcodes.EditItemActivity;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.ItemRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.GeneralRecyclerView;

public class AllItemsFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
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
		floatingActionButton.setOnClickListener(v ->
		{
			MainActivity mainActivity = (MainActivity) getActivity();
			if (mainActivity != null) {
				mainActivity.changeFragment(new ConfirmPurchasesFragment());
				mainActivity.checkAllItemsMenuItem(false);
			}
		});
	}

	private void initRecyclerView(View allItemsView)
	{
		new GeneralRecyclerView(allItemsView.findViewById(R.id.allItemsRecView),
				new ItemRecyclerController(),
				allItemsView.findViewById(R.id.allItemsSwipeRefreshLayout),
				this);
	}

	@Override
	protected void showToolbarMenu(Menu menu)
	{
		menu.findItem(R.id.action_scan_code).setVisible(true);
		menu.findItem(R.id.action_create).setVisible(false);
		menu.findItem(R.id.action_edit).setVisible(false);
		ResultAsyncTask.exeResultAsyncTask(() -> MainActivity.kitchenManager.isAdmin(), (value) ->
			{
				menu.findItem(R.id.action_create).setVisible(value);
				menu.findItem(R.id.action_edit).setVisible(value);
			}
		);
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
	public void onClick(int section, int item)
	{
		if (MainActivity.editMode) {
			final Item clickedItem = MainActivity.kitchenManager.getItem(section, item);
			Intent intent = new Intent(getActivity(), EditItemActivity.class);
			intent.putExtra("itemId", clickedItem.get_id());
			startActivity(intent);
		}
	}

	@Override
	public void onSwiped(int section, int item)
	{
	}
}
