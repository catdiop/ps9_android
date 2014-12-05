package enseirb.t3.e_health.entity;

import java.util.Date;

import android.util.Log;
import enseirb.t3.e_health.activity.EHealth;
import enseirb.t3.e_health.process.DataProcess;

public class ArduinoData  {

	private String arduinoData;
	private Date date;
	
	public double value;

	public ArduinoData(String arduinoData) {
		this.arduinoData = arduinoData;
	}
    
    // Methods
    
    // get data from arduino and store it to the database
    public double getAndStoreData() {
    	
    	String[] aDataArrayStr = arduinoData.split("(?=DATA)");

		for (int i = 1; i < aDataArrayStr.length; i++)	
			stockData(getChunks(aDataArrayStr[i]));
		
		return value;
    }
    
    // Get data timestamp
    public String getPaquetTimestamp(String firstChunk) {
    	return firstChunk.substring(4, firstChunk.length());
    }
    
    public void stockData(String[] chunks) {
    	
    	DataProcess dataProcess = null;
    	String[] chunkTmp;
    	Data dataTmp = null;
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	
    	for (int i = 1; i < chunks.length; i++) {
    		
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		
    		if(chunkTmp.length < 3){
    			Log.d("erreur","donn�es invalides");
    			break;
    		}
    		
    		date = new Date((System.currentTimeMillis()/1000) - (paquetTimestamp - Long.parseLong(chunkTmp[0])));

    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date);
    		
    		if(chunkTmp[1].equals("O")){
    			value = Double.parseDouble(chunkTmp[2]);
    		}
    		
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", dataTmp.getDate().toString());
    		
    		dataProcess = new DataProcess(dataTmp);
    		dataProcess.process(dataTmp);
    		
    		EHealth.db.createData(dataTmp);
    	}
    	//if (dataProcess.correlation() != null)
    		//store alert in DB
    	
//    	dataDB.close();
    }
    
    public String[] getChunks(String ArduinoData) {
    	
    	String[] chunks = ArduinoData.split(";");
    	
    	return chunks;
    }
}

