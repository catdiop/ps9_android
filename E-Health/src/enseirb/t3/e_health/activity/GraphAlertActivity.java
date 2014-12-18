package enseirb.t3.e_health.activity;

import java.util.List;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
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
	private String dataname = "A";
	private static int nbreMesuresPrint = 9;
	private int idPatient;
	Thread ct = null;
	private int alertId;
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
		Data data = null;

		line = new LineGraph(dataname);
		view = line.getView(this);
		setContentView(view);
		
		for (Data dataTmp:datas) {
//			if(d.getDataname()!=this.dataName)
//				continue;
			if (dataTmp.getDataname().equals(dataname))
				data = dataTmp;
//			for (Data dataTmp : datas) {
//				if (dataTmp.getDataname().equals(dataname))
//					data = dataTmp;
//			}

			Point p = new Point(data.getDate(), Double.parseDouble(data.getValue()));
			line.addNewPoint(p);
//			cmpt++;
//			if (cmpt > nbreMesuresPrint) {
//				line.removePoint(cmpt - (nbreMesuresPrint + 1));
//				cmpt--;
//			}
			view.repaint();
		}
	}

	public void onBackPressed(Thread ct) {
		session.logoutUser();
		if (ct != null)
			((BtThread) ct).close();
		super.onBackPressed();
	}
}
