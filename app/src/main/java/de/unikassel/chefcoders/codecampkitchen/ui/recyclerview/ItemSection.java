package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.unikassel.chefcoders.codecampkitchen.R;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class ItemSection extends StatelessSection
{
	private String[] dataset;

	public ItemSection(String[] dataset)
	{
		super(SectionParameters.builder()
				.itemResourceId(R.layout.item_view)
				.headerResourceId(R.layout.header_view)
				.build());
		this.dataset = dataset;
	}

	@Override
	public int getContentItemsTotal()
	{
		return this.dataset.length;
	}

	@Override
	public RecyclerView.ViewHolder getItemViewHolder(View view)
	{
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
		itemViewHolder.showText(this.dataset[position]);
	}
}
