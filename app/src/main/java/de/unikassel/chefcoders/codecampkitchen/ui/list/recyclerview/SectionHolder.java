package de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.R;

public class SectionHolder extends RecyclerView.ViewHolder
{
	private TextView textView;

	public SectionHolder(View itemView)
	{
		super(itemView);
		this.textView = itemView.findViewById(R.id.section_title);
	}

	public void setTitle(String title)
	{
		this.textView.setText(title);
	}
}
