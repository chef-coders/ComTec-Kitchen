package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.RecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class GeneralRecyclerView
{
	private RecyclerView        recyclerView;
	private SwipeRefreshLayout  swipeRefreshLayout;
	private RecViewEventHandler eventHandler;
	private RecyclerController  recyclerController;

	public interface RecViewEventHandler
	{
		void handleRecViewLoadFinished();

		void handleRecViewScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy);

		void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy);

		void handleRecViewItemTouched(View view, int position);
	}

	public GeneralRecyclerView(RecyclerView recyclerView, RecyclerController recyclerController,
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

		this.recyclerView.addOnItemTouchListener(
				new RecyclerTouchListener(
						recyclerView.getContext(),
						this.recyclerView,
						this::handleOnTouch));

		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
		{
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
			{ return false; }

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
			{
				handleOnSwiped(viewHolder, direction);
			}
		};
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerView);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.recyclerView.getContext());
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setAdapter(new SectionedRecyclerViewAdapter());

		new SimpleAsyncTask(() -> this.recyclerController.refresh(), () -> {
			this.reloadSections();
			this.eventHandler.handleRecViewLoadFinished();
		}).execute();
	}

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	private RowPos calcRowPos(int pos)
	{
		SectionedRecyclerViewAdapter sectionedAdapter = (SectionedRecyclerViewAdapter)this.recyclerView.getAdapter();
		if(sectionedAdapter == null)
		{
			return null;
		}

		int numOfSections = this.recyclerController.getSections();
		int counter = 0;

		for(int sectionId = 0; sectionId < numOfSections; sectionId++)
		{
			counter++;
			Section section = sectionedAdapter.getSectionForPosition(sectionId);

			for(int itemId = 0; itemId < section.getContentItemsTotal(); itemId++)
			{
				if(counter + itemId == pos)
				{
					return new RowPos(section, sectionId, itemId);
				}
			}

			counter += section.getContentItemsTotal();
		}

		return null;
	}

	private void handleOnTouch(final View view, int pos)
	{
		final RowPos rowPos = this.calcRowPos(pos);
		if (rowPos == null || rowPos.getSection() == null)
		{
			return;
		}

		ResultAsyncTask.exeResultAsyncTask(()->
						this.recyclerController.onClick(rowPos.getSectionId(), rowPos.getRowId()),
				(Boolean b) -> {
					RecyclerView.ViewHolder viewHolder = rowPos.getSection().getItemViewHolder(view);
					if(b && viewHolder != null)
					{
						this.recyclerController.populate(viewHolder, rowPos.getSectionId(), rowPos.getRowId());
					}
				});
	}

	private void handleOnSwiped(RecyclerView.ViewHolder viewHolder, int direction)
	{
		final RowPos rowPos = this.calcRowPos(viewHolder.getPosition());
		if(rowPos == null || rowPos.getSection() == null)
		{
			return;
		}

		new SimpleAsyncTask(() -> {
				boolean refresh = this.recyclerController.onSwiped(rowPos.getSectionId(), rowPos.getRowId());
				if(refresh)
				{
					this.recyclerController.refresh();
				}
			}, () -> {}).execute();
	}

	private void handleOnSwipeRefresh()
	{
		new SimpleAsyncTask(() -> this.recyclerController.refresh(), () -> {
			this.reloadSections();
			this.swipeRefreshLayout.setRefreshing(false);
		}).execute();
	}

	private void handleOnScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		if (dy > 0)
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

	private void reloadSections()
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		adapter.removeAllSections();

		final int sections = this.recyclerController.getSections();
		for (int section = 0; section < sections; section++)
		{
			adapter.addSection(new GeneralSection(this.recyclerController, section));
		}

		adapter.notifyDataSetChanged();
	}
}
