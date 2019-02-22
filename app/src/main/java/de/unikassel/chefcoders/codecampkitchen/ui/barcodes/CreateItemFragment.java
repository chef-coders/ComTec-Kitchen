package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.ItemDetailFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class CreateItemFragment extends ItemDetailFragment
{
	public static CreateItemFragment newInstance(String barcode)
	{
		CreateItemFragment fragment = new CreateItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString("barcode", barcode);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		View createItemView = inflater.inflate(R.layout.fragment_create_item, container, false);

		this.initViews(createItemView);

		Button createButton = createItemView.findViewById(R.id.createButton);
		createButton.setOnClickListener(this::onCreate);

		Button genIdButton = createItemView.findViewById(R.id.generateIdButton);
		genIdButton.setOnClickListener(this::onGeneratedId);

		Bundle args = this.getArguments();
		if (args != null)
		{
			this.barcodeValue.setEnabled(false);
			genIdButton.setEnabled(false);
			this.barcodeValue.setText(this.getArguments().getString("barcode"));
		}
		else
		{
			this.barcodeValue.setText("");
		}

		return createItemView;
	}

	public void onCreate(View view)
	{
		final Item item = this.getItem();
		if (item == null)
		{
			return;
		}

		SimpleAsyncTask.execute(this.getContext(), () -> MainActivity.kitchenManager.items().create(item), () -> {
			MainActivity mainActivity = (MainActivity) this.getActivity();
			mainActivity.changeFragment(new AllItemsFragment());
		});
	}

	public void onGeneratedId(View view)
	{
		final long barcode = (long) (Math.floor(Math.random() * (99999999999L - 10000000000L + 1L)) + 10000000000L);
		this.barcodeValue.setText("Generated:" + barcode);
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.create_item_fragment_title);
	}
}
