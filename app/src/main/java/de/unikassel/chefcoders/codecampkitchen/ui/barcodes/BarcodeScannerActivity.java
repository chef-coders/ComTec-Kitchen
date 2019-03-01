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
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_scanner);
	}

	@Override
	public void onScanned(Barcode barcode)
	{
		boolean isAdmin = MainActivity.kitchenManager.session().isAdmin();
		final Item item = MainActivity.kitchenManager.items().get(barcode.rawValue);

		if (item != null)
		{
			// item exists

			final int amountAvailable = item.getAmount() - MainActivity.kitchenManager.cart().getAmount(item);
			if (amountAvailable >= 1)
			{
				// still available, close scanner and display view that allows entering an amount

				Intent intent = new Intent(BarcodeScannerActivity.this, MainActivity.class);
				intent.putExtra("barcode", barcode.rawValue);
				this.finish();
				this.startActivity(intent);
			}
			else
			{
				// not available, close scanner and show Shop

				Intent intent = new Intent(BarcodeScannerActivity.this, MainActivity.class);
				intent.putExtra("barcodeFailed", barcode.rawValue);
				this.finish();
				this.startActivity(intent);
			}
		}
		else if (isAdmin)
		{
			// unknown barcode and user is admin, close scanner and show Create Item view

			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("barcodeCreate", barcode.rawValue);
			this.startActivity(intent);
		}
		else
		{
			// unknown item and not admin, inform user but don't close scanner

			Toast.makeText(this.getApplicationContext(), this.getString(R.string.item_not_found), Toast.LENGTH_SHORT).show();
		}
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
