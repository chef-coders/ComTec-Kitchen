package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public interface RecyclerEventHandler
{
	void handleLoadFinished();

	void handleScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy);

	void handleScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy);

	void handleClick(RowInfo row);

	void handleSwipe(RowInfo row);
}
