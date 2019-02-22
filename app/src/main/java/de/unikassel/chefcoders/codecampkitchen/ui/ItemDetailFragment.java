package de.unikassel.chefcoders.codecampkitchen.ui;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;

public abstract class ItemDetailFragment extends KitchenFragment
{
	protected TextView barcodeValue;
	protected EditText nameText;
	protected EditText priceText;
	protected EditText amountText;
	protected Spinner kindSpinner;

	protected void initViews(View itemDetailView)
	{
		this.barcodeValue = itemDetailView.findViewById(R.id.barcodeValueView);
		this.nameText = itemDetailView.findViewById(R.id.nameText);
		this.priceText = itemDetailView.findViewById(R.id.priceText);
		this.amountText = itemDetailView.findViewById(R.id.amountText);
		this.kindSpinner = itemDetailView.findViewById(R.id.kindSpinner);

		this.kindSpinner.setAdapter(
				new ArrayAdapter<>(
						this.getActivity(),
						android.R.layout.simple_spinner_dropdown_item,
						ItemKind.createEntries(this.getContext())
				)
		);
	}
}
