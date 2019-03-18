package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.view.View;
import android.widget.*;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;

public abstract class ItemDetailFragment extends KitchenFragment
{
	protected TextView barcodeTextView;
	protected EditText nameText;
	protected EditText priceText;
	protected EditText amountText;
	protected Spinner  kindSpinner;

	public ItemDetailFragment(int titleRes)
	{
		super(titleRes, true);
	}

	protected void initViews(View itemDetailView)
	{
		this.barcodeTextView = itemDetailView.findViewById(R.id.barcodeValueView);
		this.nameText = itemDetailView.findViewById(R.id.nameText);
		this.priceText = itemDetailView.findViewById(R.id.priceText);
		this.amountText = itemDetailView.findViewById(R.id.amountText);
		this.kindSpinner = itemDetailView.findViewById(R.id.kindSpinner);

		DisableButtonTextWatcher
			.bind(this.saveButton, this::isInputValid, this.barcodeTextView, this.nameText, this.priceText,
			      this.amountText);

		this.kindSpinner.setAdapter(
			new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item,
			                   ItemKind.createEntries(this.getContext())));
	}

	protected Item getItem()
	{
		final String itemId = this.barcodeTextView.getText().toString();
		final String name = this.nameText.getText().toString();

		final double price = Double.parseDouble(this.priceText.getText().toString());
		final int amount = Integer.parseInt(this.amountText.getText().toString());
		final ItemKind.Entry selectedEntry = (ItemKind.Entry) this.kindSpinner.getSelectedItem();
		final String kind = selectedEntry.getValue();

		int maxPrice = this.getResources().getInteger(R.integer.max_price);
		if (price > (double) maxPrice)
		{
			Toast.makeText(this.getContext(), getString(R.string.price_max_error, maxPrice), Toast.LENGTH_LONG).show();
			this.priceText.setText("" + maxPrice);
			return null;
		}

		int minAmount = this.getResources().getInteger(R.integer.min_amount);
		if (amount < minAmount)
		{
			Toast.makeText(this.getContext(), getString(R.string.amount_minimum_error, minAmount), Toast.LENGTH_LONG)
			     .show();
			this.amountText.setText("" + minAmount);
			return null;
		}

		return new Item().set_id(itemId).setName(name).setPrice(price).setAmount(amount).setKind(kind);
	}

	protected boolean isInputValid()
	{
		if (this.nameText.getText().length() == 0 || this.priceText.getText().length() == 0
		    || this.amountText.getText().length() == 0)
		{
			return false;
		}

		try
		{
			Integer.parseInt(this.amountText.getText().toString());
			Double.parseDouble(this.priceText.getText().toString());
			return true;
		}
		catch (NumberFormatException ex)
		{
			return false;
		}
	}
}
