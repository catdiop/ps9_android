package enseirb.t3.e_health.activity;

import android.app.Application;
import android.util.Log;
import enseirb.t3.e_health.DAO.DatabaseHandler;

public class EHealth extends Application {
	
	public static DatabaseHandler db;
	
	public static String TAG = "EHealth";

	@Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started up");
        db = new DatabaseHandler(this);
	}
	
}
