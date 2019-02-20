package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public class CreateItemActivity extends AppCompatActivity
{
	private Barcode barcode;

	private EditText nameText;
	private EditText priceText;
	private EditText amountText;
	private EditText kindText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);

		barcode = (Barcode) getIntent().getSerializableExtra("barcode");

		this.nameText = findViewById(R.id.nameText);
		this.priceText = findViewById(R.id.priceText);
		this.amountText = findViewById(R.id.amountText);
		this.kindText = findViewById(R.id.kindText);
	}

	public void onCreate(View view) {
		try {
			double price = Double.parseDouble(priceText.getText().toString());
			int amount = Integer.parseInt(amountText.getText().toString());
			MainActivity.kitchenManager.createItem(barcode.rawValue, nameText.getText().toString(), price, amount, kindText.getText().toString());
		} catch (Exception ex) {
			priceText.setText("0.00");
			amountText.setText("0");
			Toast.makeText(getApplicationContext(), "Price or amount is not a valid number", Toast.LENGTH_SHORT).show();
		}
	}
}
