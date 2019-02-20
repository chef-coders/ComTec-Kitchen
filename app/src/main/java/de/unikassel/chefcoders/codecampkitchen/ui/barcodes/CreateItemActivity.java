package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public class CreateItemActivity extends AppCompatActivity
{
	private String barcode;

	private EditText nameText;
	private EditText priceText;
	private EditText amountText;
	private EditText kindText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);

		barcode = (String) getIntent().getExtras().get("barcode");

		this.nameText = findViewById(R.id.nameText);
		this.priceText = findViewById(R.id.priceText);
		this.amountText = findViewById(R.id.amountText);
		this.kindText = findViewById(R.id.kindText);
	}

	public void onCreate(View view) {
		try {
			double price = Double.parseDouble(priceText.getText().toString());
			int amount = Integer.parseInt(amountText.getText().toString());
			MainActivity.kitchenManager.createItem(barcode, nameText.getText().toString(), price, amount, kindText.getText().toString());
			startActivity(new Intent(CreateItemActivity.this, MainActivity.class));
		} catch (Exception ex) {
			priceText.setText("0.00");
			amountText.setText("0");
		}
	}
}
