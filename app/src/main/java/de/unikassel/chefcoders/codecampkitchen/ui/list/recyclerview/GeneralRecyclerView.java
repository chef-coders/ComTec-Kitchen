package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.RecyclerController;
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
			ItemTouchHelper.Callback itemSwipeCallback = new SwipeDelCallback(this, ContextCompat.getDrawable(
				this.recyclerView.getContext(), R.drawable.ic_delete_white_36dp), ContextCompat.getDrawable(
				this.recyclerView.getContext(), R.color.colorAccent), this.recyclerController);
			new ItemTouchHelper(itemSwipeCallback).attachToRecyclerView(this.recyclerView);
		}
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.recyclerView.getContext());
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setAdapter(new SectionedRecyclerViewAdapter());

		SimpleAsyncTask.execute(this.recyclerView.getContext(), () -> this.recyclerController.refresh(), () -> {
			this.reloadSections();
			this.eventHandler.handleRecViewLoadFinished();
		});
	}

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	public static RowPos calcRowPos(int pos, RecyclerController recyclerController, RecyclerView recyclerView)
	{
		SectionedRecyclerViewAdapter sectionedAdapter = (SectionedRecyclerViewAdapter) recyclerView.getAdapter();
		if (sectionedAdapter == null)
		{
			return null;
		}

		int numOfSections = recyclerController.getSections();
		int counter = 0;

		for (int sectionId = 0; sectionId < numOfSections; sectionId++)
		{
			if (pos == counter)
			{
				return null;
			}
			counter++;
			Section section = sectionedAdapter.getSectionForPosition(sectionId);
			int sectionSize = recyclerController.getItems(sectionId);

			for (int itemId = 0; itemId < sectionSize; itemId++)
			{
				if (counter + itemId == pos)
				{
					return new RowPos(section, sectionId, itemId);
				}
			}

			counter += sectionSize;
		}

		return null;
	}

	private void handleOnTouch(final View view, int pos)
	{
		final RowPos rowPos = calcRowPos(pos, this.recyclerController, this.recyclerView);
		if (rowPos == null || rowPos.getSection() == null)
		{
			return;
		}

		RecyclerView.ViewHolder viewHolder = rowPos.getSection().getItemViewHolder(view);
		if (viewHolder == null)
		{
			return;
		}

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			return this.recyclerController.onClick(viewHolder, rowPos.getSectionId(), rowPos.getItemId());
		}, (Boolean b) -> {
			if (b)
			{
				this.recyclerController.populate(viewHolder, rowPos.getSectionId(), rowPos.getItemId());
			}
			this.eventHandler.onClick(rowPos.getSectionId(), rowPos.getItemId());
		});
	}

	@Override
	public void handleOnSwiped(RecyclerView.ViewHolder viewHolder)
	{
		final int position = viewHolder.getLayoutPosition();
		if (position == NO_POSITION)
		{
			return;
		}

		final RowPos rowPos = calcRowPos(position, this.recyclerController, this.recyclerView);
		if (rowPos == null || rowPos.getSection() == null)
		{
			return;
		}

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			boolean refreshAll = this.recyclerController
				                     .onSwiped(viewHolder, rowPos.getSectionId(), rowPos.getItemId());
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
				if (adapter != null)
				{
					adapter.notifyDataSetChanged();
				}
			}
			this.eventHandler.onSwiped(rowPos.getSectionId(), rowPos.getItemId());
		});
	}

	private void handleOnSwipeRefresh()
	{
		SimpleAsyncTask.execute(this.recyclerView.getContext(), () -> this.recyclerController.refresh(), () -> {
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