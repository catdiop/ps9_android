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
    
    // Get data timestamp
    public String getPaquetTimestamp(String firstChunk) {
    	
    	String date = new String();
    	date = firstChunk.substring(4, firstChunk.length());
    	
    	return date;
    }
    
    public void stockData(String[] chunks, DatabaseHandler dataDB) {
    	
    	String[] chunkTmp;
    	Data dataTmp = new Data();
    	String paquetTimestamp = getPaquetTimestamp(chunks[0]);
    	
    	for (int i = 1; i < chunks.length; i++) {
    		chunkTmp = chunks[i].trim().split("\\|");
    		dataTmp.setDate(chunkTmp[0]);
    		// TODO synchronize timestamp
    		dataTmp.setDataname(chunkTmp[1]);
    		dataTmp.setValue(chunkTmp[2]);
    		
    		//
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		
    		dataDB.createData(dataTmp);
    	}
    	dataDB.close();
    }
    
    public String[] getChunks(String ArduinoData) {
    	
    	String[] chunks = ArduinoData.split(";");
    	
    	return chunks;
    }
    
    public int getPaquetTimestamp(ArduinoData ArduinoData) {
    	
    	int paquetTimestamp = 0;
    	
    	return paquetTimestamp;
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

