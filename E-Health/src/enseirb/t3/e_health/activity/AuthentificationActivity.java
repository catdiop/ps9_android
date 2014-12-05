package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.entity.ArduinoData;
import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;

/**
 * @author catdiop
 * 
 */
public class AuthentificationActivity extends Activity implements
		OnClickListener {

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

		case R.id.menu_bluetooth:
			Bluetooth bluetooth = new Bluetooth(this);
			bluetooth.enableBluetooth();
			if (!bluetooth.queryingPairedDevices()) {
				// discover
				bluetooth.discoverDevices();
			}
			break;

		case R.id.menu_alerts:
			Intent intent = new Intent(AuthentificationActivity.this,
					AlertsActivity.class);
			startActivity(intent);
			break;

		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authentification);

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);

		// TESTS patients / docteurs
		EHealth.db.deleteAllUser();

		Patient patient = new Patient("jo", "ab");
		patient.setType("patient");

		Doctor doctor = new Doctor("bob", "ba");
		doctor.setType("doctor");

		EHealth.db.createUser(patient);
		EHealth.db.createUser(doctor);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// Si l'utilisateur appuie sur connexion
		case R.id.connexion:
			TextView usernameView = (TextView) findViewById(R.id.username);
			TextView passwordView = (TextView) findViewById(R.id.password);

			if (EHealth.db.isUser(usernameView.getText().toString(),
					passwordView.getText().toString())) {
				// on se connecte
				// Intent intent = new Intent(AuthentificationActivity.this,
				// Graph.class);
				// startActivity(intent);
				// else {
				// createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
				// }

				if (EHealth.db.isUser(usernameView.getText().toString(),
						passwordView.getText().toString())) {
					// on se connecte
					if (EHealth.db.isUserADoctor(usernameView.getText()
							.toString())) {

						Intent intent = new Intent(
								AuthentificationActivity.this,
								AlertsActivity.class);
						startActivity(intent);
					} else {

						Intent intent = new Intent(
								AuthentificationActivity.this, Graph.class);
						startActivity(intent);
					}
				}

				else {

					createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
				}
				break;
			}
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
