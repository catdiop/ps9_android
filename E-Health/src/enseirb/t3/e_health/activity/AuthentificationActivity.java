package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.entity.ArduinoData;
import enseirb.t3.e_health.entity.Patient;

/**
 * @author catdiop
 * 
 */
public class AuthentificationActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authentification);

		Button bluetooth = (Button) findViewById(R.id.bluetooth);
		bluetooth.setOnClickListener(this);

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);

		// TESTS Mesures
		ArduinoData aData = new ArduinoData("DATA200000;180000|B|85;181000|B|90;182000|B|105;183000|B|100;184000|B|95;185000|B|98;DATA220000;186000|B|95;187000|B|95;188000|B|95;189000|B|92;190000|B|85;191000|B|90;192000|B|105");

		// Get data and store it to the db
		
		aData.getAndStoreData();
//		ArduinoData aData = new ArduinoData("DATA200000;180000|B|85;181000|B|90;182000|B|105;183000|B|100;184000|B|95;185000|B|98;186000|B|95;187000|B|95;188000|B|95;189000|B|92;190000|B|85;191000|B|90;192000|B|105;193000|B|100;194000|B|95;195000|B|98;196000|B|95;197000|B|95;198000|B|95;199000|B|92");
		
//		String aDataStr = aData.getArduinoData();

		// Split data
//		String[] chunks = aData.getChunks(aDataStr);
//		aData.stockData(chunks, dbHandler);

		//tests r�cuperation
//		List<Data> DataList = dbHandler.retrieveDataList("B");
//		Log.d("data", DataList.get(1).getDataname());
//		Log.d("data", DataList.get(1).getValue());
//		Log.d("data", DataList.get(1).getDate());

		// Get data timestamp
		//		String date = aData.getDataTimestamp(chunks[0]);

		//EHealth.db.deleteAllUser();
		//Patient patient = new Patient("pikro", "1234");
		//EHealth.db.createUser(patient);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// Si l'utilisateur appuie sur connexion
		case R.id.connexion:
			TextView usernameView = (TextView) findViewById(R.id.username);
			TextView passwordView = (TextView) findViewById(R.id.password);
			//if (dbHandler.isUser(usernameView.getText().toString(), passwordView.getText()
				//	.toString())) {
				// on se connecte
				Intent intent = new Intent(AuthentificationActivity.this, Graph.class);
				startActivity(intent);
			//} else {
				//createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			//}
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
