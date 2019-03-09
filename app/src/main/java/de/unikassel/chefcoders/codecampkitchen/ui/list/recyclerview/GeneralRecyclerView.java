package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.async.ResultAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.RecyclerController;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import java.util.Map;

public class GeneralRecyclerView
{
	// =============== Fields ===============

	private final RecyclerView         recyclerView;
	private final SwipeRefreshLayout   swipeRefreshLayout;
	private final RecyclerEventHandler eventHandler;
	private final RecyclerController   recyclerController;

	// =============== Constructors ===============

	private GeneralRecyclerView(RecyclerView recyclerView, RecyclerController recyclerController,
		SwipeRefreshLayout swipeRefreshLayout, RecyclerEventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
		this.recyclerController = recyclerController;
		this.recyclerView = recyclerView;
		this.swipeRefreshLayout = swipeRefreshLayout;
	}

	// =============== Static Methods ===============

	public static void install(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout,
		RecyclerController recyclerController, RecyclerEventHandler eventHandler)
	{
		final GeneralRecyclerView view = new GeneralRecyclerView(recyclerView, recyclerController, swipeRefreshLayout,
		                                                         eventHandler);
		view.initRecyclerView();
		view.initData();
		view.initSwipeRefreshLayout();
		view.initScroll();
		view.initTouch();
		view.initSwipe();
	}

	// =============== Methods ===============

	// --------------- Initialization ---------------

	private void initRecyclerView()
	{
		this.recyclerView.setHasFixedSize(true);
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this.recyclerView.getContext()));
		this.recyclerView.setAdapter(new SectionedRecyclerViewAdapter());
	}

	private void initData()
	{
		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, this::reload,
		                        this.eventHandler::handleLoadFinished);
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
			adapter.addSection(new CollapsibleSection(this.recyclerController, index));
		}
		// remove extra sections
		if (oldSections > newSections)
		{
			for (Map.Entry<String, Section> entry : adapter.getCopyOfSectionsMap().entrySet())
			{
				final CollapsibleSection section = (CollapsibleSection) entry.getValue();
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

	private void initSwipeRefreshLayout()
	{
		this.swipeRefreshLayout.setOnRefreshListener(this::handleOnSwipeRefresh);
	}

	private void handleOnSwipeRefresh()
	{
		this.reload();
		SimpleAsyncTask.execute(this.recyclerView.getContext(), this.recyclerController::refresh, this::reload,
		                        () -> this.swipeRefreshLayout.setRefreshing(false));
	}

	// --------------- Scroll ---------------

	private void initScroll()
	{
		this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
			{
				GeneralRecyclerView.this.handleOnScrolled(recyclerView, dx, dy);
			}
		});
	}

	private void handleOnScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		if (dy > 0)
		{
			// scrolls down
			this.eventHandler.handleScrolledDown(recyclerView, dx, dy);
		}
		else
		{
			// scrolls up
			this.eventHandler.handleScrolledUp(recyclerView, dx, dy);
		}
	}

	// --------------- Touch ---------------

	private void initTouch()
	{
		RowTouchHelper.install(this.recyclerView, this::handleOnTouch);
	}

	private void handleOnTouch(RowInfo row)
	{
		if (row.getRowIndex() < 0)
		{
			// header tapped

			final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
			assert adapter != null;

			final CollapsibleSection section = (CollapsibleSection) row.getSection();
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

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			return this.recyclerController.onClick(row);
		}, refreshRow -> {
			if (refreshRow)
			{
				this.recyclerView.getAdapter().notifyItemChanged(row.getAdapterPosition());
			}
		}, () -> this.eventHandler.handleClick(row));
	}

	// --------------- Swipe ---------------

	private void initSwipe()
	{
		if (this.recyclerController.swipeIsSupported())
		{
			final Drawable trashIcon = ContextCompat.getDrawable(this.recyclerView.getContext(),
			                                                     R.drawable.ic_delete_white_36dp);
			final Drawable accentColor = ContextCompat.getDrawable(this.recyclerView.getContext(), R.color.colorAccent);
			RowSwipeHelper.install(this.recyclerView, trashIcon, accentColor, this::handleOnSwiped);
		}
	}

	private void handleOnSwiped(RowInfo row)
	{
		if (row.getRowIndex() < 0)
		{
			// header swiped
			return;
		}

		ResultAsyncTask.execute(this.recyclerView.getContext(), () -> {
			return this.recyclerController.onSwiped(row);
		}, removed -> {
			final CollapsibleSection section = (CollapsibleSection) row.getSection();
			// not removed -> only refresh the row
			if (!removed)
			{
				this.recyclerView.getAdapter().notifyItemChanged(row.getAdapterPosition());
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
				this.recyclerView.getAdapter().notifyItemRemoved(row.getAdapterPosition());
			}
		}, () -> this.eventHandler.handleSwipe(row));
	}

	private void removeSection(CollapsibleSection toRemove)
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) this.recyclerView.getAdapter();
		assert adapter != null;

		for (Map.Entry<String, Section> entry : adapter.getCopyOfSectionsMap().entrySet())
		{
			final CollapsibleSection section = (CollapsibleSection) entry.getValue();
			final int index = section.getIndex();

			if (section == toRemove)
			{
				adapter.removeSection(entry.getKey());
			}
			else if (index > toRemove.getIndex())
			{
				section.setIndex(index - 1);
			}
		}

		adapter.notifyDataSetChanged();
	}
}
