package enseirb.t3.e_health.activity;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Patient;

public class AlertsActivity extends Activity {

	private ListView list; 
	private Context context;
	private ListAlertAdapter adapter;
	static View view;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		default:;	
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		list = (ListView)findViewById(R.id.list_alerts);
		list.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			
				
				return false;
			}

		});
		List<Alert> alerts=new LinkedList<Alert>();
		Calendar c=Calendar.getInstance();
		Patient p=new Patient();
		p.setFirstname("lili");
		p.setLastname("lolo");
		Alert a=new Alert(p, c.getTime(), "Tachycardie");
		a.setPatient(p);
		a.setDate(c.getTime());
//		alerts.add(a);
		p.setFirstname("titi");
		p.setLastname("toto");
		a.setPatient(p);
		alerts.add(a);
		
		adapter=new ListAlertAdapter(AlertsActivity.this, alerts);
		list.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.authentificate, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
