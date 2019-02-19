package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder
{
	private View itemView;
	private TextView textView;

	public ItemViewHolder(View itemView)
	{
		super(itemView);
		this.itemView = itemView;
		this.textView = itemView.findViewById(R.id.title);
		this.itemView.setBackgroundColor(Color.parseColor("#e5e5e5"));
	}

	public void show(Item item)
	{
		this.textView.setText(item.getName());
	}
}
