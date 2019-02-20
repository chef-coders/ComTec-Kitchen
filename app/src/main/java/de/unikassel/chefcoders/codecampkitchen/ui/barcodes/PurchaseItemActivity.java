package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;

public class PurchaseItemActivity extends AppCompatActivity
{
	private Barcode barcode;

	private EditText amountText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_item);

		barcode = barcode = (Barcode) getIntent().getSerializableExtra("barcode");

		this.amountText = findViewById(R.id.amountText);
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
			this.amountText.setText(amount);
		} catch (Exception ex) {
			this.amountText.setText("1");
		}
	}

	public void onPurchase(View view) {
		try {
			int amount = Integer.parseInt(this.amountText.getText().toString());
			MainActivity.kitchenManager.buyItem(MainActivity.kitchenManager.getItemById(barcode.rawValue), amount);
		} catch (Exception ex) {
			this.amountText.setText("1");
			Toast.makeText(getApplicationContext(), "Amount is not a valid number", Toast.LENGTH_SHORT).show();
		}
	}
}
