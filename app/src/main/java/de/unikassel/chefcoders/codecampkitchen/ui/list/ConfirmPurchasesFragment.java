package de.unikassel.chefcoders.codecampkitchen.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Cart;
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.SimpleDialog;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.ShoppingCartRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RecyclerEventHandler;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.RowInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPurchasesFragment extends KitchenFragment implements RecyclerEventHandler
{

	private FloatingActionButton floatingActionButton;
	private ProgressBar          progressBar;

	public ConfirmPurchasesFragment()
	{
		super(R.string.purchase, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_confirm_purchases, container, false);
		this.initFloatingActionButton(view);
		this.initRecyclerView(view);
		return view;
	}

	private void initFloatingActionButton(View view)
	{
		this.floatingActionButton = view.findViewById(R.id.buyItemButton);
		this.floatingActionButton.setOnClickListener(v -> this.confirmPurchaseDialog());
	}

	private void confirmPurchaseDialog()
	{
		final double total = Cart.shared.getTotal();
		SimpleDialog.builder().title(this.getActivity().getString(R.string.total_price, total))
		            .message(this.getActivity().getString(R.string.confirm_purchase))
		            .yesButton(this.getActivity().getString(R.string.purchase), (dialog, which) -> this.purchaseItems())
		            .noButton(this.getActivity().getString(R.string.cancel), (dialog, which) -> {})
		            .build(this.getActivity()).show(this.getFragmentManager(), "dialog");
	}

	private void purchaseItems()
	{
		this.progressBar.setVisibility(View.VISIBLE);
		SimpleAsyncTask.execute(this.getContext(), () -> Cart.shared.submit(), () -> {
			MainActivity mainActivity = (MainActivity) this.getActivity();
			if (mainActivity != null)
			{
				mainActivity.changeFragmentBack();
				Toast.makeText(this.getActivity(), R.string.purchase_success, Toast.LENGTH_LONG).show();
			}
		}, () -> this.progressBar.setVisibility(View.GONE));
	}

	private void initRecyclerView(View view)
	{
		this.progressBar = view.findViewById(R.id.progressBar);
		GeneralRecyclerView
			.install(view.findViewById(R.id.allItemsRecView), view.findViewById(R.id.allItemsSwipeRefreshLayout),
			         new ShoppingCartRecyclerController(), this);
	}

	@Override
	public void updateToolbar(Toolbar toolbar)
	{
		Menu menu = toolbar.getMenu();
		menu.findItem(R.id.action_clear_all).setVisible(true);
	}

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
		this.floatingActionButton.show();
	}

	@Override
	public void handleClick(RowInfo row)
	{

	}

	@Override
	public void handleSwipe(RowInfo row)
	{

	}
}
