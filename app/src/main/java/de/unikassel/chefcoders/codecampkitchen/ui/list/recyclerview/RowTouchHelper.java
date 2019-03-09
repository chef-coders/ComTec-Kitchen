package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public final class RowTouchHelper extends GestureDetector.SimpleOnGestureListener
	implements RecyclerView.OnItemTouchListener
{
	// =============== Classes ===============

	public interface OnTouchListener
	{
		void onTouch(View view, int pos);
	}

	// =============== Fields ===============

	private final RecyclerView    recyclerView;
	private final GestureDetector gestureDetector;
	private final OnTouchListener onTouchListener;

	// =============== Constructors ===============

	private RowTouchHelper(RecyclerView recyclerView, OnTouchListener onTouchListener)
	{
		this.recyclerView = recyclerView;
		this.gestureDetector = new GestureDetector(recyclerView.getContext(), this);
		this.onTouchListener = onTouchListener;
	}

	// =============== Static Methods ===============

	public static void install(RecyclerView recyclerView, OnTouchListener onTouchListener)
	{
		recyclerView.addOnItemTouchListener(new RowTouchHelper(recyclerView, onTouchListener));
	}

	// =============== Methods ===============

	// --------------- OnItemTouchListener ---------------

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

	// --------------- SimpleOnGestureListener ---------------

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		View child = this.recyclerView.findChildViewUnder(e.getX(), e.getY());
		if (child == null)
		{
			return true;
		}
		int childPos = this.recyclerView.getChildAdapterPosition(child);
		this.onTouchListener.onTouch(child, childPos);
		return false;
	}
}
