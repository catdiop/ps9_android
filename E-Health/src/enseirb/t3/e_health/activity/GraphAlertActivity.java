package enseirb.t3.e_health.activity;

import java.util.ArrayList;
import java.util.List;

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
	private int cmpt = 0;
	private static int nbreMesuresPrint = 9;
	private int idPatient;
	Thread ct = null;
	private int alertId;
	private String dataName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);

		session = new SessionManager(getApplicationContext());

		idPatient = session.getUserDetails();
		alertId=this.getIntent().getIntExtra("alertId", 0);
		dataName=this.getIntent().getStringExtra("dataName");
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void openChart() {
		List<Data> datas=EHealth.db.retrieveDatasForAlert(alertId);

		for (Data d:datas) {
			if(d.getDataname()!=this.dataName)
				continue;

			Point p = new Point(d.getDate(), Double.parseDouble(d.getValue()));
			line.addNewPoint(p);
			cmpt++;
			if (cmpt > nbreMesuresPrint) {
				line.removePoint(cmpt - (nbreMesuresPrint + 1));
				cmpt--;
			}
			view.repaint();
		}
		line = new LineGraph(dataName);
		view = line.getView(this);
		setContentView(view);
	}

	public void onBackPressed(Thread ct) {
		session.logoutUser();
		if (ct != null)
			((BtThread) ct).close();
		super.onBackPressed();
	}
}
