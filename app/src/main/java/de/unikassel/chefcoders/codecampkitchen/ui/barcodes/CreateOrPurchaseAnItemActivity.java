package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.vision.barcode.Barcode;

import de.unikassel.chefcoders.codecampkitchen.R;

public class CreateOrPurchaseAnItemActivity extends AppCompatActivity
{
	private Barcode barcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_purchase_an_item);

		barcode = barcode = (Barcode) getIntent().getSerializableExtra("barcode");
	}

	public void onCreateItem(View view) {
		Intent intent = new Intent(CreateOrPurchaseAnItemActivity.this, CreateItemActivity.class);
		intent.putExtra("barcode", barcode);
		startActivity(intent);
	}

	public void onPurchaseItem(View view) {
		Intent intent = new Intent(CreateOrPurchaseAnItemActivity.this, PurchaseItemActivity.class);
		intent.putExtra("barcode", barcode);
		startActivity(intent);
	}
}
