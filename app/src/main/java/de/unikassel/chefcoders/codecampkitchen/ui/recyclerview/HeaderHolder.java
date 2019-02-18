package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.R;

public class HeaderHolder extends RecyclerView.ViewHolder
{
	private TextView textView;

	public HeaderHolder(View itemView)
	{
		super(itemView);
		this.textView = itemView.findViewById(R.id.section_title);
	}

	public void setTitle(String title)
	{
		this.textView.setText(title);
	}
}
