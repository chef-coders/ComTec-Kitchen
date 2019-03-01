package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.Toast;
import com.google.android.gms.vision.barcode.Barcode;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import info.androidhive.barcode.BarcodeReader;

import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener
{
	private BarcodeReader barcodeReader;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_scanner);

		this.barcodeReader = (BarcodeReader) this.getSupportFragmentManager().findFragmentById(R.id.barcodeScanner);
	}

	@Override
	public void onScanned(Barcode barcode)
	{
		boolean isAdmin = MainActivity.kitchenManager.session().isAdmin();
		boolean itemExists = MainActivity.kitchenManager.items().exists(barcode.rawValue);

		if (itemExists)
		{
			this.scannedExistingItem(barcode);
		}
		else if (isAdmin)
		{
			this.adminScannedNewItem(barcode);
		}
		else
		{
			Toast.makeText(this.getApplicationContext(), this.getString(R.string.item_not_found), Toast.LENGTH_SHORT).show();
		}
	}

	private void adminScannedNewItem(Barcode barcode)
	{
		Intent intent = new Intent(BarcodeScannerActivity.this, MainActivity.class);
		intent.putExtra("barcodeCreate", barcode.rawValue);
		this.startActivity(intent);
	}

	private void scannedExistingItem(Barcode barcode)
	{
		if (this.getAvailableAmount(barcode.rawValue) >= 1)
		{
			Intent intent = new Intent(BarcodeScannerActivity.this, MainActivity.class);
			intent.putExtra("barcode", barcode.rawValue);
			this.finish();
			this.startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(BarcodeScannerActivity.this, MainActivity.class);
			intent.putExtra("barcodeFailed", barcode.rawValue);
			this.finish();
			this.startActivity(intent);
		}
	}

	private int getAvailableAmount(String barcode)
	{
		Item item = MainActivity.kitchenManager.items().get(barcode);
		return item.getAmount() - MainActivity.kitchenManager.cart().getAmount(item);
	}

	@Override
	public void onScannedMultiple(List<Barcode> list)
	{
	}

	@Override
	public void onBitmapScanned(SparseArray<Barcode> sparseArray)
	{
	}

	@Override
	public void onScanError(String s)
	{
		Toast.makeText(this.getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCameraPermissionDenied()
	{
		this.finish();
		this.startActivity(new Intent(this, MainActivity.class));
	}

	@Override
	public void onBackPressed()
	{
		this.finish();
		this.startActivity(new Intent(this, MainActivity.class));
	}
}
