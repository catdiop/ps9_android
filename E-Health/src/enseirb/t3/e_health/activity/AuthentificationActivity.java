package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.DAO.UserDatabaseHandler;

/**
 * @author catdiop
 *
 */
public class AuthentificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_authentification);
	}



	public void onClick(View v) {

		//Si l'utilisateur appuit sur connexion
		if(v.getId()==R.id.connexion) {
			UserDatabaseHandler dbHandler=new UserDatabaseHandler(this.getApplicationContext());
			TextView view1=(TextView)findViewById(R.id.username);
			TextView view2=(TextView)findViewById(R.id.password);
			if(dbHandler.isUser(view1.getText().toString(), view2.getText().toString())) {
				//on se connecte
			}
			else
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
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

