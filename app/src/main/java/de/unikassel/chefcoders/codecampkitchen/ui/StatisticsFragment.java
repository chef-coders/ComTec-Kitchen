package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;
import de.unikassel.chefcoders.codecampkitchen.ui.async.SimpleAsyncTask;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsFragment extends KitchenFragment
{
	private List<Purchase> purchases;

	private LinearLayout statisticsLayout;
	private TextView     totalAmountView;
	private TextView     moneySpentView;
	private TextView     totalNumberView;
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

		this.statisticsLayout = allItemsView.findViewById(R.id.statisticsLayout);
		this.totalAmountView = allItemsView.findViewById(R.id.totalAmountView);
		this.moneySpentView = allItemsView.findViewById(R.id.moneySpentView);
		this.totalNumberView = allItemsView.findViewById(R.id.totalNumberView);
		this.purchasedItemsView = allItemsView.findViewById(R.id.purchasedItemsView);

		this.horizontalBarChart = allItemsView.findViewById(R.id.chart);

		// display whatever is currently available, then refresh and update display
		this.reload();
		SimpleAsyncTask.execute(this.getActivity(), this::refresh, this::reload);

		return allItemsView;
	}

	private void refresh()
	{
		if (MainActivity.kitchenManager.session().isAdmin())
		{
			MainActivity.kitchenManager.purchases().refreshAll();
		}
		else
		{
			MainActivity.kitchenManager.purchases().refreshMine();
		}
	}

	private void reload()
	{
		if (MainActivity.kitchenManager.session().isAdmin())
		{
			this.purchases = MainActivity.kitchenManager.purchases().getAll();
		}
		else
		{
			this.purchases = MainActivity.kitchenManager.purchases().getMine();
			this.statisticsLayout.removeView(this.totalAmountView);
			this.statisticsLayout.removeView(this.totalNumberView);
		}

		this.initValues();
		this.initChart();
	}

	private void initValues()
	{
		double totalAmount = 0;
		double moneySpent = 0;
		int totalNumber = 0;
		int purchasedItems = 0;

		String userId = MainActivity.kitchenManager.session().getLoggedInUser().get_id();

		for (Purchase purchase : this.purchases)
		{
			totalAmount += purchase.getPrice();

			totalNumber += purchase.getAmount();

			if (purchase.getUser_id().equals(userId))
			{
				moneySpent += purchase.getPrice();

				purchasedItems += purchase.getAmount();
			}
		}

		this.totalAmountView.setText(getString(R.string.total_amount_text, totalAmount));
		this.moneySpentView.setText(getString(R.string.money_spent_text, moneySpent));
		this.totalNumberView.setText(getString(R.string.total_number_text, totalNumber));
		this.purchasedItemsView.setText(getString(R.string.purchased_items_text, purchasedItems));
	}

	private void initChart()
	{
		final Map<String, Integer> boughtItems = this.purchases.stream().collect(Collectors.groupingBy(
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
			record.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));
			records.add(record);
		}

		XAxis xAxis = this.horizontalBarChart.getXAxis();
		xAxis.setLabelCount(names.size());
		xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);
		xAxis.setEnabled(true);
		xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));

		YAxis yAxis = this.horizontalBarChart.getAxisLeft();
		yAxis.setDrawAxisLine(true);
		yAxis.setDrawGridLines(true);
		yAxis.setAxisMinimum(0f);
		yAxis.setAxisMinimum(0f);
		yAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryText));

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
		final Item item = MainActivity.kitchenManager.items().get(itemId);
		return item != null ? item.getName() : "...";
	}
}
