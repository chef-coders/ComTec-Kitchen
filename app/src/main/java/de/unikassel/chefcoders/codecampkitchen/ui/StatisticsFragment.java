package de.unikassel.chefcoders.codecampkitchen.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

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
		purchases = MainActivity.kitchenManager.getAllPurchases();
		View allItemsView = inflater.inflate(R.layout.fragment_statistics, container, false);
		this.initAmountOfBoughtItemsChart(allItemsView);
		return allItemsView;
	}

	private void initAmountOfBoughtItemsChart(View allItemsView) {
		GraphView graph = (GraphView) allItemsView.findViewById(R.id.graph);

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
		List<DataPoint> dataPoints = new ArrayList<>();
		List<String> names = new ArrayList<>();
		for (Map.Entry<Item, Integer> entry : boughtItems.entrySet()) {
			dataPoints.add(new DataPoint(index++, entry.getValue()));
			String name = entry.getKey().getName();
			int lastIndex = name.length() >= 3 ? 3 : name.length();
			names.add(entry.getKey().getName().substring(0, lastIndex));
		}

		StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
		staticLabelsFormatter.setHorizontalLabels(names.toArray(new String[0]));
		graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

		BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints.toArray(new DataPoint[0]));
		graph.addSeries(series);

		series.setValueDependentColor(data -> Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100));
		series.setSpacing(50);
		series.setDataWidth(1.5);
		series.setDrawValuesOnTop(true);
		series.setValuesOnTopColor(Color.RED);
	}
}
