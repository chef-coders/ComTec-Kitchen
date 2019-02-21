package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class PurchaseItemActivity extends AppCompatActivity
{
	private String barcode;

	private TextView barcodeValue;
	private EditText amountText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_item);

		barcode = (String) getIntent().getExtras().get("barcode");

		this.barcodeValue = findViewById(R.id.barcodeValueView);
		this.amountText = findViewById(R.id.amountText);

		this.barcodeValue.setText(barcode);
	}

	public void onAdd(View view) {
		this.add(1);
	}

	public void onSub(View view) {
		this.add(-1);
	}

	private void add(int value) {
		try {
			int amount = Integer.parseInt(this.amountText.getText().toString());
			amount = amount + value;
			this.amountText.setText("" + amount);
		} catch (Exception ex) {
			this.amountText.setText("1");
		}
	}

	public void onPurchase(View view) {
		try {
			int amount = Integer.parseInt(this.amountText.getText().toString());
			new SimpleAsyncTask(() -> {
				MainActivity.kitchenManager.addToCart(MainActivity.kitchenManager.getItemById(barcode), amount);
			}, this::startMainActivity).execute();
		} catch (Exception ex) {
			this.amountText.setText("1");
		}
	}

	private void startMainActivity() {
		finish();
		startActivity(new Intent(PurchaseItemActivity.this, MainActivity.class));
	}
}
