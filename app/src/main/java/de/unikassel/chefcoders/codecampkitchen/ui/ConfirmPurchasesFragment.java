package de.unikassel.chefcoders.codecampkitchen.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.ItemRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.ShoppingCartRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.GeneralRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPurchasesFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
{

	private FloatingActionButton floatingActionButton;
	private ProgressBar progressBar;

	public ConfirmPurchasesFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_confirm_purchases, container, false);
		this.initFloatingActionButton(view);
		this.initRecyclerView(view);
		return view;
	}

	private void initFloatingActionButton(View view)
	{
		floatingActionButton = view.findViewById(R.id.buyItemButton);
		floatingActionButton.setOnClickListener(v ->
				new SimpleAsyncTask(() -> MainActivity.kitchenManager.submitCart(),
						() ->
						{
							MainActivity mainActivity = (MainActivity) getActivity();
							if (mainActivity != null) {
								mainActivity.changeFragment(new AllItemsFragment());
								Toast.makeText(getActivity(), R.string.purchase_success, Toast.LENGTH_LONG)
										.show();
							}
						}).execute());
	}

	private void initRecyclerView(View view)
	{
		progressBar = view.findViewById(R.id.progressBar);
		new GeneralRecyclerView(view.findViewById(R.id.allItemsRecView),
				new ShoppingCartRecyclerController(),
				view.findViewById(R.id.allItemsSwipeRefreshLayout),
				this);
	}

	@Override
	protected void showToolbarMenu(Menu menu)
	{
		menu.findItem(R.id.action_clear_all)
				.setVisible(true);
	}

	@Override
	public void handleRecViewLoadFinished()
	{
		progressBar.setVisibility(View.GONE);
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
