package de.unikassel.chefcoders.codecampkitchen.ui.controller;

import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public abstract class GroupedRecyclerController<T, V extends RecyclerView.ViewHolder> implements RecyclerController<V>
{
	private List<T>[] items;
	private String[]  headers;

	@Override
	public int getSections()
	{
		return this.items.length;
	}

	@Override
	public int getItems(int section)
	{
		return this.items[section].size();
	}

	@Override
	public String getHeader(int section)
	{
		return this.headers[section];
	}

	protected T get(int section, int item)
	{
		return this.items[section].get(item);
	}

	public final void fill(Map<String, List<T>> grouped)
	{
		final int numSections = grouped.size();

		this.items = (List<T>[]) new List[numSections];
		this.headers = new String[numSections];

		int section = 0;
		for (Map.Entry<String, List<T>> entry : grouped.entrySet())
		{
			this.headers[section] = entry.getKey();
			this.items[section++] = entry.getValue();
		}
	}
}
