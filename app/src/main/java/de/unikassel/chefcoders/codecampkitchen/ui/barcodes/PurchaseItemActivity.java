package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.vision.barcode.Barcode;

import de.unikassel.chefcoders.codecampkitchen.R;

public class PurchaseItemActivity extends AppCompatActivity
{
	private Barcode barcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_item);

		barcode = barcode = (Barcode) getIntent().getSerializableExtra("barcode");
	}
}
