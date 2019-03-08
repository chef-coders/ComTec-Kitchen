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
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.UserRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
{

	private ProgressBar progressBar;

	public AllUserFragment()
	{
		super(R.string.users, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_all_user, container, false);
		this.initRecyclerView(view);
		return view;
	}

	private void initRecyclerView(View view)
	{
		this.progressBar = view.findViewById(R.id.progressBar);
		GeneralRecyclerView.install(view.findViewById(R.id.recyclerView), view.findViewById(R.id.swipeRefreshLayout),
		                            new UserRecyclerController(), this);
	}

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
	public void onClick(int section, int item)
	{
	}

	@Override
	public void onSwiped(int section, int item)
	{

	}
}
