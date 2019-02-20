package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface RecyclerController<T extends RecyclerView.ViewHolder>
{
	int getSections();

	int getItems(int sections);

	String getHeader(int section);

	void refresh();

	T create(View view);

	void populate(T v, int section, int item);

	void onClick(int section, int item);
}
