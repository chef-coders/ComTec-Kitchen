package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
{
	private GestureDetector gestureDetector;
	private OnTouchListener onTouchListener;

	public interface OnTouchListener
	{
		void onTouch(View view, int pos);
	}

	public RecyclerTouchListener(Context context, RecyclerView recyclerView,
	                             OnTouchListener onTouchListener)
	{
		this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e)
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
		this.onTouchListener = onTouchListener;
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView,
	                                     @NonNull MotionEvent motionEvent)
	{
		this.gestureDetector.onTouchEvent(motionEvent);
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {}
}
