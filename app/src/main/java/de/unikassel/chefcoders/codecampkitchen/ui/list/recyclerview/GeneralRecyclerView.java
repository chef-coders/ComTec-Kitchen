package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.RecyclerController;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import java.util.Map;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class GeneralRecyclerView
{
	// =============== Classes ===============

	public interface RecViewEventHandler
	{
		void handleRecViewLoadFinished();

		void handleRecViewScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy);

		void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy);

		void onClick(int section, int item);

		void onSwiped(int section, int item);
	}

	// =============== Fields ===============

	private RecyclerView        recyclerView;
	private SwipeRefreshLayout  swipeRefreshLayout;
	private RecViewEventHandler eventHandler;
	private RecyclerController  recyclerController;

	// =============== Constructors ===============

	public GeneralRecyclerView(RecyclerView recyclerView, RecyclerController recyclerController,
		SwipeRefreshLayout swipeRefreshLayout, RecViewEventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
		this.recyclerController = recyclerController;

		this.initRecyclerView(recyclerView);
		this.initSwipeRefreshLayout(swipeRefreshLayout);
	}

	// =============== Methods ===============

	// --------------- Initialization ---------------

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

		RecyclerTouchListener.install(recyclerView, this::handleOnTouch);

		if (this.recyclerController.swipeIsSupported())
		{
			final Drawable trashIcon = ContextCompat.getDrawable(this.recyclerView.getContext(),
			                                                     R.drawable.ic_delete_white_36dp);
			final Drawable accentColor = ContextCompat.getDrawable(this.recyclerView.getContext(), R.color.colorAccent);
			SwipeDelCallback.install(recyclerView, trashIcon, accentColor, this::handleOnSwiped);
		}

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.recyclerView.getContext());
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setAdapter(new SectionedRecyclerViewAdapter());

		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, this::reload,
		                        this.eventHandler::handleRecViewLoadFinished);
	}

	// --------------- Reload ---------------

	private void reload()
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		final int oldSections = this.recyclerController.getSections();
		this.recyclerController.reload();
		final int newSections = this.recyclerController.getSections();

		// add missing sections
		for (int index = oldSections; index < newSections; index++)
		{
			adapter.addSection(new GeneralSection(this.recyclerController, index));
		}
		// remove extra sections
		if (oldSections > newSections)
		{
			for (Map.Entry<String, Section> entry : adapter.getCopyOfSectionsMap().entrySet())
			{
				final GeneralSection section = (GeneralSection) entry.getValue();
				final int sectionIndex = section.getIndex();
				if (sectionIndex >= newSections)
				{
					final String tag = entry.getKey();
					adapter.removeSection(tag);
				}
			}
		}

		adapter.notifyDataSetChanged();
	}

	// --------------- Swipe Refresh ---------------

	private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	private void handleOnSwipeRefresh()
	{
		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, this::reload,
		                        () -> this.swipeRefreshLayout.setRefreshing(false));
	}

	// --------------- Scroll ---------------

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

	// --------------- Touch ---------------

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
		}, refreshRow -> {
			if (refreshRow)
			{
				adapter.notifyItemChanged(pos);
			}
		}, () -> this.eventHandler.onClick(sectionIndex, itemIndex));
	}

	// --------------- Swipe ---------------

	private void handleOnSwiped(RecyclerView.ViewHolder viewHolder)
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
			return this.recyclerController.onSwiped(viewHolder, sectionIndex, itemIndex);
		}, removed -> {
			if (!removed)
			{
				// not removed -> only refresh the row
				adapter.notifyItemChanged(pos);
			}
			else if (section.getSectionItemsTotal() == 2)
			{
				// last item in section removed (i.e. 2 items before reload, header and row) -> remove section
				this.recyclerController.reload();
				this.removeSection(section);
			}
			else
			{
				// one item removed
				this.recyclerController.reload();
				adapter.notifyItemRemoved(pos);
			}
		}, () -> this.eventHandler.onSwiped(sectionIndex, itemIndex));
	}

	private void removeSection(GeneralSection toRemove)
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		for (Map.Entry<String, Section> entry : adapter.getCopyOfSectionsMap().entrySet())
		{
			final GeneralSection generalSection = (GeneralSection) entry.getValue();
			final int index = generalSection.getIndex();

			if (generalSection == toRemove)
			{
				adapter.removeSection(entry.getKey());
			}
			else if (index > toRemove.getIndex())
			{
				generalSection.setIndex(index - 1);
			}
		}

		adapter.notifyDataSetChanged();
	}
}
