package de.unikassel.chefcoders.codecampkitchen.ui.barcodes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;
import de.unikassel.chefcoders.codecampkitchen.ui.AllItemsFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.DisableButtonTextWatcher;
import de.unikassel.chefcoders.codecampkitchen.ui.ItemDetailFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.multithreading.SimpleAsyncTask;

public class EditItemFragment extends ItemDetailFragment
{
	public static EditItemFragment newInstance(String itemId)
	{
		EditItemFragment fragment = new EditItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString("itemId", itemId);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		View editItemView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		this.initViews(editItemView);

		final String itemId = this.getArguments().getString("itemId");
		final Item item = MainActivity.kitchenManager.items().get(itemId);

		Button editButton = editItemView.findViewById(R.id.editButton);
		editButton.setOnClickListener(this::onEditClicked);

		DisableButtonTextWatcher.bind(editButton,
				this.nameText,
				this.priceText,
				this.amountText);

		this.barcodeTextView.setText(item.get_id());
		this.nameText.setText(item.getName());
		this.priceText.setText(String.valueOf(item.getPrice()));
		this.amountText.setText(String.valueOf(item.getAmount()));
		this.kindSpinner.setSelection(ItemKind.getIndex(item.getKind()));

		return editItemView;
	}

	public void onEditClicked(View view)
	{
		final Item item = this.getItem();
		if (item == null)
		{
			return;
		}

		SimpleAsyncTask.execute(this.getContext(), () -> MainActivity.kitchenManager.items().update(item), () -> {
			MainActivity.kitchenManager.cart().clear();
			Toast.makeText(this.getActivity(), R.string.edit_item_successful, Toast.LENGTH_SHORT).show();
			MainActivity mainActivity = (MainActivity) this.getActivity();
			mainActivity.changeFragment(new AllItemsFragment());
		});
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.edit_item_fragment_title);
	}
}
