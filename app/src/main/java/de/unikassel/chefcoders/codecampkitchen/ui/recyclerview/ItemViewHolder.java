package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.R;

public class ItemViewHolder extends RecyclerView.ViewHolder
{
	public final View     itemView;
	public final TextView titleTextView;
	public final TextView prizeTextView;
	public final TextView amountTextView;
	public final TextView numSelectedTextView;

	public ItemViewHolder(View itemView)
	{
		super(itemView);

		this.itemView = itemView;
		this.titleTextView = itemView.findViewById(R.id.item_title);
		this.prizeTextView = itemView.findViewById(R.id.item_prize);
		this.amountTextView = itemView.findViewById(R.id.item_amount);
		this.numSelectedTextView = itemView.findViewById(R.id.item_num_selected);

		this.itemView.setBackgroundColor(Color.parseColor("#e5e5e5"));
	}
}
