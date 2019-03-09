package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

public final class RowTouchHelper extends GestureDetector.SimpleOnGestureListener
	implements RecyclerView.OnItemTouchListener
{
	// =============== Fields ===============

	private final RecyclerView    recyclerView;
	private final GestureDetector gestureDetector;
	private final RowEventHandler eventHandler;

	// =============== Constructors ===============

	private RowTouchHelper(RecyclerView recyclerView, RowEventHandler eventHandler)
	{
		this.recyclerView = recyclerView;
		this.gestureDetector = new GestureDetector(recyclerView.getContext(), this);
		this.eventHandler = eventHandler;
	}

	// =============== Static Methods ===============

	public static void install(RecyclerView recyclerView, RowEventHandler onTouchListener)
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
		final RowInfo rowInfo = RowInfo.fromPos(this.recyclerView, e.getX(), e.getY());
		if (rowInfo != null)
		{
			this.eventHandler.handle(rowInfo);
		}
		return false;
	}
}
