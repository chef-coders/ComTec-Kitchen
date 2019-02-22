package de.unikassel.chefcoders.codecampkitchen.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import de.unikassel.chefcoders.codecampkitchen.MainActivity;
import de.unikassel.chefcoders.codecampkitchen.R;
import de.unikassel.chefcoders.codecampkitchen.model.Purchase;

public class StatisticsFragment extends KitchenFragment
{
	private List<Purchase> purchases;

	public StatisticsFragment()
	{
		purchases = MainActivity.kitchenManager.getAllPurchases();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View allItemsView = inflater.inflate(R.layout.fragment_statistics, container, false);
		this.initCharts(allItemsView);
		return allItemsView;
	}

	private void initCharts(View allItemsView) {
		GraphView graph = (GraphView) allItemsView.findViewById(R.id.graph);
		LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
				new DataPoint(0, 1),
				new DataPoint(1, 5),
				new DataPoint(2, 3),
				new DataPoint(3, 2),
				new DataPoint(4, 6)
		});
		graph.addSeries(series);
	}
}
