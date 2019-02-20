package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.view.View;

public interface RecyclerController<T>
{
	int getSections();
	int getItems(int sections);
	String getHeader(int section);
	void refresh();
	void populate(T v, int section, int item);
	void onClick(int section, int item);
}
