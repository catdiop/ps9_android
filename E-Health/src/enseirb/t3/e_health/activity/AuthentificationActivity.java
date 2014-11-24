package enseirb.t3.e_health.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.entity.ArduinoData;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Patient;

/**
 * @author catdiop
 * 
 */
public class AuthentificationActivity extends Activity implements
		OnClickListener {

	private final static int REQUEST_ENABLE_BT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authentification);

		Button bluetooth = (Button) findViewById(R.id.bluetooth);
		bluetooth.setOnClickListener(this);

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);
		
		DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
		dbHandler.deleteAllData();
		dbHandler.deleteAllUser();
		
		Patient patient = new Patient("pikro", "1234");
		dbHandler.createUser(patient);
		
		// TESTS Mesures
		ArduinoData aData = new ArduinoData("DATA190;180|B|85;180|O|95;181|B|90;181|O|92;182|B|105;183|B|100;183|O|91;184|B|95;185|B|98;187|B|95;189|B|92;");
		
		String aDataStr = aData.getArduinoData();

		// Split data
		String[] chunks = aData.getChunks(aDataStr);
		aData.stockData(chunks, dbHandler);
		
		//tests récuperation
		List<Data> DataList = dbHandler.retrieveDataList("B");
		Log.d("data", DataList.get(1).getDataname());
		Log.d("data", DataList.get(1).getValue());
		Log.d("data", DataList.get(1).getDate());

		// Get data timestamp
//		String date = aData.getDataTimestamp(chunks[0]);
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// Si l'utilisateur appuie sur connexion
		case R.id.connexion:
			DatabaseHandler dbHandler = new DatabaseHandler(
					this.getApplicationContext());
			TextView view1 = (TextView) findViewById(R.id.username);
			TextView view2 = (TextView) findViewById(R.id.password);
			if (dbHandler.isUser(view1.getText().toString(), view2.getText()
					.toString())) {
				// on se connecte
				Intent intent = new Intent(AuthentificationActivity.this, Graph.class);
				startActivity(intent);
			} else {
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			}
			break;

		// Si l'utilisateur active le bluetooth
		case R.id.bluetooth:
			Bluetooth bluetooth = new Bluetooth(this);
			bluetooth.enableBluetooth();
			if (!bluetooth.queryingPairedDevices()) {
				// discover
				bluetooth.discoverDevices();
			}
			break;
//		case R.id.button1:
//			Bluetooth bluetooth = new Bluetooth(this);
//			bluetooth.enableBluetooth();
//			if (!bluetooth.queryingPairedDevices()) {
//				// discover
//				bluetooth.discoverDevices();
//			}
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
