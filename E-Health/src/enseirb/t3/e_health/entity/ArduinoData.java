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
	private final static String TAG = "ArduinoData";
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
    	
    	DataProcess dataProcess = null;
//      DataProcess dataProcess = null;
//      DataProcess dataProcess = new DataProcess();
//      dataProcess.setZeroOxygenCount(0);

    	String[] chunkTmp;
    	Data dataTmp = null;
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	int idAlert = 0;
    	
    	for (int i = 1; i < chunks.length; i++) {
    		
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		
    		if(chunkTmp.length < 3){
    			Log.d("erreur","données invalides");
    			break;
    		}
    		
    		date = new Date((System.currentTimeMillis()/1000) - (paquetTimestamp - Long.parseLong(chunkTmp[0])));
    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date, idPatient);
    		
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", dataTmp.getDate().toString());
    		
    		if (cmpNeedToSave != 0) {
    			EHealth.db.createSavedData(dataTmp, idAlert);
    			EHealth.db.deleteAllData();
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
    		//store alert in DB
//    		Alert alert = new Alert(dataTmp.getIdPatient(), dataTmp.getDate(), "Apnée");
    		EHealth.db.deleteAllAlert();
    		idAlert = EHealth.db.createAlert(alert);
    		cmpNeedToSave = numberDataPerSensor;
    		
    		ArrayList<Data> arraySavedData = new ArrayList<Data>();
    		ArrayList<String> arrayDataname = this.dataProcess.getDataNames();
//    		arrayDataname.add("A");
//    		arrayDataname.add("B");
//    		arrayDataname.add("O");
//    		
    		for (String dataname : arrayDataname) {
	    		arraySavedData = EHealth.db.retrieveDataList(dataname);
	    		for (Data data : arraySavedData)
	    			EHealth.db.createSavedData(data, idAlert);
    		}
    	}
    	
//    	dataDB.close();
    	return arrayData;
    }
    
    public String[] getChunks(String ArduinoData) {
    	
    	String[] chunks = ArduinoData.split(";");
    	
    	return chunks;
    }
}

