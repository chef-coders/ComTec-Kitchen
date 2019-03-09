package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.RecyclerController;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class CollapsibleSection extends StatelessSection
{
	// =============== Fields ===============

	private final RecyclerController controller;

	private int index;

	private boolean collapsed;

	// =============== Constructors ===============

	public CollapsibleSection(RecyclerController controller, int index)
	{
		super(SectionParameters.builder().itemResourceId(R.layout.item_view).headerResourceId(R.layout.header_view)
		                       .build());

		this.controller = controller;
		this.index = index;
	}

	// =============== Properties ===============

	public int getIndex()
	{
		return this.index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public boolean isCollapsed()
	{
		return this.collapsed;
	}

	public void setCollapsed(boolean collapsed)
	{
		this.collapsed = collapsed;
	}

	// =============== Methods ===============

	@Override
	public int getContentItemsTotal()
	{
		return this.isCollapsed() ? 0 : this.controller.getItems(this.index);
	}

	@Override
	public RecyclerView.ViewHolder getItemViewHolder(View view)
	{
		return this.controller.create(view);
	}

	@Override
	public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		this.controller.populate(holder, this.index, position);
	}

	@Override
	public RecyclerView.ViewHolder getHeaderViewHolder(View view)
	{
		return new HeaderViewHolder(view);
	}

	@Override
	public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
	{
		((HeaderViewHolder) holder).setTitle(this.controller.getHeader(this.index));
	}
}
