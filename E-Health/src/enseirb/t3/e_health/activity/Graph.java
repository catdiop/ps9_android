package enseirb.t3.e_health.activity;

import java.util.Date;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.project.e_health.R;

import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.bluetooth.BtThread;
import enseirb.t3.e_health.entity.ArduinoData;
import enseirb.t3.e_health.graph.LineGraph;
import enseirb.t3.e_health.graph.Point;

public class Graph extends Activity {

	private GraphicalView view;
	private LineGraph line;
	private Bluetooth bt;
	
	private static String TAG = "Graph";
	
	private int cmpt = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);
		
		bt = new Bluetooth(this);
		bt.enableBluetooth();
		if (!bt.queryingPairedDevices()) {
			// discover
			bt.discoverDevices();
		}
		
		openChart("O");
		
		// Getting references to buttons
		/*Button btnHeartBeats = (Button) findViewById(R.id.btn_heartBeats);
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

					break;
				}

			}
		};

		// Setting event click listener for the button btn_heartBeats of the measures activity layout
		btnHeartBeats.setOnClickListener(clickListener);
		btnOxygen.setOnClickListener(clickListener);*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.graph, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_bt:
				if (!bt.queryingPairedDevices()) {
					// discover
					bt.discoverDevices();
				} else {
					Thread ct = new BtThread(bt.device, handler);
					ct.start();
				}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String message = bundle.getString("msg");
			ArduinoData arduinoData = new ArduinoData(message);
			double value = arduinoData.getAndStoreData();

			Point p = new Point(new Date(), value);
			line.addNewPoint(p);
			cmpt++;
			//TODO : changer le 9 en variable globale
			if (cmpt > 9) {
				line.removePoint(cmpt - 10);
				cmpt--;
			}
			view.repaint();
			
			Log.d(TAG, Double.toString(value));
		}
	};

	private void openChart(String dataname){
		line = new LineGraph(dataname);
		view = line.getView(this);
		setContentView(view);
	}
}
