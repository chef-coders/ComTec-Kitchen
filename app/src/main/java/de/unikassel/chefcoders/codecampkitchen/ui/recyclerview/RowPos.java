package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;

public class RowPos
{
	private Section section;
	private int sectionId;
	private int rowId;

	public RowPos(Section section, int sectionId, int rowId)
	{
		this.section = section;
		this.sectionId = sectionId;
		this.rowId = rowId;
	}

	public Section getSection()
	{
		return section;
	}

	public void setSection(Section section)
	{
		this.section = section;
	}

	public int getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(int sectionId)
	{
		this.sectionId = sectionId;
	}

	public int getRowId()
	{
		return rowId;
	}

	public void setRowId(int itemId)
	{
		this.rowId = itemId;
	}

	@Override
	public String toString()
	{
		return "rowId: " + this.rowId + " sectionId: " + sectionId;
	}
}
