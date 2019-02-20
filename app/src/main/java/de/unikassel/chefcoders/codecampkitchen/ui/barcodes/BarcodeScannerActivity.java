package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import info.androidhive.barcode.BarcodeReader;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener
{
	private BarcodeReader barcodeReader;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_scanner);

		barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcodeScanner);
	}

	@Override
	public void onScanned(Barcode barcode) {
		boolean isAdmin = MainActivity.kitchenManager.isAdmin();
		boolean itemExists = MainActivity.kitchenManager.containsItem(barcode.rawValue);
		if (isAdmin && !itemExists) {
			Intent intent = new Intent(BarcodeScannerActivity.this, CreateItemActivity.class);
			intent.putExtra("barcode", barcode);
			startActivity(intent);
		} else if (itemExists) {
			Intent intent = new Intent(BarcodeScannerActivity.this, PurchaseItemActivity.class);
			intent.putExtra("barcode", barcode);
			startActivity(intent);
		}
	}

	@Override
	public void onScannedMultiple(List<Barcode> list) {
	}

	@Override
	public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
	}

	@Override
	public void onScanError(String s) {
		Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCameraPermissionDenied() {
		finish();
	}
}
