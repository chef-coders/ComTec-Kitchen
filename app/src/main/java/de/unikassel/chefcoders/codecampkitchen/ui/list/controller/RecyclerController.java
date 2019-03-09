package de.unikassel.chefcoders.codecampkitchen.ui.list.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;

public interface RecyclerController<V extends RecyclerView.ViewHolder>
{
	int getSections();

	int getItems(int sections);

	String getHeader(int section);

	void reload();

	void refresh();

	V create(View view);

	void populate(V v, int section, int item);

	boolean onClick(RowInfo<V> row);

	boolean onSwiped(RowInfo<V> row);

	boolean swipeIsSupported();
}
