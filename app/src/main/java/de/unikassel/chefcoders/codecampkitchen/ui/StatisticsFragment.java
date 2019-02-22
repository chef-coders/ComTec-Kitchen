package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

	public StatisticsFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		purchases = MainActivity.kitchenManager.purchases().getAll();
		View allItemsView = inflater.inflate(R.layout.fragment_statistics, container, false);
		this.initAmountOfBoughtItemsChart(allItemsView);
		return allItemsView;
	}

	private void initAmountOfBoughtItemsChart(View allItemsView) {
		HorizontalBarChart horizontalBarChart = (HorizontalBarChart) allItemsView.findViewById(R.id.chart);

		LinkedHashMap<Item, Integer> boughtItems = new LinkedHashMap<>();
		for (Purchase purchase : this.purchases) {
			Item item = MainActivity.kitchenManager.getItemById(purchase.getItem_id());
			if (item == null) { continue; }
			if (boughtItems.containsKey(item)) {
				Integer value = boughtItems.get(item);
				value += purchase.getAmount();
			} else {
				boughtItems.put(item, purchase.getAmount());
			}
		}

		int index = 0;
		ArrayList<IBarDataSet> dataSets = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		for (Map.Entry<Item, Integer> entry : boughtItems.entrySet()) {
			String name = entry.getKey().getName();
			names.add(name);

			BarEntry value = new BarEntry(index++, entry.getValue());

			ArrayList<BarEntry> record = new ArrayList<>();
			record.add(value);

			BarDataSet x = new BarDataSet(record, name);
			dataSets.add(x);
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

		BarData data = new BarData(dataSets);

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
