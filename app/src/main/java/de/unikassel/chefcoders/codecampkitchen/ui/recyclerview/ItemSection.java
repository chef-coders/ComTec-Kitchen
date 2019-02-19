package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.unikassel.chefcoders.codecampkitchen.R;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class ItemSection extends StatelessSection
{
	private String[] dataset;
	private String sectionName;

	public ItemSection(String[] dataset, String sectionName)
	{
		super(SectionParameters.builder()
				.itemResourceId(R.layout.item_view)
				.headerResourceId(R.layout.header_view)
				.build());

		this.dataset = dataset;
		this.sectionName = sectionName;
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

	@Override
	public RecyclerView.ViewHolder getHeaderViewHolder(View view)
	{
		return new SectionHolder(view);
	}

	@Override
	public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
	{
		((SectionHolder)holder).setTitle(this.sectionName);
	}
}
