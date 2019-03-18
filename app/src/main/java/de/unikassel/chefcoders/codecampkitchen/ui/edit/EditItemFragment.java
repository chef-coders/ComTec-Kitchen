package de.unikassel.chefcoders.codecampkitchen.ui.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Items;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.ItemKind;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

public class EditItemFragment extends ItemDetailFragment
{
	public EditItemFragment()
	{
		super(R.string.edit_item_fragment_title);
	}

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
		super.onCreateView(inflater, container, savedInstanceState);
		View editItemView = inflater.inflate(R.layout.fragment_edit_item, container, false);

		this.initViews(editItemView);

		final String itemId = this.getArguments().getString("itemId");
		final Item item = Items.shared.get(itemId);

		this.barcodeTextView.setText(item.get_id());
		this.nameText.setText(item.getName());
		this.priceText.setText(String.valueOf(item.getPrice()));
		this.amountText.setText(String.valueOf(item.getAmount()));
		this.kindSpinner.setSelection(ItemKind.getIndex(item.getKind()));

		return editItemView;
	}

	@Override
	protected void handleClickedOnSave(View view)
	{
		final Item item = this.getItem();
		if (item == null)
		{
			return;
		}

		SimpleAsyncTask.execute(this.getContext(), () -> Items.shared.update(item), () -> {
			Cart.shared.clear();
			Toast.makeText(this.getActivity(), R.string.edit_item_successful, Toast.LENGTH_SHORT).show();
			MainActivity mainActivity = (MainActivity) this.getActivity();
			mainActivity.changeFragmentBack();
		});
	}
}
