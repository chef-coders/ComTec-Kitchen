package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.R;

public class ItemViewHolder extends RecyclerView.ViewHolder
{
	public TextView textView;

	public ItemViewHolder(View itemView)
	{
		super(itemView);
		this.textView = itemView.findViewById(R.id.text);
	}

	public void showText(String s)
	{
		this.textView.setText(s);
	}
}
