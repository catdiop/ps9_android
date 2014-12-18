package enseirb.t3.e_health.activity;

import java.util.ArrayList;

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
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.graph.LineGraph;
import enseirb.t3.e_health.graph.Point;
import enseirb.t3.e_health.process.DataProcess;
import enseirb.t3.e_health.session.SessionManager;

public class Graph extends Activity {

	SessionManager session;
	private GraphicalView view;
	private LineGraph line;
	private Bluetooth bt;
	private String dataname = "O";
	private static String TAG = "Graph";
	private int cmpt = 0;
	private static final int nbreMesuresPrint = 9;
	private int idPatient;
	public static DataProcess dataProcess = new DataProcess();
	public static ArduinoData arduinoData = new ArduinoData(dataProcess);
	Thread ct = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);

		session = new SessionManager(getApplicationContext());

		bt = new Bluetooth(this);
		bt.enableBluetooth();

		idPatient = session.getUserDetails();

		openChart();
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
			if (!bt.queryingPairedDevices())
				bt.discoverDevices();
			else {
				ct = new BtThread(bt.device, handler);
				ct.start();
			}
			return true;
		case R.id.action_A:
			dataname = "A";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_B:
			dataname = "B";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_C:
			dataname = "C";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_O:
			dataname = "O";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_P:
			dataname = "P";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_R:
			dataname = "R";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_T:
			dataname = "T";
			cmpt = 0;
			openChart();
			return true;
		case R.id.deconnexion:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public Handler handler = new Handler() {
		// private DataProcess dataProcess;

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			Data data = null;
			ArrayList<Data> arrayData = null;
			String message = bundle.getString("msg");
			String[] aDataArrayStr = message.split("(?=DATA)");
			// dataProcess = new DataProcess();
			// ArduinoData arduinoData = new ArduinoData(Graph.dataProcess);

			for (int i = 1; i < aDataArrayStr.length; i++) {
				if (aDataArrayStr[i].length() > 35) {

					arrayData = arduinoData.stockData(
							arduinoData.getChunks(aDataArrayStr[i]), idPatient);

					for (Data dataTmp : arrayData) {
						if (dataTmp.getDataname().equals(dataname))
							data = dataTmp;
					}

					Point p = new Point(data.getDate(), Double.parseDouble(data
							.getValue()));
					line.addNewPoint(p);
					cmpt++;
					if (cmpt > nbreMesuresPrint) {
						line.removePoint(cmpt - (nbreMesuresPrint + 1));
						cmpt--;
					}
					view.repaint();

					Log.d(TAG, Double.toString(Double.parseDouble(data
							.getValue())));
				}
			}
		}
	};

	private void openChart() {
		line = new LineGraph(dataname);
		view = line.getView(this);
		setContentView(view);
	}
	
	@Override
	public void onBackPressed() {
		session.logoutUser();
		if (ct != null)
			((BtThread) ct).close();
		super.onBackPressed();
	}
}
