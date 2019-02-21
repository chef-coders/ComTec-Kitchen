package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class SwipeDelCallback extends ItemTouchHelper.SimpleCallback
{
	private Drawable icon;
	private ColorDrawable background;

	public interface SwipeEvent
	{
		void handleOnSwiped(RecyclerView.ViewHolder viewHolder, int direction);
	}

	private SwipeEvent swipeEvent;

	public SwipeDelCallback(SwipeEvent swipeEvent, Drawable icon, ColorDrawable background)
	{
		super(0, ItemTouchHelper.LEFT);
		this.swipeEvent = swipeEvent;
		this.icon = icon;
		this.background = background;
	}

	@Override
	public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
	{ return false; }

	@Override
	public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
	{
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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

	@Override
	public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
	{
		this.swipeEvent.handleOnSwiped(viewHolder, direction);
	}
}
