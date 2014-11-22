package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

//import enseirb.t3.e_health.DAO.UserDatabaseHandler;
import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.entity.ArduinoData;
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
		dbHandler.deleteAllUser();
		
		Patient patient = new Patient("pikro", "1234");
		dbHandler.create(patient);
		
		// TESTS
		ArduinoData aData = new ArduinoData("DATA190;180|B|78;180|O|78;183|B|82;184|B|85;184|O|85;186|B|77;188|B|80;188|O|80;");
		
		String aDataStr = aData.getArduinoData();

		// Split data
		String[] chunks = aData.getChunks(aDataStr);
		aData.stockData(chunks, dbHandler);

		// Get data timestamp
		String date = aData.getDataTimestamp(chunks[0]);
		
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
				Intent intent = new Intent(AuthentificationActivity.this, Measures.class);
				startActivity(intent);
			} else {
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			}
			break;

		// Si l'utilisateur active le bluetooth
		case R.id.bluetooth:
			Bluetooth bluetooth = new Bluetooth(this);
			bluetooth.enableBluetooth();
//			if (!bluetooth.queryingPairedDevices()) {
				// discover
				bluetooth.discoverDevices();
//			}
			break;

		// Si l'utilisateur veut accéder aux charts
		case R.id.goToChart:
			Intent intent = new Intent(AuthentificationActivity.this,
					Measures.class);
			startActivity(intent);

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

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			byte[] writeBuf = (byte[]) msg.obj;
			int begin = (int) msg.arg1;
			int end = (int) msg.arg2;

			switch (msg.what) {
			case 1:
				String writeMessage = new String(writeBuf);
				writeMessage = writeMessage.substring(begin, end);
				break;
			}
		}
	};

}
