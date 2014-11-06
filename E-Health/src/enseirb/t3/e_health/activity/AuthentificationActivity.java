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

import enseirb.t3.e_health.DAO.UserDatabaseHandler;
import enseirb.t3.e_health.bluetooth.Bluetooth;

/**
 * @author catdiop
 *
 */
public class AuthentificationActivity extends Activity implements OnClickListener {

	private final static int  REQUEST_ENABLE_BT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_authentification);

		Button bluetooth = (Button) findViewById(R.id.bluetooth);		
		bluetooth.setOnClickListener(this);

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		//Si l'utilisateur appuit sur connexion
		case R.id.connexion:
			UserDatabaseHandler dbHandler=new UserDatabaseHandler(this.getApplicationContext());
			TextView view1=(TextView)findViewById(R.id.username);
			TextView view2=(TextView)findViewById(R.id.password);
			if(dbHandler.isUser(view1.getText().toString(), view2.getText().toString())) {
				//on se connecte
			}
			else {
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			}
			break;

			//Si l'utilisateur active le bluetooth
		case R.id.bluetooth:
			Bluetooth bluetooth = new Bluetooth(this);
			bluetooth.enableBluetooth();

			break;

		case R.id.goToChart:
			Intent intent = new Intent(AuthentificationActivity.this, Measures.class);
			startActivity(intent);

			break;
		}
	}



	private void createDialog(String msg) {

		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
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

