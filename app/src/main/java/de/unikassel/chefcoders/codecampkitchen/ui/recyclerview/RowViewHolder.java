package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.R;

public class RowViewHolder extends RecyclerView.ViewHolder
{
	public final View     itemView;
	public final TextView titleTextView;
	public final TextView priceTextView;
	public final TextView amountTextView;
	public final TextView numSelectedTextView;

	public RowViewHolder(View itemView)
	{
		super(itemView);

		this.itemView = itemView;
		this.titleTextView = itemView.findViewById(R.id.item_title);
		this.priceTextView = itemView.findViewById(R.id.item_price);
		this.amountTextView = itemView.findViewById(R.id.item_amount);
		this.numSelectedTextView = itemView.findViewById(R.id.item_num_selected);

		this.itemView.setBackgroundColor(Color.parseColor("#e5e5e5"));
	}
}
