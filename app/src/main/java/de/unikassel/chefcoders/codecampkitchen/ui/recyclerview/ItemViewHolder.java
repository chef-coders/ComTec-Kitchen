package de.unikassel.chefcoders.codecampkitchen.ui.recyclerview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder
{
	private View itemView;
	private TextView titleTextView;
	private TextView prizeTextView;
	private TextView amountTextView;

	public ItemViewHolder(View itemView)
	{
		super(itemView);
		this.itemView = itemView;
		this.titleTextView = itemView.findViewById(R.id.item_title);
		this.prizeTextView = itemView.findViewById(R.id.item_prize);
		this.amountTextView = itemView.findViewById(R.id.item_amount);

		this.itemView.setBackgroundColor(Color.parseColor("#e5e5e5"));
	}

	public void show(Item item)
	{
		if(item == null)
		{
			return;
		}

		if(item.getName() != null)
		{
			this.titleTextView.setText(item.getName());
		}

		if(item.getPrice() > 0.0)
		{
			this.prizeTextView.setText(String.format("%.2f €", item.getPrice()));
		}
		else
		{
			this.prizeTextView.setText("-");
		}

		if(item.getAmount() != 0)
		{
			this.amountTextView.setText(item.getAmount() + " available");
		}
		else
		{
			this.amountTextView.setText("Not available");
		}
	}
}
