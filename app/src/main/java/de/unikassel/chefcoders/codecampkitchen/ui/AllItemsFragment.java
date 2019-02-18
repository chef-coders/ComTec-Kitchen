package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemSection;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class AllItemsFragment extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
	    View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

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
	    sectionAdapter.addSection(new ItemSection(softDrinks, "Soft Drinks"));
	    sectionAdapter.addSection(new ItemSection(coffees, "Coffees"));
	    sectionAdapter.addSection(new ItemSection(alcoholics, "Alcoholic drinks"));
		recyclerView.setAdapter(sectionAdapter);

	    return allItemsView;
    }
}
