package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind.Entry;
import de.unikassel.chefcoders.codecampkitchen.ui.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class EditItemFragment extends KitchenFragment
{
	private String itemId;
	private Item item;

	private TextView barcodeValue;
	private EditText nameText;
	private EditText priceText;
	private EditText amountText;
	private Spinner kindSpinner;

	public static EditItemFragment newInstance(String itemId)
	{
		EditItemFragment fragment = new EditItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString("itemId", itemId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View editItemsView = inflater.inflate(R.layout.activity_edit_item, container, false);

		this.itemId = getArguments().getString("itemId");

		this.item = MainActivity.kitchenManager.getItemById(this.itemId);

		this.barcodeValue = editItemsView.findViewById(R.id.barcodeValueView);
		this.nameText = editItemsView.findViewById(R.id.nameText);
		this.priceText = editItemsView.findViewById(R.id.priceText);
		this.amountText = editItemsView.findViewById(R.id.amountText);
		this.kindSpinner = editItemsView.findViewById(R.id.kindSpinner);
		Button editButton = editItemsView.findViewById(R.id.editButton);
		editButton.setOnClickListener(this::onEdit);

		this.kindSpinner.setAdapter(
			new ArrayAdapter<>(
				this.getActivity(),
				android.R.layout.simple_spinner_dropdown_item,
				ItemKind.createEntries(this.getContext())
			)
		);

		this.barcodeValue.setText(item.get_id());
		this.nameText.setText(item.getName());
		this.priceText.setText("" + item.getPrice());
		this.amountText.setText("" + item.getAmount());
		this.kindSpinner.setSelection(ItemKind.getIndex(item.getKind()));

		return editItemsView;
	}

	public void onEdit(View view) {
		final String name = nameText.getText().toString();
		final double price;
		final int amount;
		final Entry selectedEntry = (Entry) kindSpinner.getSelectedItem();
		final String kind = selectedEntry.getValue();

		try {
			price = Double.parseDouble(priceText.getText().toString());
			amount = Integer.parseInt(amountText.getText().toString());
		} catch (NumberFormatException ex) {
			priceText.setText("0.00");
			amountText.setText("0");
			return;
		}

		SimpleAsyncTask.execute(this.getContext(),
			() -> MainActivity.kitchenManager.updateItem(itemId, name, price, amount, kind),
			() -> {
				MainActivity mainActivity = (MainActivity)this.getActivity();
				mainActivity.changeFragment(new AllItemsFragment());
			}
		);
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle("Edit item");
	}
}
