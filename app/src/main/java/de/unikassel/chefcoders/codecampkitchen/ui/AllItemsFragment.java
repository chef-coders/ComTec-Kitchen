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

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.recyclerview.ItemAdapter;
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
		    	MainActivity mainActivity = (MainActivity) getActivity();
				if (mainActivity != null) {
					mainActivity.changeFragment(new ConfirmPurchasesFragment());
					mainActivity.checkAllItemsMenuItem(false);
				}
			}
	    });
    }

    private void initRecyclerView(View allItemsView)
    {
	    RecyclerView recyclerView = allItemsView.findViewById(R.id.allItemsRecView);

	    recyclerView.setHasFixedSize(true);

	    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
	    recyclerView.setLayoutManager(layoutManager);

	    Item fanta = new Item()
			    .setName("Fanta")
			    .setKind("Softdrink")
			    .setPrice(3.4)
			    .setAmount(-1);
	    Item cola = new Item()
			    .setName("Cola")
			    .setKind("Softdrink")
			    .setPrice(5.0)
			    .setAmount(5);
	    Item water = new Item()
			    .setName("Wasser")
			    .setKind("Softdrink")
			    .setPrice(1.435345);
	    Item sprite = new Item()
			    .setName("Sprite")
			    .setKind("Softdrink")
			    .setPrice(Double.NaN)
			    .setAmount(3);

	    Item latte = new Item()
			    .setName("Latte")
			    .setKind("Kaffee")
			    .setPrice(Double.NEGATIVE_INFINITY)
			    .setAmount(10);
	    Item kaffee = new Item()
			    .setName("Kaffee")
			    .setKind("Kaffee")
			    .setPrice(0.0000)
			    .setAmount(13);

	    Item beer = new Item()
			    .setName("Beer")
			    .setKind("Alcoholic")
			    .setPrice(39)
			    .setAmount(3);
	    Item cognac = new Item()
			    .setName("Cognac")
			    .setKind("Alcoholic")
			    .setPrice(1)
			    .setAmount(-1);

	    List<Item> items = new ArrayList<>();
	    items.add(fanta);
	    items.add(cola);
	    items.add(water);
	    items.add(sprite);
	    items.add(latte);
	    items.add(kaffee);
	    items.add(beer);
	    items.add(cognac);

	    ItemAdapter itemAdapter = new ItemAdapter();
	    itemAdapter.setItems(items);
	    recyclerView.setAdapter(itemAdapter);
    }
}
