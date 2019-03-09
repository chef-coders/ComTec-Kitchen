package de.unikassel.chefcoders.codecampkitchen.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.PurchasesRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RecyclerEventHandler;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchasesFragment extends KitchenFragment implements RecyclerEventHandler
{

	private ProgressBar progressBar;

	public MyPurchasesFragment()
	{
		super(R.string.purchase_history, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_my_purchases, container, false);
		this.progressBar = view.findViewById(R.id.progressBar);
		this.initRecyclerView(view);
		return view;
	}

	private void initRecyclerView(View view)
	{
		GeneralRecyclerView
			.install(view.findViewById(R.id.allItemsRecView), view.findViewById(R.id.allItemsSwipeRefreshLayout),
			         new PurchasesRecyclerController(), this);
	}

	// --- --- --- Handle user interactions --- --- ---
	@Override
	public void handleLoadFinished()
	{
		this.progressBar.setVisibility(View.GONE);
	}

	@Override
	public void handleScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy)
	{

	}

	@Override
	public void handleScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
	}

	@Override
	public void handleClick(RowInfo row)
	{

	}

	@Override
	public void handleSwipe(RowInfo row)
	{

	}
}
