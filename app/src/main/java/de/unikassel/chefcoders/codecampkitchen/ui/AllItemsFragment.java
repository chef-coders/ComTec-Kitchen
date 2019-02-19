package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AllItemsFragment extends Fragment
{
	private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
	    View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

	    this.initFloatingActionButton(allItemsView);
	    this.initRecyclerView(allItemsView);

	    return allItemsView;
    }

    private void initFloatingActionButton(View allItemsView)
    {
	    floatingActionButton = allItemsView.findViewById(R.id.buyItemButton);
	    floatingActionButton.setOnClickListener(new View.OnClickListener()
	    {
		    @Override
		    public void onClick(View v)
		    {
			    //TODO
		    }
	    });
    }

    private void initRecyclerView(View allItemsView)
    {
	    RecyclerView recyclerView = allItemsView.findViewById(R.id.allItemsRecView);

	    recyclerView.setHasFixedSize(true);

	    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
	    recyclerView.setLayoutManager(layoutManager);

	    List<Item> softDrinks = new ArrayList<>();
	    Item fanta = new Item()
			    .setName("Fanta");
	    softDrinks.add(fanta);
	    Item cola = new Item()
			    .setName("Cola");
	    softDrinks.add(cola);
	    Item water = new Item()
			    .setName("Wasser");
	    softDrinks.add(water);
	    Item sprite = new Item()
			    .setName("Sprite");
	    softDrinks.add(sprite);

	    List<Item> coffees = new ArrayList<>();
	    Item latte = new Item()
			    .setName("Latte");
	    coffees.add(latte);
	    Item kaffee = new Item()
			    .setName("Kaffee");
	    coffees.add(kaffee);

	    for(Item coffee : coffees)
	    {
		    coffee.setKind("Kaffee");
	    }

	    SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
	    sectionAdapter.addSection("Softdrinks", new ItemSection(softDrinks, "Soft Drinks"));
	    sectionAdapter.addSection("Kaffee", new ItemSection(coffees, "Coffees"));
	    recyclerView.setAdapter(sectionAdapter);
    }
}
