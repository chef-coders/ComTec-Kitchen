package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class EditItemActivity extends AppCompatActivity
{
	private String itemId;
	private Item item;

	private TextView barcodeValue;
	private EditText nameText;
	private EditText priceText;
	private EditText amountText;
	private Spinner kindSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		Toolbar toolbar = this.findViewById(R.id.edit_item_toolbar);
		this.setSupportActionBar(toolbar);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			itemId = (String) bundle.get("itemId");
			item = MainActivity.kitchenManager.getItemById(itemId);
		} else {
			return;
		}

		this.barcodeValue = findViewById(R.id.barcodeValueView);
		this.nameText = findViewById(R.id.nameText);
		this.priceText = findViewById(R.id.priceText);
		this.amountText = findViewById(R.id.amountText);
		this.kindSpinner = findViewById(R.id.kindSpinner);

		this.kindSpinner.setAdapter(
			new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_dropdown_item,
				ItemKind.createEntries(EditItemActivity.this)
			)
		);

		this.barcodeValue.setText(item.get_id());
		this.nameText.setText(item.getName());
		this.priceText.setText("" + item.getPrice());
		this.amountText.setText("" + item.getAmount());
		this.kindSpinner.setSelection(ItemKind.getIndex(item.getKind()));
	}

	public void onEdit(View view) {
		final String name = nameText.getText().toString();
		final double price;
		final int amount;
		final ItemKind.Entry selectedEntry = (ItemKind.Entry) kindSpinner.getSelectedItem();
		final String kind = selectedEntry.getValue();

		try {
			price = Double.parseDouble(priceText.getText().toString());
			amount = Integer.parseInt(amountText.getText().toString());
		} catch (Exception ex) {
			priceText.setText("0.00");
			amountText.setText("0");
			return;
		}

		SimpleAsyncTask.execute(this.getApplicationContext(),
			() -> MainActivity.kitchenManager.updateItem(itemId, name, price, amount, kind),
			this::startMainActivity
		);
	}

	private void startMainActivity() {
		finish();
		startActivity(new Intent(EditItemActivity.this, MainActivity.class));
	}
}
