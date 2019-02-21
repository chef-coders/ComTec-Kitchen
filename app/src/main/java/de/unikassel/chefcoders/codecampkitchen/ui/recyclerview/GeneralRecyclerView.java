package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.controller.RecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class GeneralRecyclerView implements SwipeDelCallback.SwipeEvent
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

		void onClick(int section, int item);

		void onSwiped(int section, int item);
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
				GeneralRecyclerView.this.handleOnScrolled(recyclerView, dx, dy);
			}
		});

		this.recyclerView.addOnItemTouchListener(
			new RecyclerTouchListener(recyclerView.getContext(), this.recyclerView, this::handleOnTouch));

		if (this.recyclerController.swipeIsSupported())
		{
			ItemTouchHelper.SimpleCallback itemSwipeCallback = new SwipeDelCallback(this, ContextCompat.getDrawable(
				this.recyclerView.getContext(), R.drawable.ic_delete_white_36dp), new ColorDrawable(Color.RED));
			new ItemTouchHelper(itemSwipeCallback).attachToRecyclerView(this.recyclerView);
		}
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.recyclerView.getContext());
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setAdapter(new SectionedRecyclerViewAdapter());

		SimpleAsyncTask.execute(() -> this.recyclerController.refresh(), () -> {
			this.reloadSections();
			this.eventHandler.handleRecViewLoadFinished();
		});
	}

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	private RowPos calcRowPos(int pos)
	{
		SectionedRecyclerViewAdapter sectionedAdapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		if (sectionedAdapter == null)
		{
			return null;
		}

		int numOfSections = this.recyclerController.getSections();
		int counter = 0;

		for (int sectionId = 0; sectionId < numOfSections; sectionId++)
		{
			counter++;
			Section section = sectionedAdapter.getSectionForPosition(sectionId);

			for (int itemId = 0; itemId < section.getContentItemsTotal(); itemId++)
			{
				if (counter + itemId == pos)
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

		this.eventHandler.onClick(rowPos.getSectionId(), rowPos.getItemId());
		ResultAsyncTask.exeResultAsyncTask(() -> {
			return this.recyclerController.onClick(rowPos.getSectionId(), rowPos.getItemId());
		}, (Boolean b) -> {
			RecyclerView.ViewHolder viewHolder = rowPos.getSection().getItemViewHolder(view);
			if (b && viewHolder != null)
			{
				this.recyclerController.populate(viewHolder, rowPos.getSectionId(), rowPos.getItemId());
			}
		});
	}

	@Override
	public void handleOnSwiped(RecyclerView.ViewHolder viewHolder, int direction)
	{
		final int position = viewHolder.getAdapterPosition();
		if (position == NO_POSITION)
		{
			return;
		}

		final RowPos rowPos = this.calcRowPos(position);
		if (rowPos == null || rowPos.getSection() == null)
		{
			return;
		}

		this.eventHandler.onSwiped(rowPos.getSectionId(), rowPos.getItemId());
		ResultAsyncTask.exeResultAsyncTask(() -> {
			boolean refreshAll = this.recyclerController.onSwiped(rowPos.getSectionId(), rowPos.getItemId());
			if (refreshAll)
			{
				this.recyclerController.refresh();
			}
			return refreshAll;
		}, (Boolean refreshAll) -> {
			if (refreshAll)
			{
				this.reloadSections();
			}
			else
			{
				RecyclerView.Adapter adapter = this.recyclerView.getAdapter();
				if(adapter != null)
				{
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void handleOnSwipeRefresh()
	{
		SimpleAsyncTask.execute(() -> this.recyclerController.refresh(), () -> {
			this.reloadSections();
			this.swipeRefreshLayout.setRefreshing(false);
		});
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
