package enseirb.t3.e_health.activity;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.project.e_health.R;

public class Measures extends Activity {

	private GraphicalView mChart;

	private String[] mMonth = new String[] {
			"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);

		// Getting references to buttons
		Button btnHeartBeats = (Button) findViewById(R.id.btn_heartBeats);
		Button btnOxygen = (Button) findViewById(R.id.btn_oxygen);

		// Defining click event listener for the button btn_chart
		OnClickListener clickListener = new OnClickListener() {

			@Override
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

	// Heart beats chart
	private void openChartHeartBeats(){
		int[] x = { 1,2,3,4,5,6,7,8 };
		int[] heartBeat = { 80, 81, 79, 80, 90, 87, 91, 92};

		// Creating an  XYSeries for heart beat
		XYSeries heartBeatSeries = new XYSeries("Heartbeats");
		// Adding data to heartBeatSeries
		for(int i=0;i<x.length;i++){
			heartBeatSeries.add(x[i], heartBeat[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Adding heartBeatSeries to the dataset
		dataset.addSeries(heartBeatSeries);

		// Creating XYSeriesRenderer to customize heartBeatSeries
		XYSeriesRenderer heartBeatRenderer = new XYSeriesRenderer();
		heartBeatRenderer.setColor(Color.RED);
		heartBeatRenderer.setPointStyle(PointStyle.CIRCLE);
		heartBeatRenderer.setFillPoints(true);
		heartBeatRenderer.setLineWidth(2);
		heartBeatRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Heart beats of client X");
		multiRenderer.setXTitle("Year 2012");
		multiRenderer.setYTitle("Heart beats");
		multiRenderer.setZoomButtonsVisible(true);
		for(int i=0;i<x.length;i++){
			multiRenderer.addXTextLabel(i+1, mMonth[i]);
		}

		// Adding heart beat renderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(heartBeatRenderer);

		// Creating an intent to plot line chart using dataset and multipleRenderer
		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);


		// Start Activity
		startActivity(intent);
	}

	// Oxygen chart
	private void openChartOxygen(){
		int[] x = { 1,2,3,4,5,6,7,8 };
		int[] oxygen = {50, 51, 51, 50, 40, 39, 40, 37};

		// Creating an  XYSeries for heart beat
		XYSeries oxygenSeries = new XYSeries("Oxygen");

		// Adding data to oxygenSeries
		for(int i=0;i<x.length;i++){
			oxygenSeries.add(x[i], oxygen[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Adding oxygenSeries to the dataset
		dataset.addSeries(oxygenSeries);

		// Creating XYSeriesRenderer to customize oxygenSeries
		XYSeriesRenderer oxygenRenderer = new XYSeriesRenderer();
		oxygenRenderer.setColor(Color.GREEN);
		oxygenRenderer.setPointStyle(PointStyle.CIRCLE);
		oxygenRenderer.setFillPoints(true);
		oxygenRenderer.setLineWidth(2);
		oxygenRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Oxygen rate of client X");
		multiRenderer.setXTitle("Year 2012");
		multiRenderer.setYTitle("Oxygen rate");
		multiRenderer.setZoomButtonsVisible(true);
		for(int i=0;i<x.length;i++){
			multiRenderer.addXTextLabel(i+1, mMonth[i]);
		}

		// Adding heart beat renderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(oxygenRenderer);

		// Creating an intent to plot line chart using dataset and multipleRenderer
		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);

		// Start Activity
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}



}
