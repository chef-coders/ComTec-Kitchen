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

		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, () -> {
			this.reload();
			this.eventHandler.handleRecViewLoadFinished();
		});
	}

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	private void handleOnTouch(final View view, int pos)
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		final int itemIndex = adapter.getPositionInSection(pos);
		final GeneralSection section = (GeneralSection) adapter.getSectionForPosition(pos);
		final int sectionIndex = section.getIndex();

		if (itemIndex < 0)
		{
			final int numItems = this.recyclerController.getItems(section.getIndex());

			if (section.isCollapsed())
			{
				section.setCollapsed(false);
				adapter.notifyItemRangeInsertedInSection(section, 0, numItems);
			}
			else
			{
				section.setCollapsed(true);
				adapter.notifyItemRangeRemovedFromSection(section, 0, numItems);
			}

			return;
		}

		final RecyclerView.ViewHolder viewHolder = section.getItemViewHolder(view);
		assert viewHolder != null;

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			return this.recyclerController.onClick(viewHolder, sectionIndex, itemIndex);
		}, (Boolean b) -> {
			if (b)
			{
				// TODO adapter.notifyItemChanged(pos);
				this.recyclerController.populate(viewHolder, sectionIndex, itemIndex);
			}
			this.eventHandler.onClick(sectionIndex, itemIndex);
		});
	}

	@Override
	public void handleOnSwiped(RecyclerView.ViewHolder viewHolder)
	{
		final int pos = viewHolder.getLayoutPosition();
		if (pos == NO_POSITION)
		{
			return;
		}

		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		final int itemIndex = adapter.getPositionInSection(pos);
		if (itemIndex < 0)
		{
			// header swiped
			return;
		}

		final GeneralSection section = (GeneralSection) adapter.getSectionForPosition(pos);
		final int sectionIndex = section.getIndex();

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			boolean refreshAll = this.recyclerController.onSwiped(viewHolder, sectionIndex, itemIndex);
			if (refreshAll)
			{
				this.recyclerController.refresh();
			}
			return refreshAll;
		}, (Boolean refreshAll) -> {
			if (refreshAll)
			{
				this.reload();
			}
			else
			{
				// TODO not necessary
				adapter.notifyDataSetChanged();
			}
			this.eventHandler.onSwiped(sectionIndex, itemIndex);
		});
	}

	private void handleOnSwipeRefresh()
	{
		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, () -> {
			this.reload();
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

	private void reload()
	{
		this.recyclerController.reload();

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
