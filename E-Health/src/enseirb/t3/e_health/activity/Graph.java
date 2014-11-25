package enseirb.t3.e_health.activity;

import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.project.e_health.R;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.graph.GraphThread;
import enseirb.t3.e_health.graph.LineGraph;

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

	private void openChartHeartBeats(){
		List<Data> datas = dbHandler.retrieveDataList("B");
		
		line = new LineGraph(1, datas);
		view = line.getView(this);
		setContentView(view);
		
		thread = new GraphThread(datas, view, line);
		thread.start();
	}

	// Oxygen chart
	private void openChartOxygen(){
		List<Data> datas = dbHandler.retrieveDataList("O");
		
		line = new LineGraph(2, datas);
		view = line.getView(this);
		setContentView(view);
		
		thread = new GraphThread(datas, view, line);
		thread.start();
	}
}
