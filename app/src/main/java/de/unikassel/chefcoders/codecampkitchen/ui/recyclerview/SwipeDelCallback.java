package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import de.unikassel.chefcoders.codecampkitchen.ui.controller.RecyclerController;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

public class SwipeDelCallback extends ItemTouchHelper.Callback
{
	public interface SwipeEvent
	{
		void handleOnSwiped(RecyclerView.ViewHolder viewHolder);
	}

	private Drawable icon;
	private Drawable background;

	private RecyclerController recyclerController;

	private boolean swipeBack;

	private SwipeEvent swipeEvent;

	// --- --- --- Initialization --- --- ---
	public SwipeDelCallback(SwipeEvent swipeEvent, Drawable icon, Drawable background, RecyclerController recyclerController)
	{
		this.swipeEvent = swipeEvent;
		this.icon = icon;
		this.background = background;
		this.recyclerController = recyclerController;
	}

	@Override
	public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder)
	{
		return makeMovementFlags(0, LEFT);
	}

	// --- --- --- Handle swipe action --- --- ---
	@Override
	public int convertToAbsoluteDirection(int flags, int layoutDirection)
	{
		if(this.swipeBack)
		{
			this.swipeBack = false;
			return 0;
		}

		return super.convertToAbsoluteDirection(flags, layoutDirection);
	}

	// --- --- --- Handle draw action --- --- ---
	@Override
	public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
	                        float dX, float dY, int actionState, boolean isCurrentlyActive)
	{
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

		boolean successfulSwipe = this.isSuccessfulSwipe(recyclerView, viewHolder);

		if(actionState == ACTION_STATE_SWIPE)
		{
			this.swipeBack = ! successfulSwipe;
		}

		if(successfulSwipe)
		{
			this.drawBackgroundFrame(c, viewHolder, dX);
		}
	}

	private void drawBackgroundFrame(Canvas c, RecyclerView.ViewHolder viewHolder, float dX)
	{
		View itemView = viewHolder.itemView;
		int backgroundCornerOffset = 20;

		int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
		int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
		int iconBottom = iconTop + icon.getIntrinsicHeight();

		if(dX < 0)
		{
			// swiping to the left
			int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
			int iconRight = itemView.getRight() - iconMargin;
			icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

			background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
					itemView.getTop(), itemView.getRight(), itemView.getBottom());
		}
		else
		{
			this.background.setBounds(0, 0, 0, 0);
		}
		this.background.draw(c);
		this.icon.draw(c);
	}

	private boolean isSuccessfulSwipe(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		RowPos swipePos = GeneralRecyclerView
				.calcRowPos(viewHolder.getLayoutPosition(), this.recyclerController, recyclerView);
		return swipePos != null;
	}

	// --- --- --- Not used --- --- ---
	@Override
	public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
	{ return false; }

	@Override
	public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
	{
		this.swipeEvent.handleOnSwiped(viewHolder);
	}
}
