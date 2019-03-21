package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.barcode.Barcode;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Items;
import de.unikassel.chefcoders.codecampkitchen.logic.Session;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import info.androidhive.barcode.BarcodeReader;

import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener
{
	private TextView barcodeTextView;

	private String lastBarcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_barcode_scanner);

		this.barcodeTextView = this.findViewById(R.id.barcodeTextView);

		this.barcodeTextView.setOnClickListener(view -> {
			if (this.lastBarcode != null)
			{
				this.handleBarcode(this.lastBarcode);
			}
		});
	}

	@Override
	public void onScanned(Barcode barcode)
	{
		final String id = barcode.rawValue;
		this.lastBarcode = id;
		this.barcodeTextView.setText(id);
	}

	private void handleBarcode(String id)
	{
		final boolean isAdmin = Session.shared.isAdmin();
		final Item item = Items.shared.get(id);

		if (item != null)
		{
			// item exists

			final int amountAvailable = item.getAmount() - Cart.shared.getAmount(item);
			if (amountAvailable >= 1)
			{
				// still available, close scanner and display view that allows entering an amount

				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("barcode", id);
				this.finish();
				this.startActivity(intent);
			}
			else
			{
				// not available, inform user but don't close scanner
				this.runOnUiThread(() -> {
					Toast.makeText(this.getApplicationContext(), R.string.item_amount_not_available, Toast.LENGTH_SHORT)
					     .show();
				});
			}
		}
		else if (isAdmin)
		{
			// unknown barcode and user is admin, close scanner and show Create Item view

			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("barcodeCreate", id);
			this.startActivity(intent);
		}
		else
		{
			// unknown item and not admin, inform user but don't close scanner
			this.runOnUiThread(() -> {
				Toast.makeText(this.getApplicationContext(), R.string.item_not_found, Toast.LENGTH_SHORT).show();
			});
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
		this.barcodeTextView.setText(s);
	}

	@Override
	public void onCameraPermissionDenied()
	{
		this.onBackPressed();
	}

	@Override
	public void onBackPressed()
	{
		this.finish();
		this.startActivity(new Intent(this, MainActivity.class));
	}
}
