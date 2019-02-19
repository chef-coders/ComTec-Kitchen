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

import de.unikassel.chefcoders.codecampkitchen.R;
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

	    String[] softDrinks = {
			    "Fanta", "Cola", "Wasser", "Sprite", "Zitronensaft", "Zaubersaft",
			    "Apfel-Schorle", "Energy-Drink", "Malzbier", "Weihwasser", "Dreckbrühe", "Kirschschorle"
	    };

	    String[] coffees = {
			    "Latte", "Expresso", "Yellow Tea"
	    };

	    String[] alcoholics = {
			    "Bier", "Champagner", "Schaps", "Cogniak"
	    };

	    SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
	    sectionAdapter.addSection("Soft Drinks", new ItemSection(softDrinks, "Soft Drinks"));
	    sectionAdapter.addSection("Coffees", new ItemSection(coffees, "Coffees"));
	    sectionAdapter.addSection("Alcoholic drinks", new ItemSection(alcoholics, "Alcoholic drinks"));
	    recyclerView.setAdapter(sectionAdapter);
    }
}
