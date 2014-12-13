package enseirb.t3.e_health.process;

import java.util.ArrayList;

import android.util.Log;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Patient;

public class DataProcess {
	private static Alert alert;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	private ArrayList<String> dataNames= new ArrayList<String>();
	
	// Oxygen processing variables
	private int zeroOxygenCount;
	private int zeroOxygenBlockCount;
	private int noAirflowCount;
	private int noAirflowBlockCount;
	private boolean positiveOxygen;
	
	public ArrayList<String> getDataNames() {
		return this.dataNames;
	}
	
	public int getZeroOxygenCount() {
		return this.zeroOxygenCount;
	}

	public void setZeroOxygenCount(int zeroOxygenCount) {
		this.zeroOxygenCount = zeroOxygenCount;
	}
	
	public int getZeroOxygenBlockCount() {
		return this.zeroOxygenBlockCount;
	}

	public void setZeroOxygenBlockCount(int zeroOxygenBlockCount) {
		this.zeroOxygenBlockCount = zeroOxygenBlockCount;
	}

	public void setPositiveOxygen (boolean positiveOxygen) {

		this.positiveOxygen = positiveOxygen;
	}
	
	public int getNoAirflowCount() {
		return this.noAirflowCount;
	}

	public void setNoAirflowCount(int noAirflowCount) {
		this.noAirflowCount = noAirflowCount;
	}
	
	public DataProcess () {
		
		this.data = null;
	}
	
	public DataProcess (Data data) {
		this.data = data;
	}
	
	
	
	public void process (Data data) {

		switch (data.getDataname()) {
		/* Voir calcul d'IAH
		 Apnée centrale : arrêt total du flux respiratoire pendant au moins 10 s
		 Hypopnée : diminution du flux respiratoire de 50 % pendant au moins 10 s
		 Pour l'instant : pas de différenciation des deux vu qu'on ne calcule pas l'IAH */
		case "A":
			if (Double.parseDouble(data.getValue()) < 512) {
				
				Log.d("airflow", "< 512");
				this.setNoAirflowCount(this.noAirflowCount + 1);
			}
			else {
				
				this.setNoAirflowCount(0);
			}
			if (this.noAirflowCount == 10) {
				
				Log.d("airflow", "Apnée");
				alertes.add("Apnée");
				this.dataNames.add("A");
				this.noAirflowBlockCount = 0;
			}
			break;
		case "B":
			if (Double.parseDouble(data.getValue()) > 100) {
				alertes.add("Tachycardie");
				this.dataNames.add("B");
			}
			else if (Double.parseDouble(data.getValue()) < 60) {
				alertes.add("Bradycardie");
				this.dataNames.add("B");
			}
			break;
		case "O":
			if (Double.parseDouble(data.getValue()) < 90) {
				if (Double.parseDouble(data.getValue()) < 5) {

					this.setZeroOxygenCount(this.getZeroOxygenCount() + 1);	
					Log.d("oxygen", "< 5");
					Log.d("zeroOxygenCount", "" + this.getZeroOxygenCount());
					if (this.zeroOxygenBlockCount > 0 && this.positiveOxygen == true) {
						
						alertes.add("Apnée");
						this.setZeroOxygenBlockCount(0);
						this.dataNames.add("O");
						break;
					}
					
					if (this.getZeroOxygenCount() == 10) {
						
						this.setZeroOxygenBlockCount(this
								.getZeroOxygenBlockCount() + 1);
						this.setZeroOxygenCount(0);
					}

					this.positiveOxygen = false;
				}
				else {

					alertes.add("Hypoxémie");
					this.dataNames.add("O");
				}
			}
			else {
				
				Log.d("Oxygen", "Remis à 0");
				this.setZeroOxygenCount(0);
				this.positiveOxygen = true;
			}
			break;
		case "P":
			break;
		case "T":
			if (Double.parseDouble(data.getValue()) > 38) {
				alertes.add("Hyperthermie");
				this.dataNames.add("T");
			}
			else if (Double.parseDouble(data.getValue()) < 35) {
				alertes.add("Hypothermie");
				this.dataNames.add("T");
			}
			break;
		default :
			break;
		}
	}
	
	public Alert correlation () {
		
		if (alertes.contains("Bradycardie")) {
			if (alertes.contains("Hypoxémie")) {
				/* Ajouter valeurs du capteur galvanique pour déterminer une angine de poitrine */ 
				alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
				return alert;
			} else {
				alert = new Alert(data.getIdPatient(), data.getDate(), "Bradycardie");
				return alert;
			}
		} else if (alertes.contains("Apnée")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
		} else if (alertes.contains("Hypoxémie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypoxémie");
			return alert;
		} else if  (alertes.contains("Hypothermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
			return alert;
		} else if  (alertes.contains("Hyperthermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
			return alert;
		}
		return null;
	}
}
