package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class CreateItemActivity extends AppCompatActivity
{
	private String barcode;

	private TextView barcodeValue;
	private EditText nameText;
	private EditText priceText;
	private EditText amountText;
	private Spinner kindSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			barcode = (String) bundle.get("barcode");
		} else {
			Double doubleValue = Math.floor(Math.random() * (99999999999L - 10000000000L + 1L)) + 10000000000L;
			Long longValue = doubleValue.longValue();
			barcode = "Generated:" + longValue.toString();
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
				ItemKind.createEntries(CreateItemActivity.this)
			)
		);

		this.barcodeValue.setText(barcode);
	}

	public void onCreate(View view) {
		try {
			String name = nameText.getText().toString();
			double price = Double.parseDouble(priceText.getText().toString());
			int amount = Integer.parseInt(amountText.getText().toString());
			ItemKind.Entry selectedEntry = (ItemKind.Entry) kindSpinner.getSelectedItem();
			String kind = selectedEntry.getValue();
			new SimpleAsyncTask(() -> {
				MainActivity.kitchenManager.createItem(barcode, name, price, amount, kind);
			}, this::startMainActivity).execute();
		} catch (Exception ex) {
			priceText.setText("0.00");
			amountText.setText("0");
		}
	}

	private void startMainActivity() {
		finish();
		startActivity(new Intent(CreateItemActivity.this, MainActivity.class));
	}
}
