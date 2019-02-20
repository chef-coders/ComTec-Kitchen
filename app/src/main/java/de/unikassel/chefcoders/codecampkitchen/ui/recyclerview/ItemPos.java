package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;

public class ItemPos
{
	private Section section;
	private int sectionId;
	private int itemId;

	public ItemPos(Section section, int sectionId, int itemId)
	{
		this.section = section;
		this.sectionId = sectionId;
		this.itemId = itemId;
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

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	public String toString()
	{
		return "itemId: " + this.itemId + " sectionId: " + sectionId;
	}
}
