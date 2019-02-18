package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder>
{
	public String[] dataset;

	public ItemsAdapter(String[] test)
	{
		this.dataset = test;
	}

	@Override
	public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.item_view, viewGroup, false);

		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
		return itemViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i)
	{
		itemViewHolder.showText(this.dataset[i]);
	}

	@Override
	public int getItemCount()
	{
		return this.dataset.length;
	}
}
