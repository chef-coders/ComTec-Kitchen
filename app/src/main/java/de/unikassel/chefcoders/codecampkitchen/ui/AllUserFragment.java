package de.unikassel.chefcoders.codecampkitchen.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.UserRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.GeneralRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
{

	private ProgressBar progressBar;

	public AllUserFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_all_user, container, false);
		this.initRecyclerView(view);
		return view;
	}

	private void initRecyclerView(View view)
	{
		progressBar = view.findViewById(R.id.progressBar);
		new GeneralRecyclerView(view.findViewById(R.id.recyclerView),
				new UserRecyclerController(),
				view.findViewById(R.id.swipeRefreshLayout),
				this);
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.users);
	}

	@Override
	public void handleRecViewLoadFinished()
	{
		progressBar.setVisibility(View.GONE);
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
