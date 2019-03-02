package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.RecyclerController;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class GeneralSection extends StatelessSection
{
	// =============== Fields ===============

	private final RecyclerController recyclerController;

	private final int index;

	// =============== Constructors ===============

	public GeneralSection(RecyclerController recyclerController, int index)
	{
		super(SectionParameters.builder().itemResourceId(R.layout.item_view).headerResourceId(R.layout.header_view)
		                       .build());

		this.recyclerController = recyclerController;
		this.index = index;
	}

	// =============== Properties ===============

	public int getIndex()
	{
		return this.index;
	}

	// =============== Methods ===============

	@Override
	public int getContentItemsTotal()
	{
		return this.recyclerController.getItems(this.index);
	}

	@Override
	public RecyclerView.ViewHolder getItemViewHolder(View view)
	{
		return this.recyclerController.create(view);
	}

	@Override
	public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		this.recyclerController.populate(holder, this.index, position);
	}

	@Override
	public RecyclerView.ViewHolder getHeaderViewHolder(View view)
	{
		return new SectionHolder(view);
	}

	@Override
	public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
	{
		((SectionHolder) holder).setTitle(this.recyclerController.getHeader(this.index));
	}
}
