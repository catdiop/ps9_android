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
	private DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);

	
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
					openChart("B");

					break;
				// Oxygen chart
				case R.id.btn_oxygen:
					openChart("O");

					break;
				}

			}
		};

		// Setting event click listener for the button btn_heartBeats of the measures activity layout
		btnHeartBeats.setOnClickListener(clickListener);
		btnOxygen.setOnClickListener(clickListener);
	}

	private void openChart(String dataname){
		List<Data> datas = dbHandler.retrieveDataList(dataname);
		
		line = new LineGraph(dataname, datas);
		view = line.getView(this);
		setContentView(view);
		
		thread = new GraphThread(datas, view, line);
		thread.start();
	}
}
