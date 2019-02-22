package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Item;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

public class StatisticsFragment extends KitchenFragment
{
	private List<Purchase> purchases;

	private LinearLayout statisticsLayout;
	private TextView totalAmountView;
	private TextView moneySpentView;
	private TextView totalNumberView;
	private TextView purchasedItemsView;

	private HorizontalBarChart horizontalBarChart;

	public StatisticsFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View allItemsView = inflater.inflate(R.layout.fragment_statistics, container, false);

		statisticsLayout = allItemsView.findViewById(R.id.statisticsLayout);
		totalAmountView = allItemsView.findViewById(R.id.totalAmountView);
		moneySpentView = allItemsView.findViewById(R.id.moneySpentView);
		totalNumberView = allItemsView.findViewById(R.id.totalNumberView);
		purchasedItemsView = allItemsView.findViewById(R.id.purchasedItemsView);

		horizontalBarChart = allItemsView.findViewById(R.id.chart);

		if (MainActivity.kitchenManager.session().isAdmin()) {
			purchases = MainActivity.kitchenManager.purchases().getAll();
		} else {
			purchases = MainActivity.kitchenManager.purchases().getMine();
			statisticsLayout.removeView(totalAmountView);
			statisticsLayout.removeView(totalNumberView);
		}

		this.initValues();
		this.initAmountOfBoughtItemsChart();

		return allItemsView;
	}

	private void initValues() {
		double totalAmount = 0;
		double moneySpent = 0;
		int totalNumber = 0;
		int purchasedItems = 0;

		String userId = MainActivity.kitchenManager.session().getLoggedInUser().get_id();

		for (Purchase purchase : this.purchases) {
			totalAmount += purchase.getPrice();

			totalNumber += purchase.getAmount();

			if (purchase.getUser_id().equals(userId)) {
				moneySpent += purchase.getPrice();

				purchasedItems += purchase.getAmount();
			}
		}

		totalAmountView.setText(getString(R.string.totalAmountText, totalAmount));
		moneySpentView.setText(getString(R.string.moneySpentText, moneySpent));
		totalNumberView.setText(getString(R.string.totalNumberText, totalNumber));
		purchasedItemsView.setText(getString(R.string.purchasedItemsText, purchasedItems));
	}

	private void initAmountOfBoughtItemsChart() {
		LinkedHashMap<Item, Integer> boughtItems = new LinkedHashMap<>();
		for (Purchase purchase : this.purchases) {
			Item item = MainActivity.kitchenManager.items().get(purchase.getItem_id());
			if (item == null) { continue; }
			if (boughtItems.containsKey(item)) {
				Integer value = boughtItems.get(item);
				value += purchase.getAmount();
			} else {
				boughtItems.put(item, purchase.getAmount());
			}
		}

		int index = 0;
		ArrayList<IBarDataSet> records = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		for (Map.Entry<Item, Integer> entry : boughtItems.entrySet()) {
			String name = entry.getKey().getName();
			names.add(name);

			BarEntry barEntry = new BarEntry(index++, entry.getValue());

			ArrayList<BarEntry> barEntries = new ArrayList<>();
			barEntries.add(barEntry);

			BarDataSet record = new BarDataSet(barEntries, name);
			record.setValueTextSize(10f);
			records.add(record);
		}

		XAxis xAxis = horizontalBarChart.getXAxis();
		xAxis.setLabelCount(names.size());
		xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);
		xAxis.setEnabled(true);

		YAxis yAxis = horizontalBarChart.getAxisLeft();
		yAxis.setDrawAxisLine(true);
		yAxis.setDrawGridLines(true);
		yAxis.setAxisMinimum(0f);
		yAxis.setAxisMinimum(0f);

		YAxis yAxisRight = horizontalBarChart.getAxisRight();
		yAxisRight.setDrawAxisLine(true);
		yAxisRight.setDrawGridLines(false);

		horizontalBarChart.setFitBars(true);
		horizontalBarChart.animateXY(2500, 2500);

		BarData data = new BarData(records);

		horizontalBarChart.setData(data);
		horizontalBarChart.getDescription().setEnabled(false);
		horizontalBarChart.getLegend().setEnabled(false);
		horizontalBarChart.invalidate();
	}

	@Override
	protected void updateToolbar(Toolbar toolbar)
	{
		toolbar.setTitle(R.string.statistics);
	}
}
