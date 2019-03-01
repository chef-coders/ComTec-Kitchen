package de.unikassel.chefcoders.codecampkitchen.ui;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;

public abstract class ItemDetailFragment extends KitchenFragment
{
	protected TextView barcodeTextView;
	protected EditText nameText;
	protected EditText priceText;
	protected EditText amountText;
	protected Spinner  kindSpinner;

	protected void initViews(View itemDetailView)
	{
		this.barcodeTextView = itemDetailView.findViewById(R.id.barcodeValueView);
		this.nameText = itemDetailView.findViewById(R.id.nameText);
		this.priceText = itemDetailView.findViewById(R.id.priceText);
		this.amountText = itemDetailView.findViewById(R.id.amountText);
		this.kindSpinner = itemDetailView.findViewById(R.id.kindSpinner);

		this.kindSpinner.setAdapter(
			new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item,
			                   ItemKind.createEntries(this.getContext())));
	}

	protected Item getItem()
	{
		final String itemId = this.barcodeTextView.getText().toString();
		final String name = this.nameText.getText().toString();

		double price = 0.0;
		int amount = 0;
		final ItemKind.Entry selectedEntry = (ItemKind.Entry) this.kindSpinner.getSelectedItem();
		final String kind = selectedEntry.getValue();

		boolean formatException = false;
		try
		{
			price = Double.parseDouble(this.priceText.getText().toString());
		}
		catch (NumberFormatException ex)
		{
			this.priceText.setText("0.00");
			formatException = true;
		}

		try
		{
			amount = Integer.parseInt(this.amountText.getText().toString());
		}
		catch (NumberFormatException ex)
		{
			this.amountText.setText("1");
			formatException = true;
		}

		if(formatException)
		{
			return null;
		}

		if(amount <= 0)
		{
			Toast.makeText(this.getContext(), R.string.amount_minimum_error, Toast.LENGTH_LONG)
					.show();
			this.amountText.setText("1");
			return null;
		}

		return new Item().set_id(itemId).setName(name).setPrice(price).setAmount(amount).setKind(kind);
	}
}
