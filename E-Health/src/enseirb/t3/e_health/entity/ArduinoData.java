package enseirb.t3.e_health.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;
import enseirb.t3.e_health.DAO.DatabaseHandler;

public class ArduinoData  {

	private String ArduinoData;
	
	// Constructors
	public ArduinoData() {
		ArduinoData = "";
	}
	
	public ArduinoData(String ArduinoData) {
		this.ArduinoData = ArduinoData;
	}
	
	// Getter
    public String getArduinoData() {
    	return ArduinoData;
    }
    
    // Setter
    public void setArduinoData(String ArduinoData) {
    	this.ArduinoData = ArduinoData;
    }
    
    // Methods
    
    // get data from arduino and store it to the database
    public void getAndStoreData(DatabaseHandler dbHandler) {
    	
    	String[] aDataArrayStr = this.splitArduinoPaquets();

		for (int i = 1; i < aDataArrayStr.length; i++) {

			String aDataStr = aDataArrayStr[i];
			
			String[] chunks = this.getChunks(aDataStr);
			this.stockData(chunks,  dbHandler);
		}
    }
    
    // Get data timestamp
    public String getPaquetTimestamp(String firstChunk) {
    	return firstChunk.substring(4, firstChunk.length());
    }
    
    public void stockData(String[] chunks, DatabaseHandler dataDB) {
    	
    	String[] chunkTmp;
    	Data dataTmp = new Data();
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	Long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	
    	for (int i = 1; i < chunks.length; i++) {
    		chunkTmp = chunks[i].trim().split("\\|");
    		dataTmp.setDate(Long.toString((System.currentTimeMillis()/1000) - (paquetTimestamp - Long.parseLong(chunkTmp[0]))));
//    		dataTmp.setDate(chunkTmp[0]);
    		// TODO synchronize timestamp
    		dataTmp.setDataname(chunkTmp[1]);
    		dataTmp.setValue(chunkTmp[2]);
    		
    		//
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", dataTmp.getDate());
    		
    		dataDB.createData(dataTmp);
    	}
    	dataDB.close();
    }
    
    public String[] splitArduinoPaquets() {

    	String aDataStr = this.getArduinoData();
		String[] arduinoDataArray = aDataStr.split("(?=DATA)");

    	return arduinoDataArray;
    }
    
    public String[] getChunks(String ArduinoData) {
    	
    	String[] chunks = ArduinoData.split(";");
    	
    	return chunks;
    }
    
    public Data convertArduinoData(ArduinoData ArduinoData) {
    	
    	Data data = new Data();
    	
    	return data;
    }
    
public String synchronizeTimestamp(String paquetTimestampStr, String dataTimestampStr) {
    	
		Calendar cal = Calendar.getInstance();
		String secPassedStr = cal.getTime().toString();
		double secPassed = Double.parseDouble(secPassedStr);
		double paquetTimestamp = Double.parseDouble(paquetTimestampStr);
    	double dataTimestamp = Double.parseDouble(dataTimestampStr);
    	double realDataTimestamp = dataTimestamp*secPassed/paquetTimestamp;
    	String realDataTimestampStr = String.valueOf(realDataTimestamp);
    	
    	
    	return realDataTimestampStr;
    }
    
    public String getCurrentTime() {
    	
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println("time : " + sdf.format(cal.getTime()) );
    	return sdf.format(cal.getTime());
    }
	
}

