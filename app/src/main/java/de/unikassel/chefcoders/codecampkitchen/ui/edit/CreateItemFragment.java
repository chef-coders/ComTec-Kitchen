package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

import java.util.UUID;

public class CreateItemFragment extends ItemDetailFragment
{
	public CreateItemFragment()
	{
		super(R.string.create_item_fragment_title);
	}

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
		super.onCreateView(inflater, container, savedInstanceState);

		View createItemView = inflater.inflate(R.layout.fragment_create_item, container, false);

		this.initViews(createItemView);

		Button genIdButton = createItemView.findViewById(R.id.generateIdButton);
		genIdButton.setOnClickListener(this::onGeneratedId);

		Bundle args = this.getArguments();
		if (args != null)
		{
			this.barcodeTextView.setEnabled(false);
			genIdButton.setEnabled(false);
			this.barcodeTextView.setText(this.getArguments().getString("barcode"));
		}
		else
		{
			this.barcodeTextView.setText("");
		}

		return createItemView;
	}

	public void onGeneratedId(View view)
	{
		this.barcodeTextView.setText(UUID.randomUUID().toString());
	}

	@Override
	protected boolean isInputValid()
	{
		if (!super.isInputValid())
		{
			return false;
		}

		final String barcode = this.barcodeTextView.getText().toString();
		if (MainActivity.kitchenManager.items().get(barcode) != null)
		{
			final Toast toast = Toast.makeText(this.getContext(), R.string.id_exists, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return false;
		}

		return true;
	}

	@Override
	protected void handleClickedOnSave(View view)
	{
		final Item item = this.getItem();
		if (item == null)
		{
			return;
		}

		SimpleAsyncTask.execute(this.getContext(), () -> MainActivity.kitchenManager.items().create(item), () -> {
			Toast.makeText(this.getActivity(), R.string.create_item_successful, Toast.LENGTH_SHORT).show();
			MainActivity mainActivity = (MainActivity) this.getActivity();
			mainActivity.changeFragmentBack();
		});
	}
}
