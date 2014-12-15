package enseirb.t3.e_health.process;

import java.util.ArrayList;

import android.util.Log;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Patient;

public class DataProcess {
	private Alert alert = null;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	private ArrayList<String> dataNames = new ArrayList<String>();
	private String TAG = "DataProcess";
	
	// Oxygen processing variables
	private int zeroOxygenCount;
	private int zeroOxygenBlockCount = 0;
	private int noAirflowCount = 0;
	private final static int noAirflowBlockCount = 10;
	private boolean positiveOxygen;
	
	public ArrayList<String> getDataNames() {
		return this.dataNames;
	}
	
	public DataProcess (Data data) {
		this.data = data;
	}	
	
	public DataProcess() {
	}

	public void process (Data data) {
		
		this.data = data;
		dataNames = new ArrayList<String>();

		switch (data.getDataname()) {
		/* Voir calcul d'IAH
		 Apnée centrale : arrêt total du flux respiratoire pendant au moins 10 s
		 Hypopnée : diminution du flux respiratoire de 50 % pendant au moins 10 s
		 Pour l'instant : pas de différenciation des deux vu qu'on ne calcule pas l'IAH */
		case "A":
			if (Double.parseDouble(data.getValue()) < 512) {
				Log.d(TAG, "airflow < 128");
				noAirflowCount++;
			}
			else 
				noAirflowCount = 0;
			if (noAirflowCount == noAirflowBlockCount) {
				Log.d(TAG, "airflow = 10");
				alertes.add("Apnee");
				noAirflowCount = 0;
			}
			break;
		case "B":
//			if (Double.parseDouble(data.getValue()) > 95) {
//				alertes.add("Tachycardie");
//				dataNames.add("B");
//			}
//			else if (Double.parseDouble(data.getValue()) < 60) {
//				alertes.add("Bradycardie");
//				dataNames.add("B");
//			}
			break;
		case "O":
//			if (Double.parseDouble(data.getValue()) < 90) {
//				if (Double.parseDouble(data.getValue()) < 5) {
//
//					zeroOxygenCount++;	
//					Log.d("oxygen", "< 5");
//					Log.d(TAG, "oxygencount" + zeroOxygenCount);
//					if (zeroOxygenBlockCount > 0 && positiveOxygen == true) {
//						
//						alertes.add("Apnée");
//						zeroOxygenBlockCount = 0;
//						dataNames.add("O");
//						break;
//					}
//					
//					if (zeroOxygenCount == 10) {
//						
//						zeroOxygenBlockCount++;
//						zeroOxygenCount = 0;
//					}
//
//					positiveOxygen = false;
//				}
//				else {
//
//					alertes.add("Hypoxémie");
//					dataNames.add("O");
//				}
//			}
//			else {
//				
//				Log.d(TAG, "Oxygen Remis à 0");
//				zeroOxygenCount = 0;
//				positiveOxygen = true;
//			}
			break;
		case "P":
			break;
		case "T":
//			if (Double.parseDouble(data.getValue()) > 38) {
//				alertes.add("Hyperthermie");
//				dataNames.add("T");
//			}
//			else if (Double.parseDouble(data.getValue()) < 35) {
//				alertes.add("Hypothermie");
//				dataNames.add("T");
//			}
			break;
		default :
			break;
		}
	}
	
	public Alert correlation () {
		
//		if (alertes.contains("Bradycardie")) {
//			if (alertes.contains("Hypoxemie")) {
//				/* Ajouter valeurs du capteur galvanique pour déterminer une angine de poitrine */ 
//				alert = new Alert(data.getIdPatient(), data.getDate(), "Apnee");
//				return alert;
//			} else {
//				alert = new Alert(data.getIdPatient(), data.getDate(), "Bradycardie");
//				return alert;
//			}
//		} else if (alertes.contains("Apnee")) {
//			alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
//		} else if (alertes.contains("Hypoxémie")) {
//			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypoxémie");
//			return alert;
//		} else if  (alertes.contains("Hypothermie")) {
//			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
//			return alert;
//		} else if  (alertes.contains("Hyperthermie")) {
//			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
//			return alert;
//		}
		if (alertes.contains("Apnee")) {
			alertes = new ArrayList<String>();
			alert = new Alert(this.data.getIdPatient(), this.data.getDate(), "Apnee");
			dataNames.add("A");
			dataNames.add("B");
			dataNames.add("O");
		return alert;
		}
		return null;
	}
}
