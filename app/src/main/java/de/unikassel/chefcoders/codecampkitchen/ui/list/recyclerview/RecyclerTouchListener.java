package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
{
	// =============== Classes ===============

	public interface OnTouchListener
	{
		void onTouch(View view, int pos);
	}

	// =============== Fields ===============

	private GestureDetector gestureDetector;

	// =============== Constructors ===============

	public RecyclerTouchListener(Context context, RecyclerView recyclerView, OnTouchListener onTouchListener)
	{
		this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onSingleTapUp(MotionEvent e)
			{
				View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
				if (child == null)
				{
					return true;
				}
				int childPos = recyclerView.getChildAdapterPosition(child);
				onTouchListener.onTouch(child, childPos);
				return false;
			}
		});
	}

	// =============== Methods ===============

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent)
	{
		this.gestureDetector.onTouchEvent(motionEvent);
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent)
	{
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b)
	{
	}
}
