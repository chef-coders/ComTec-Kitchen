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

public class SwipeDelCallback extends ItemTouchHelper.Callback
{
	// =============== Classes ===============

	public interface SwipeEvent
	{
		void handleOnSwiped(RecyclerView.ViewHolder viewHolder);
	}

	// =============== Fields ===============

	private final Drawable   icon;
	private final Drawable   background;
	private final SwipeEvent swipeEvent;

	private boolean swipeBack;

	// =============== Constructors ===============

	public SwipeDelCallback(Drawable icon, Drawable background, SwipeEvent swipeEvent)
	{
		this.swipeEvent = swipeEvent;
		this.icon = icon;
		this.background = background;
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
		this.swipeEvent.handleOnSwiped(viewHolder);
	}
}
