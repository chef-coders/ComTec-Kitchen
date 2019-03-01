package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.list.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseItemFragment extends KitchenFragment
{
	private String barcode;

	private TextView textViewName;
	private TextView textViewPrice;
	private TextView textViewAvailable;

	private TextView barcodeValue;
	private TextView amountText;

	private int availableAmountToAdd;

	public PurchaseItemFragment()
	{
		// Required empty public constructor
	}

	public static PurchaseItemFragment newInstance(String barcode)
	{
		PurchaseItemFragment fragment = new PurchaseItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString("barcode", barcode);
		fragment.setArguments(bundle);
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_purchase_item, container, false);

		this.barcode = getArguments().getString("barcode");

		Item item = MainActivity.kitchenManager.items().get(this.barcode);
		int amountInCart = MainActivity.kitchenManager.cart().getAmount(item);
		this.availableAmountToAdd = item.getAmount() - amountInCart;

		this.barcodeValue = view.findViewById(R.id.barcodeValueView);
		this.amountText = view.findViewById(R.id.amountText);
		this.textViewName = view.findViewById(R.id.textViewName);
		this.textViewPrice = view.findViewById(R.id.textViewPrice);
		this.textViewAvailable = view.findViewById(R.id.availableTextView);

		this.textViewAvailable.setText("" + this.availableAmountToAdd);
		textViewName.setText(item.getName());
		textViewPrice.setText(getString(R.string.item_price, item.getPrice()));

		this.barcodeValue.setText(barcode);

		view.findViewById(R.id.addButton)
				.setOnClickListener(this::onAdd);

		view.findViewById(R.id.minusButton)
				.setOnClickListener(this::onSub);

		view.findViewById(R.id.buttonSave)
				.setOnClickListener(this::onPurchase);

		return view;
	}

	public void onAdd(View view)
	{
		this.add(1);
	}

	public void onSub(View view)
	{
		this.add(-1);
	}

	private void add(int value)
	{
		try {
			int amount = Integer.parseInt(this.amountText.getText().toString());
			amount = amount + value;

			if(amount < 1 || amount > this.availableAmountToAdd)
			{
				return;
			}

			this.amountText.setText("" + amount);
		} catch (NumberFormatException ex) {
			this.amountText.setText("1");
		}
	}

	public void onPurchase(View view)
	{
		final int amount;

		try {
			amount = Integer.parseInt(this.amountText.getText().toString());
		} catch (NumberFormatException ex) {
			this.amountText.setText("1");
			return;
		}

		SimpleAsyncTask.execute(this.getActivity(),
		                        () -> MainActivity.kitchenManager.cart().add(
			                        MainActivity.kitchenManager.items().get(barcode), amount),
				this::goBack
		);
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.add_to_cart);
	}

	private void goBack()
	{
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity != null) {
			mainActivity.changeFragmentBack(new AllItemsFragment());
		}
	}

}
