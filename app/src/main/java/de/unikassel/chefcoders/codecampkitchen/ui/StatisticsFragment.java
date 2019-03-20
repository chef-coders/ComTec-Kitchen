package de.unikassel.chefcoders.codecampkitchen.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.logic.Items;
import de.unikassel.chefcoders.codecampkitchen.logic.Purchases;
import de.unikassel.chefcoders.codecampkitchen.logic.Session;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsFragment extends KitchenFragment
{
	private Button       myPurchasesButton;
	private Button       allPurchasesButton;
	private TextView     moneySpentView;
	private TextView     purchasedItemsView;

	private HorizontalBarChart horizontalBarChart;

	public StatisticsFragment()
	{
		super(R.string.statistics, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View allItemsView = inflater.inflate(R.layout.fragment_statistics, container, false);

		this.myPurchasesButton = allItemsView.findViewById(R.id.myPurchasesButton);
		this.allPurchasesButton = allItemsView.findViewById(R.id.allPurchasesButton);
		this.moneySpentView = allItemsView.findViewById(R.id.moneySpentView);
		this.purchasedItemsView = allItemsView.findViewById(R.id.purchasedItemsView);

		this.horizontalBarChart = allItemsView.findViewById(R.id.chart);

		if (Session.shared.isAdmin())
		{
			this.myPurchasesButton.setOnClickListener(v -> this.refreshFragment(false));
			this.allPurchasesButton.setOnClickListener(v -> this.refreshFragment(true));
		}
		else
		{
			this.allPurchasesButton.setVisibility(View.GONE);
		}

		this.refreshFragment(false);

		return allItemsView;
	}

	private void refreshFragment(boolean getAll)
	{
		this.myPurchasesButton.setEnabled(false);
		this.allPurchasesButton.setEnabled(false);

		final int accentColor = this.getResources().getColor(R.color.colorAccent);
		this.myPurchasesButton.setBackgroundColor(getAll ? Color.TRANSPARENT : accentColor);
		this.allPurchasesButton.setBackgroundColor(!getAll ? Color.TRANSPARENT : accentColor);

		SimpleAsyncTask
			.execute(this.getActivity(), getAll ? Purchases.shared::refreshAll : Purchases.shared::refreshMine, () -> {
				this.reload(getAll ? Purchases.shared.getAll() : Purchases.shared.getMine());
			}, () -> {
				(getAll ? this.myPurchasesButton : this.allPurchasesButton).setEnabled(true);
			});
	}

	private void reload(List<Purchase> purchases)
	{
		this.initValues(purchases);
		this.initChart(purchases);
	}

	private void initValues(List<Purchase> purchases)
	{
		double moneySpent = 0;
		int purchasedItems = 0;

		for (Purchase purchase : purchases)
		{
			moneySpent += purchase.getPrice();
			purchasedItems += purchase.getAmount();
		}

		this.moneySpentView.setText(getString(R.string.money_spent_text, moneySpent));
		this.purchasedItemsView.setText(getString(R.string.purchased_items_text, purchasedItems));
	}

	private void initChart(List<Purchase> purchases)
	{
		final Map<String, Integer> boughtItems = purchases.stream().collect(Collectors.groupingBy(
			StatisticsFragment::getItemName, TreeMap::new, Collectors.summingInt(Purchase::getAmount)));

		int index = 0;
		final List<String> names = new ArrayList<>();
		final List<IBarDataSet> records = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : boughtItems.entrySet())
		{
			final String name = entry.getKey();

			names.add(name);

			final BarEntry barEntry = new BarEntry(index++, entry.getValue());
			final BarDataSet record = new BarDataSet(Collections.singletonList(barEntry), name);

			record.setValueTextSize(10F);
			record.setValueTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorPrimaryText));
			records.add(record);
		}

		XAxis xAxis = this.horizontalBarChart.getXAxis();
		xAxis.setLabelCount(names.size());
		xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);
		xAxis.setEnabled(true);
		xAxis.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorPrimaryText));

		YAxis yAxis = this.horizontalBarChart.getAxisLeft();
		yAxis.setDrawAxisLine(true);
		yAxis.setDrawGridLines(true);
		yAxis.setAxisMinimum(0f);
		yAxis.setAxisMinimum(0f);
		yAxis.setTextColor(ContextCompat.getColor(this.getActivity(), R.color.colorPrimaryText));

		YAxis yAxisRight = this.horizontalBarChart.getAxisRight();
		yAxisRight.setDrawAxisLine(true);
		yAxisRight.setDrawGridLines(false);

		this.horizontalBarChart.setFitBars(true);
		this.horizontalBarChart.animateXY(2500, 2500);

		BarData data = new BarData(records);

		this.horizontalBarChart.setData(data);
		this.horizontalBarChart.getDescription().setEnabled(false);
		this.horizontalBarChart.getLegend().setEnabled(false);
		this.horizontalBarChart.invalidate();
	}

	private static String getItemName(Purchase purchase)
	{
		final String itemId = purchase.getItem_id();
		final Item item = Items.shared.get(itemId);
		return item != null ? item.getName() : "...";
	}
}
