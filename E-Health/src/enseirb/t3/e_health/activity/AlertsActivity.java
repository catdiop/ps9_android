package enseirb.t3.e_health.activity;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.entity.User;

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
		case R.id.add_patient:
			add();
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
		p.setFirstname("titi");
		p.setLastname("toto");
		Alert a=new Alert(p, c.getTime(), "Tachycardie");
		a.setPatient(p);
		a.setDate(c.getTime());
		//		alerts.add(a);
		a.setPatient(p);
		alerts.add(a);

		adapter=new ListAlertAdapter(AlertsActivity.this, alerts);
		list.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alerts, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//ajout d'un patient
	//add new feed
	private void add() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View addPatient = inflater.inflate(R.layout.activity_patient_registration,
				null);

		final DialogWrapper wrapper = new DialogWrapper(addPatient);

		new AlertDialog.Builder(this)
		.setTitle(R.string.menu_add_patient)
		.setView(addPatient)
		.setPositiveButton(R.string.register,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				processAdd(wrapper);
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// ignore, just dismiss
			}
		}).show();
	}

	@SuppressWarnings("deprecation")
	private void processAdd(DialogWrapper wrapper) {
		addPatient(wrapper.getUsername(), wrapper.getPassword(), wrapper.getConfirmPwd());
	}
	class DialogWrapper {
		EditText username = null;
		EditText password = null;
		EditText confirmPwd = null;
		View base = null;

		DialogWrapper(View base) {
			this.base = base;
			this.username = (EditText) base.findViewById(R.id.username);
			this.password = (EditText) base.findViewById(R.id.password);
			this.confirmPwd = (EditText) base.findViewById(R.id.passwordCheck);
		}

		String getUsername() {
			return (getUsernameField().getText().toString());
		}

		String getPassword() {
			return (getPasswordField().getText().toString());
		}

		String getConfirmPwd() {
			return (getConfirmPwdField().getText().toString());
		}


		private EditText getUsernameField() {
			if (this.username == null) {
				this.username = (EditText) base.findViewById(R.id.username);
			}

			return (this.username);
		}

		private EditText getPasswordField() {
			if (this.password == null) {
				this.password = (EditText) base.findViewById(R.id.password);
			}

			return (this.password);
		}

		private EditText getConfirmPwdField() {
			if (this.confirmPwd == null) {
				this.confirmPwd = (EditText) base.findViewById(R.id.passwordCheck);
			}

			return (this.confirmPwd);
		}
	}

	private void addPatient(String username, String password, String confirmPwd){
		if (username.length() > 0 && password.length() > 0 && confirmPwd.length() >0) {

			// If user's name is already being used
			if (EHealth.db.doesUserExist(username)) {

				createDialog("Username already in use.");
				//				Intent intent = new Intent(AuthentificationActivity.this, Graph.class);
				//				startActivity(intent);
			} else {

				// Check password
				if (password.equals(confirmPwd.toString())) {

					Patient patient = new Patient(username, password);
					patient.setType("patient");
					EHealth.db.createUser(patient);

					createDialog("Patient account created !");
				}
				else {

					createDialog("Passwords don't match.");
				}
			}
		}
		else {

			createDialog("Empty field(s).");
		}
	}

	private void createDialog(String msg) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(msg);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}
}
