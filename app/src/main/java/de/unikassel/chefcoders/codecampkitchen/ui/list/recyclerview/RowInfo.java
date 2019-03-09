package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public interface RowInfo<VH extends RecyclerView.ViewHolder>
{
	// =============== Static Methods ===============

	@Nullable
	static <VH extends RecyclerView.ViewHolder> RowInfo<VH> fromPos(RecyclerView recyclerView, float x, float y)
	{
		final View child = recyclerView.findChildViewUnder(x, y);
		if (child == null)
		{
			return null;
		}
		final VH viewHolder = (VH) recyclerView.getChildViewHolder(child);
		if (viewHolder == null)
		{
			return null;
		}
		return from(recyclerView, viewHolder);
	}

	static <VH extends RecyclerView.ViewHolder> RowInfo<VH> from(RecyclerView recyclerView, VH viewHolder)
	{
		final int adapterPosition = viewHolder.getAdapterPosition();
		final Section section;
		final int sectionIndex;
		final int rowIndex;

		final RecyclerView.Adapter adapter = recyclerView.getAdapter();

		if (adapter instanceof SectionedRecyclerViewAdapter)
		{
			final SectionedRecyclerViewAdapter sectionedAdapter = (SectionedRecyclerViewAdapter) adapter;

			section = sectionedAdapter.getSectionForPosition(adapterPosition);
			rowIndex = sectionedAdapter.getPositionInSection(adapterPosition);
			sectionIndex = section instanceof CollapsibleSection ?
				               ((CollapsibleSection) section).getIndex() :
				               Integer.MIN_VALUE;
		}
		else
		{
			section = null;
			rowIndex = Integer.MIN_VALUE;
			sectionIndex = Integer.MIN_VALUE;
		}

		return new RowInfo<VH>()
		{
			@Override
			public RecyclerView getRecyclerView()
			{
				return recyclerView;
			}

			@Override
			public VH getViewHolder()
			{
				return viewHolder;
			}

			@Override
			public int getAdapterPosition()
			{
				return adapterPosition;
			}

			@Override
			public Section getSection()
			{
				return section;
			}

			@Override
			public int getSectionIndex()
			{
				return sectionIndex;
			}

			@Override
			public int getRowIndex()
			{
				return rowIndex;
			}
		};
	}

	// =============== Methods ===============

	// --------------- General ---------------

	RecyclerView getRecyclerView();

	VH getViewHolder();

	int getAdapterPosition();

	// --------------- for Sectioned Recycler Views ---------------

	Section getSection();

	int getSectionIndex();

	int getRowIndex();
}
