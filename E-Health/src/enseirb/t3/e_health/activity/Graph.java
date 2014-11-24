package enseirb.t3.e_health.activity;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.project.e_health.R;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.graph.GraphThread;
import enseirb.t3.e_health.graph.LineGraph;
import enseirb.t3.e_health.graph.Point;

public class Graph extends Activity {

	private GraphicalView view;
	private LineGraph line;
	private static Thread thread;
	private DatabaseHandler dbHandler = new DatabaseHandler(this);

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);
		
		// Getting references to buttons
		Button btnHeartBeats = (Button) findViewById(R.id.btn_heartBeats);
		Button btnOxygen = (Button) findViewById(R.id.btn_oxygen);

		// Defining click event listener for the button btn_chart
		OnClickListener clickListener = new OnClickListener() {
//
//			@Override
			public void onClick(View v) {

				switch(v.getId())	{

				// HeatBeats chart
				case R.id.btn_heartBeats:
					openChartHeartBeats();

					break;
				// Oxygen chart
				case R.id.btn_oxygen:
					openChartOxygen();

					break;
				}

			}
		};

		// Setting event click listener for the button btn_heartBeats of the measures activity layout
		btnHeartBeats.setOnClickListener(clickListener);
		btnOxygen.setOnClickListener(clickListener);
	}
	
	
	// Charts' text size
	float textSize = 22;
	float titleTextSize = 24;
	// Heart beats chart
	private void openChartHeartBeats(){
		
		line = new LineGraph("Heart Beats");
		view = line.getView(this);
		setContentView(view);
		
		thread = new GraphThread(dbHandler, view, line, "B");
		thread.start();
		
//		List<Data> datas= dbHandler.retrieveDataList("B");
//		String[] dates = new String[datas.size()];
//		int[] values = new int[datas.size()];
//		int[] x = new int[datas.size()];
//		Data dataTmp = new Data();
		
//		for (int i = 0; i<datas.size() ; i++) {
//			dataTmp = datas.get(i);
//			dates[i] = dataTmp.getDate();
//			values[i] = Integer.parseInt(dataTmp.getValue());
//			x[i] = i + 1;
//		}

		// Creating an  XYSeries for heart beat
//		XYSeries heartBeatSeries = new XYSeries("Heartbeats");
		// Adding data to heartBeatSeries
//		for(int i=0;i<x.length;i++){
//			heartBeatSeries.add(x[i], values[i]);
//		}

		// Creating a dataset to hold each series
//		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Adding heartBeatSeries to the dataset
//		dataset.addSeries(heartBeatSeries);

		// Creating XYSeriesRenderer to customize heartBeatSeries
//		XYSeriesRenderer heartBeatRenderer = new XYSeriesRenderer();
//		heartBeatRenderer.setColor(Color.RED);
//		heartBeatRenderer.setPointStyle(PointStyle.CIRCLE);
//		heartBeatRenderer.setFillPoints(true);
//		heartBeatRenderer.setLineWidth(3);
//		heartBeatRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
//		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
//		multiRenderer.setXLabels(0);
//		multiRenderer.setChartTitle("Heart Beats");
//		multiRenderer.setXTitle("Arduino timestamps");
//		multiRenderer.setYTitle("Heart Beats");
//		multiRenderer.setZoomButtonsVisible(true);
//		multiRenderer.setAxisTitleTextSize(textSize);
//		multiRenderer.setLabelsTextSize(textSize);
//		multiRenderer.setChartTitleTextSize(titleTextSize);
//		for(int i=0;i<x.length;i++){
//			multiRenderer.addXTextLabel(x[i], dates[i]);
//		}

		// Adding heart beat renderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
//		multiRenderer.addSeriesRenderer(heartBeatRenderer);

		// Creating an intent to plot line chart using dataset and multipleRenderer
//		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);


		// Start Activity
//		startActivity(intent);
		
//		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), line.getDataset(), line.getmRenderer());
//		
//		startActivity(intent);
	}
	
	protected void onStart() {
		super.onStart();
//		view = line.getView(this);
//		setContentView(view);
	}

	// Oxygen chart
	private void openChartOxygen(){
		
		line = new LineGraph("Heart Beats");
		view = line.getView(this);
		setContentView(view);
		
		thread = new GraphThread(dbHandler, view, line, "O");
		thread.start();
//		
//		List<Data> datas= dbHandler.retrieveDataList("O");
//		String[] dates = new String[datas.size()];
//		int[] values = new int[datas.size()];
//		int[] x = new int[datas.size()];
//		Data dataTmp = new Data();
//		
//		for (int i = 0; i<datas.size() ; i++) {
//			dataTmp = datas.get(i);
//			dates[i] = dataTmp.getDate();
//			values[i] = Integer.parseInt(dataTmp.getValue());
//			x[i] = i;
//		}
//		
//		// Creating an  XYSeries for heart beat
//		XYSeries heartBeatSeries = new XYSeries("Oxygen");
//		// Adding data to heartBeatSeries
//		for(int i=0;i<x.length;i++){
//			heartBeatSeries.add(x[i], values[i]);
//		}
//
//		// Creating a dataset to hold each series
//		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
//
//		// Adding heartBeatSeries to the dataset
//		dataset.addSeries(heartBeatSeries);
//
//		// Creating XYSeriesRenderer to customize heartBeatSeries
//		XYSeriesRenderer heartBeatRenderer = new XYSeriesRenderer();
//		heartBeatRenderer.setColor(Color.GREEN);
//		heartBeatRenderer.setPointStyle(PointStyle.CIRCLE);
//		heartBeatRenderer.setFillPoints(true);
//		heartBeatRenderer.setLineWidth(2);
//		heartBeatRenderer.setDisplayChartValues(true);
//
//		// Creating a XYMultipleSeriesRenderer to customize the whole chart
//		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
//		multiRenderer.setXLabels(0);
//		multiRenderer.setChartTitle("Oxygen rate of client X");
//		multiRenderer.setXTitle("Arduino timestamps");
//		multiRenderer.setYTitle("Heart beats");
//		multiRenderer.setZoomButtonsVisible(true);
//		multiRenderer.setAxisTitleTextSize(textSize);
//		multiRenderer.setLabelsTextSize(textSize);
//		multiRenderer.setChartTitleTextSize(titleTextSize);
//		for(int i=0;i<x.length;i++){
//			multiRenderer.addXTextLabel(i+1, dates[i]);
//		}
//
//		// Adding heart beat renderer to multipleRenderer
//		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
//		// should be same
//		multiRenderer.addSeriesRenderer(heartBeatRenderer);
//
//		// Creating an intent to plot line chart using dataset and multipleRenderer
//		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);
//
//
//		// Start Activity
//		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}



}
