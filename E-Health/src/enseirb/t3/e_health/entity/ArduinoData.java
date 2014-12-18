package enseirb.t3.e_health.entity;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;
import enseirb.t3.e_health.activity.EHealth;
import enseirb.t3.e_health.process.DataProcess;

public class ArduinoData  {

	private Date date;
	private ArrayList<Data> arrayData = new ArrayList<Data>();
	private final static int numberSensor = 3;
	private final static int numberDataPerSensor = 15;
	private final static int numberData = numberDataPerSensor*numberSensor;
	private DataProcess dataProcess = null;
	private Alert alert;
	private int cmpNeedToSave = 0;

	public ArduinoData(DataProcess dataProcess) {
		this.dataProcess = dataProcess;
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
    	int idAlert = 0;
    	arrayData = new ArrayList<Data>();
    	
    	for (int i = 1; i < chunks.length; i++) {
    		
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		
    		if(chunkTmp.length < 3){
    			Log.d("erreur","donn�es invalides");
    			break;
    		}
    		
    		date = new Date((System.currentTimeMillis()) - (paquetTimestamp - Long.parseLong(chunkTmp[0])));
    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date, idPatient);
    		
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", date.toString());
    		
    		if (cmpNeedToSave != 0) {
    			EHealth.db.createSavedData(dataTmp, idAlert);
    			cmpNeedToSave--;
    		} else {
        		this.dataProcess.process(dataTmp);
    			EHealth.db.createData(dataTmp);
        		if (EHealth.db.getNumberData() > numberData)
        			EHealth.db.deleteLastData();
    		}
    		arrayData.add(dataTmp);
    	}
    	if ((alert = this.dataProcess.correlation()) != null) {

    		idAlert = EHealth.db.createAlert(alert);
    		cmpNeedToSave = numberData;
    		
    		ArrayList<Data> arraySavedData = new ArrayList<Data>();
    		ArrayList<String> arrayDataname = this.dataProcess.getDataNames();
 		
    		for (String dataname : arrayDataname) {
	    		arraySavedData = EHealth.db.retrieveDataList(dataname);
	    		for (Data data : arraySavedData)
	    			EHealth.db.createSavedData(data, idAlert);
    		}
			EHealth.db.deleteAllData();
    	}
    	return arrayData;
    }
    
    public String[] getChunks(String ArduinoData) {
    	return ArduinoData.split(";");
    }
}

