package enseirb.t3.e_health.entity;

import java.util.Date;

import android.util.Log;
import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.process.DataProcess;

public class ArduinoData  {

	private String arduinoData;
	private Date date;

	public ArduinoData(String arduinoData) {
		this.arduinoData = arduinoData;
	}
    
    // Methods
    
    // get data from arduino and store it to the database
    public void getAndStoreData(DatabaseHandler dbHandler) {
    	
    	String[] aDataArrayStr = arduinoData.split("(?=DATA)");

		for (int i = 1; i < aDataArrayStr.length; i++)	
			stockData(getChunks(aDataArrayStr[i]),  dbHandler);
    }
    
    // Get data timestamp
    public String getPaquetTimestamp(String firstChunk) {
    	return firstChunk.substring(4, firstChunk.length());
    }
    
    public void stockData(String[] chunks, DatabaseHandler dataDB) {
    	
    	DataProcess dataProcess = null;
    	String[] chunkTmp;
    	Data dataTmp = null;
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	Long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	
    	for (int i = 1; i < chunks.length; i++) {
    		
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		
    		if(chunkTmp.length < 3){
    			Log.d("erreur","données invalides");
    			break;
    		}
    		
    		date = new Date((System.currentTimeMillis()/1000) - (paquetTimestamp - Long.parseLong(chunkTmp[0])));

    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date);
    		
    		//
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", dataTmp.getDate().toString());
    		
    		dataProcess = new DataProcess(dataTmp);
    		dataProcess.process(dataTmp);
    		
    		dataDB.createData(dataTmp);
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

