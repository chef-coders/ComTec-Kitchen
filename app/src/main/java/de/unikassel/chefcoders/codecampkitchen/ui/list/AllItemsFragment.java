package de.unikassel.chefcoders.codecampkitchen.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.ItemRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;

public class AllItemsFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
{
	private FloatingActionButton floatingActionButton;
	private ProgressBar          progressBar;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

		this.progressBar = allItemsView.findViewById(R.id.progressBar);
		this.initFloatingActionButton(allItemsView);
		this.initRecyclerView(allItemsView);

		return allItemsView;
	}

	private void initFloatingActionButton(View allItemsView)
	{
		this.floatingActionButton = allItemsView.findViewById(R.id.buyItemButton);
		this.floatingActionButton.setOnClickListener(v -> {
			MainActivity mainActivity = (MainActivity) this.getActivity();
			if (mainActivity != null)
			{
				mainActivity.changeFragment(new ConfirmPurchasesFragment());
				mainActivity.setMenuItem(R.id.nav_all_items, false);
			}
		});
	}

	private void initRecyclerView(View allItemsView)
	{
		new GeneralRecyclerView(allItemsView.findViewById(R.id.allItemsRecView), new ItemRecyclerController(),
		                        allItemsView.findViewById(R.id.allItemsSwipeRefreshLayout), this);
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		if (MainActivity.editMode)
		{
			toolbar.setTitle(R.string.edit_items);
		}
		else
		{
			toolbar.setTitle(R.string.shop);
		}
		Menu menu = toolbar.getMenu();
		menu.findItem(R.id.action_scan_code).setVisible(!MainActivity.editMode);
		menu.findItem(R.id.action_create).setVisible(false);
		menu.findItem(R.id.action_edit).setVisible(false);
		ResultAsyncTask.execute(this.getContext(), () -> MainActivity.kitchenManager.session().isAdmin(), (value) -> {
			menu.findItem(R.id.action_create).setVisible(value && MainActivity.editMode);
			menu.findItem(R.id.action_edit).setVisible(value);
		});
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
		if (!MainActivity.kitchenManager.cart().getPurchases().isEmpty())
		{
			this.floatingActionButton.show();
		}
	}

	@Override
	public void onClick(int section, int item)
	{
		if (!MainActivity.editMode && !MainActivity.kitchenManager.cart().getPurchases().isEmpty())
		{
			this.floatingActionButton.show();
		}
	}

	@Override
	public void onSwiped(int section, int item)
	{
		if (MainActivity.kitchenManager.cart().getPurchases().isEmpty())
		{
			this.floatingActionButton.hide();
		}
	}
}
