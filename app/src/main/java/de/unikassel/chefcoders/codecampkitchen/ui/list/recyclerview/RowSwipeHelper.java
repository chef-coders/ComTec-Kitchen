package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class RowSwipeHelper extends ItemTouchHelper.Callback
{
	// =============== Fields ===============

	private final RecyclerView    recyclerView;
	private final Drawable        icon;
	private final Drawable        background;
	private final RowEventHandler eventHandler;

	private boolean swipeBack;

	// =============== Constructors ===============

	private RowSwipeHelper(RecyclerView recyclerView, Drawable icon, Drawable background, RowEventHandler eventHandler)
	{
		this.recyclerView = recyclerView;
		this.icon = icon;
		this.background = background;
		this.eventHandler = eventHandler;
	}

	// =============== Static Methods ===============

	public static void install(RecyclerView recyclerView, Drawable icon, Drawable background,
		RowEventHandler eventHandler)
	{
		new ItemTouchHelper(new RowSwipeHelper(recyclerView, icon, background, eventHandler))
			.attachToRecyclerView(recyclerView);
	}

	// =============== Methods ===============

	// --------------- General ---------------

	@Override
	public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder)
	{
		return makeMovementFlags(0, LEFT);
	}

	@Override
	public int convertToAbsoluteDirection(int flags, int layoutDirection)
	{
		if (this.swipeBack)
		{
			this.swipeBack = false;
			return 0;
		}

		return super.convertToAbsoluteDirection(flags, layoutDirection);
	}

	// --------------- Draw ---------------

	@Override
	public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
		@NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
	{
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

		boolean successfulSwipe = this.isSuccessfulSwipe(recyclerView, viewHolder);

		if (actionState == ACTION_STATE_SWIPE)
		{
			this.swipeBack = !successfulSwipe;
		}

		if (successfulSwipe)
		{
			this.drawBackgroundFrame(c, viewHolder, dX);
		}
	}

	private boolean isSuccessfulSwipe(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) recyclerView.getAdapter();
		assert adapter != null;

		try
		{
			final int itemIndex = adapter.getPositionInSection(viewHolder.getAdapterPosition());
			return itemIndex >= 0;
		}
		catch (IndexOutOfBoundsException ex)
		{
			return false;
		}
	}

	private void drawBackgroundFrame(Canvas c, RecyclerView.ViewHolder viewHolder, float dX)
	{
		View itemView = viewHolder.itemView;
		int backgroundCornerOffset = 20;

		int iconMargin = (itemView.getHeight() - this.icon.getIntrinsicHeight()) / 2;
		int iconTop = itemView.getTop() + (itemView.getHeight() - this.icon.getIntrinsicHeight()) / 2;
		int iconBottom = iconTop + this.icon.getIntrinsicHeight();

		if (dX < 0)
		{
			// swiping to the left
			int iconLeft = itemView.getRight() - iconMargin - this.icon.getIntrinsicWidth();
			int iconRight = itemView.getRight() - iconMargin;
			this.icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

			this.background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(),
			                          itemView.getRight(), itemView.getBottom());
		}
		else
		{
			this.background.setBounds(0, 0, 0, 0);
		}
		this.background.draw(c);
		this.icon.draw(c);
	}

	// --------------- Movement ---------------

	@Override
	public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
		@NonNull RecyclerView.ViewHolder viewHolder1)
	{
		return false;
	}

	// --------------- Swipe ---------------

	@Override
	public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
	{
		this.eventHandler.handle(RowInfo.from(this.recyclerView, viewHolder));
	}
}
