package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.entity.User;
import enseirb.t3.e_health.session.SessionManager;

/**
 * @author catdiop
 * 
 */
public class AuthentificationActivity extends Activity implements
		OnClickListener {
	SessionManager session;
	private Intent intent;
	private TextView usernameView;
	private TextView passwordView;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.authentificate, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		case R.id.add_doctor:
			// Add Doctor
			intent = new Intent(AuthentificationActivity.this,
					DoctorRegistrationActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentification);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		session = new SessionManager(getApplicationContext());

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);
		
		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(Html.fromHtml("ME <b>CONNECTER<\b>"));

		//Conseil : Purger la BDD en décommentant les 4 lignes suivantes, puis relancer l'appli en les re-commentant et en décommentant les lignes de "doctor doctor.." à 
		//EHealth.db.createPatient. Enfin, relancer une dernière fois l'appli en re-commentant tout 
		
//		EHealth.db.deleteAllPatient();
//		EHealth.db.deleteAllDoctor();
		EHealth.db.deleteAllData();
		EHealth.db.deleteAllAlert();
		EHealth.db.deleteAllASavedData();

//		Doctor doctor = new Doctor("doc", "doc");
//		EHealth.db.createDoctor(doctor);
//
		//firstname = bla1, lastname = bla2, username = ja, password = ab
//		Patient patient = new Patient("bla1", "bla2", EHealth.db.retrieveDoctor(EHealth.db.retrieveUser("doc", "doc").getIDUser()).getIDDoctor(), "ja", "ab");
//		EHealth.db.createPatient(patient);
	}

	// @Override
	protected void onResume() {
		if (session.isContains(SessionManager.KEY_ID_USER)) {
			switch (EHealth.db.retrieveUser(session.getUserDetails()).getType()) {
			case "Doctor":
				intent = new Intent(AuthentificationActivity.this,
						AlertsActivity.class);
				break;
			case "Patient":
				intent = new Intent(AuthentificationActivity.this, Graph.class);
				startActivity(intent);
			default:
				break;
			}
			startActivity(intent);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		User user;
		String username;
		String password;

		switch (v.getId()) {

		// Si l'utilisateur appuie sur connexion
		case R.id.connexion:
			usernameView = (TextView) findViewById(R.id.username);
			passwordView = (TextView) findViewById(R.id.password);

			username = usernameView.getText().toString();
			password = passwordView.getText().toString();

			if ((user = EHealth.db.retrieveUser(username, password)) != null) {
				session.createLoginSession(user.getIDUser());
				if (user.getType().equals("Doctor"))
					intent = new Intent(AuthentificationActivity.this,
							AlertsActivity.class);
				else if (user.getType().equals("Patient"))
					intent = new Intent(AuthentificationActivity.this,
							Graph.class);
				startActivity(intent);
			} else
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			break;
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
