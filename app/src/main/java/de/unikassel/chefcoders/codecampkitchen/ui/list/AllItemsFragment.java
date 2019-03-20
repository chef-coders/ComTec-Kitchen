package de.unikassel.chefcoders.codecampkitchen.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.logic.Session;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.ItemRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RecyclerEventHandler;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;

public class AllItemsFragment extends KitchenFragment implements RecyclerEventHandler
{
	private FloatingActionButton floatingActionButton;
	private ProgressBar          progressBar;

	public AllItemsFragment()
	{
		super(R.string.shop, false);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View allItemsView = inflater.inflate(R.layout.fragment_all_items, container, false);

		this.progressBar = allItemsView.findViewById(R.id.progressBar);
		this.initFloatingActionButton(allItemsView);
		this.initRecyclerView(allItemsView);

		return allItemsView;
	}

	private void initFloatingActionButton(View allItemsView)
	{
		this.floatingActionButton = allItemsView.findViewById(R.id.buyItemButton);
		this.floatingActionButton.setOnClickListener(v -> {
			MainActivity mainActivity = (MainActivity) this.getActivity();
			if (mainActivity != null)
			{
				mainActivity.changeFragmentForward(new ConfirmPurchasesFragment());
			}
		});
	}

	private void initRecyclerView(View allItemsView)
	{
		GeneralRecyclerView.install(allItemsView.findViewById(R.id.allItemsRecView),
		                            allItemsView.findViewById(R.id.allItemsSwipeRefreshLayout),
		                            new ItemRecyclerController(), this);
	}

	@Override
	public void updateToolbar(Toolbar toolbar)
	{
		final Menu menu = toolbar.getMenu();
		final boolean editMode = this.getMainActivity().isEditMode();
		final boolean isAdmin = Session.shared.isAdmin();

		toolbar.setTitle(editMode ? R.string.edit_items : R.string.shop);
		menu.findItem(R.id.action_scan_code).setVisible(!editMode);
		menu.findItem(R.id.action_create).setVisible(isAdmin && editMode);
		menu.findItem(R.id.action_edit).setVisible(isAdmin);
	}

	// --- --- --- Handle user interactions --- --- ---
	@Override
	public void handleLoadFinished()
	{
		this.progressBar.setVisibility(View.GONE);
	}

	@Override
	public void handleScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		this.floatingActionButton.hide();
	}

	@Override
	public void handleScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		if (!Cart.shared.getPurchases().isEmpty())
		{
			this.floatingActionButton.show();
		}
	}

	@Override
	public void handleClick(RowInfo row)
	{
		if (!Cart.shared.getPurchases().isEmpty() && !this.getMainActivity().isEditMode())
		{
			this.floatingActionButton.show();
		}
	}

	@Override
	public void handleSwipe(RowInfo row)
	{
		if (Cart.shared.getPurchases().isEmpty())
		{
			this.floatingActionButton.hide();
		}
	}
}
