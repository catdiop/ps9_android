package enseirb.t3.e_health.activity;

import java.util.LinkedList;
import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.project.e_health.R;

import enseirb.t3.e_health.bluetooth.BtThread;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.graph.LineGraph;
import enseirb.t3.e_health.graph.Point;
import enseirb.t3.e_health.session.SessionManager;

public class GraphAlertActivity extends Activity {

	SessionManager session;
	private GraphicalView view;
	private LineGraph line;
	private static String TAG = "Graph";
	//	private int cmpt = 0;
	private String dataname;
	private static int nbreMesuresPrint = 30;
	private int idPatient;
	Thread ct = null;
	private int alertId;
	List<String> menuPossible;
	//	private String dataName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);

		session = new SessionManager(getApplicationContext());

		idPatient = session.getUserDetails();
		alertId=this.getIntent().getIntExtra("alertId", 0);
		//		dataName=this.getIntent().getStringExtra("dataName");

		openChart();
		menuPossible=new LinkedList<String>();
		menuPossible.add("action_A");
		menuPossible.add("action_B");
		menuPossible.add("action_C");
		menuPossible.add("action_O");
		menuPossible.add("action_P");
		menuPossible.add("action_R");
		menuPossible.add("action_T");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.graph, menu);
		int index=0;

		List<String> datasTypes=getAlertDatasTypes();

		for(String str : menuPossible) {
			if(!datasTypes.contains(str)){
				switch (str) {
				case "action_A":
					index=R.id.action_A;
					break;
				case "action_B":
					index=R.id.action_B;
					break;
				case "action_C":
					index=R.id.action_C;
					break;
				case "action_O":
					index=R.id.action_O;
					break;
				case "action_P":
					index=R.id.action_P;
					break;
				case "action_R":
					index=R.id.action_R;
					break;
				case "action_T":
					index=R.id.action_T;
					break;
				default:
					index=0;
				}
				menu.removeItem(index);}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_A:
			dataname = "A";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_B:
			dataname = "B";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_C:
			dataname = "C";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_O:
			dataname = "O";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_P:
			dataname = "P";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_R:
			dataname = "R";
			//			cmpt = 0;
			openChart();
			return true;
		case R.id.action_T:
			dataname = "T";
			//			cmpt = 0;
			openChart();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void openChart() {
		List<Data> datas=EHealth.db.retrieveDatasForAlert(alertId);
		this.dataname = datas.get(0).getDataname();

		line = new LineGraph(dataname);
		line.setWindow(nbreMesuresPrint);
		view = line.getView(this);
		setContentView(view);
		for (Data dataTmp:datas) {
			if (dataTmp.getDataname().equals(dataname)) {
				Log.d(TAG, "date = " + dataTmp.getDate());
				Point p = new Point(dataTmp.getDate(), Double.parseDouble(dataTmp.getValue()));
				line.addNewPoint(p);
			}
			//				data = dataTmp;
			//			for (Data dataTmp : datas) {
			//				if (dataTmp.getDataname().equals(dataname))
			//					data = dataTmp;
			//			}

			//			Point p = new Point(data.getDate(), Double.parseDouble(data.getValue()));
			//			line.addNewPoint(p);
			//			cmpt++;
			//			if (cmpt > nbreMesuresPrint) {
			//				line.removePoint(cmpt - (nbreMesuresPrint + 1));
			//				cmpt--;
			//			}
			//			view.repaint();
		}
		view.repaint();
	}

	public void onBackPressed(Thread ct) {
		session.logoutUser();
		if (ct != null)
			((BtThread) ct).close();
		super.onBackPressed();
	}

	private List<String> getAlertDatasTypes(){
		List<Data> datas=EHealth.db.retrieveDatasForAlert(alertId);
		Data data = null;
		List<String> datasTypes=new LinkedList<String>();

		for (Data dataTmp:datas) {
			if (!datasTypes.contains("action_"+dataTmp.getDataname())) {
				datasTypes.add("action_"+dataTmp.getDataname());
			}
		}
		return datasTypes;
	}
}
