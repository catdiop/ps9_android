package enseirb.t3.e_health.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.project.e_health.R;

import enseirb.t3.e_health.activity.EHealth;
import enseirb.t3.e_health.activity.GraphAlertActivity;
import enseirb.t3.e_health.bluetooth.BtThread;
import enseirb.t3.e_health.process.DataProcess;

public class ArduinoData  {

	private Date date;
	private ArrayList<Data> arrayData = new ArrayList<Data>();
	private static int numberSensor;
	private final static int numberDataPerSensor = 15;
	private static int numberData;
	private DataProcess dataProcess = null;
	private Alert alert;
	private int cmpNeedToSave = 0;
	private BtThread btThread;
	private String TAG = "ArduinoData";
	private ArrayList<String> arrayDataname = new ArrayList<String>();
	private int idAlert;
	private int mId=2;
	private Context context;
	
	public ArduinoData(BtThread bt, DataProcess dataProcess) {
		btThread = bt;
		this.dataProcess = dataProcess;
	}
	
	public ArduinoData(BtThread bt, DataProcess dataProcess, Context context) {
		btThread = bt;
		this.dataProcess = dataProcess;
		this.context=context;
	}
    
    // Get data timestamp
    public String getPaquetTimestamp(String firstChunk) {
    	return firstChunk.substring(4, firstChunk.length());
    }
    
    public ArrayList<Data> stockData(String[] chunks, int idPatient) {
    	String[] chunkTmp;
    	Data dataTmp = null;
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	arrayData = new ArrayList<Data>();
    	long currentTime = System.currentTimeMillis();
    	
    	for (int i = 1; i < chunks.length; i++) {
    		
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		if(chunkTmp.length < 3){
    			Log.d("erreur","donn�es invalides");
    			break;
    		}
    		
    		date = new Date(currentTime - (paquetTimestamp - Long.parseLong(chunkTmp[0])));
//    		date = new Date(currentTime);
    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date, idPatient);
    		
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", date.toString());
    		
    		if (cmpNeedToSave != 0 && arrayDataname.contains(dataTmp.getDataname())) {
    			Log.d(TAG, "idAlert no alert = " + Integer.toString(idAlert));
    			EHealth.db.createSavedData(dataTmp, idAlert);
    			cmpNeedToSave--;
    			Log.d(TAG, "cmpNeedToSave = " + Integer.toString(cmpNeedToSave));
    		} else if (cmpNeedToSave == 0 && arrayDataname.contains(dataTmp.getDataname())) {
    			btThread.write("STOP\n".getBytes());
    			arrayDataname = new ArrayList<String>();
    			idAlert = 0;
    		} else if (cmpNeedToSave == 0) {
        		dataProcess.process(dataTmp);
    			EHealth.db.createData(dataTmp);
        		if (EHealth.db.getNumberData() > (chunks.length - 1)*numberDataPerSensor)
        			EHealth.db.deleteLastData();
    		}
    		arrayData.add(dataTmp);
    	}
    	if ((alert = dataProcess.correlation()) != null) {

    		idAlert = EHealth.db.createAlert(alert);
    		sendNotification(alert.getAlertName(), idAlert);
    		cmpNeedToSave = numberData;
    		
    		try {
				btThread.write("MORE\n".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		arrayDataname = dataProcess.getDataNames();
    		
        	numberSensor = arrayDataname.size();
        	numberData = numberDataPerSensor*numberSensor;
    		cmpNeedToSave = numberData;
    		Log.d(TAG, "SensorNbre = " + Integer.toString(numberSensor));
 		
    		//TODO appel a la BDD
    		for (String dataname : arrayDataname) {
	    		EHealth.db.moveDataToSavedData(dataname, idAlert);
	    		Log.d(TAG, "idAlert alerte = " + Integer.toString(idAlert));
//	    		for (Data data : arraySavedData)
//	    			EHealth.db.createSavedData(data, idAlert);
    		}
			EHealth.db.deleteAllData();
    	}
    	return arrayData;
    }
    
    public String[] getChunks(String ArduinoData) {
    	return ArduinoData.split(";");
    }
    
    private void sendNotification(String typeAlert, int idAlert){
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(context)
    	        .setSmallIcon(R.drawable.ic_action_bluetooth)
    	        .setContentTitle("Alert E-Health")
    	        .setContentText("Une nouvelle alerte "+typeAlert+" vient d'être détectée");
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(context, GraphAlertActivity.class);
    	resultIntent.putExtra("alertId", idAlert);
    	PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    	mBuilder.setContentIntent(contentIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(mId, mBuilder.build());
    }
}

