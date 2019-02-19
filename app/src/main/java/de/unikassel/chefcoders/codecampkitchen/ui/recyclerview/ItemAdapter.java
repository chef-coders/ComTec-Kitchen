package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.unikassel.chefcoders.codecampkitchen.model.Item;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ItemAdapter extends SectionedRecyclerViewAdapter
{
	public void setItems(List<Item> items)
	{
		Map<String, List<Item>> sections = new HashMap<>();

		for(Item item : items)
		{
			String kind = item.getKind();
			List<Item> sectionedItems = sections.get(kind);
			if(sectionedItems == null)
			{
				sectionedItems = new ArrayList<>();
				sections.put(kind, sectionedItems);
			}
			sectionedItems.add(item);
		}

		for(List<Item> sectionedItems : sections.values())
		{
			String kind = sectionedItems.get(0).getKind();
			this.addSection(kind, new ItemSection(sectionedItems, kind));
		}
	}
}
