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
import de.unikassel.chefcoders.codecampkitchen.ui.KitchenFragment;
import de.unikassel.chefcoders.codecampkitchen.ui.SimpleDialog;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;
import de.unikassel.chefcoders.codecampkitchen.ui.list.controller.ShoppingCartRecyclerController;
import de.unikassel.chefcoders.codecampkitchen.ui.list.recyclerview.GeneralRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPurchasesFragment extends KitchenFragment implements GeneralRecyclerView.RecViewEventHandler
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
		final double total = MainActivity.kitchenManager.cart().getTotal();
		SimpleDialog.builder().title(this.getActivity().getString(R.string.total_price, total))
		            .message(this.getActivity().getString(R.string.confirm_purchase))
		            .yesButton(this.getActivity().getString(R.string.purchase), (dialog, which) -> this.purchaseItems())
		            .noButton(this.getActivity().getString(R.string.cancel), (dialog, which) -> {})
		            .build(this.getActivity()).show(this.getFragmentManager(), "dialog");
	}

	private void purchaseItems()
	{
		this.progressBar.setVisibility(View.VISIBLE);
		SimpleAsyncTask.execute(this.getContext(), () -> MainActivity.kitchenManager.cart().submit(), () -> {
			MainActivity mainActivity = (MainActivity) this.getActivity();
			if (mainActivity != null)
			{
				mainActivity.changeFragmentBack(new AllItemsFragment());
				Toast.makeText(this.getActivity(), R.string.purchase_success, Toast.LENGTH_LONG).show();
			}
		}, () -> this.progressBar.setVisibility(View.GONE));
	}

	private void initRecyclerView(View view)
	{
		this.progressBar = view.findViewById(R.id.progressBar);
		new GeneralRecyclerView(view.findViewById(R.id.allItemsRecView), new ShoppingCartRecyclerController(),
		                        view.findViewById(R.id.allItemsSwipeRefreshLayout), this);
	}

	@Override
	public void updateToolbar(Toolbar toolbar)
	{
		Menu menu = toolbar.getMenu();
		menu.findItem(R.id.action_clear_all).setVisible(true);
	}

	@Override
	public void handleRecViewLoadFinished()
	{
		this.progressBar.setVisibility(View.GONE);
	}

	@Override
	public void handleRecViewScrolledDown(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		this.floatingActionButton.hide();
	}

	@Override
	public void handleRecViewScrolledUp(@NonNull RecyclerView recyclerView, int dx, int dy)
	{
		this.floatingActionButton.show();
	}

	@Override
	public void onClick(int section, int item)
	{

	}

	@Override
	public void onSwiped(int section, int item)
	{

	}
}
