package enseirb.t3.e_health.process;

import java.util.ArrayList;

import android.util.Log;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;

public class DataProcess {
	private static Alert alert;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	private ArrayList<String> dataNames= new ArrayList<String>();
	private String TAG = "DataProcess";

	// Oxygen processing variables
	private int zeroOxygenCount;
	private int zeroOxygenBlockCount;
	private boolean positiveOxygen;

	// Airflow processing variables
	private int noAirflowCount;
	private int noAirflowBlockCount;

	// Position processing variables
	private int position;
	private int previousPosition;

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

	public int getNoAirflowBlockCount() {
		return this.noAirflowBlockCount;
	}

	public void setNoAirflowBlockCount(int noAirflowBlockCount) {
		this.noAirflowBlockCount = noAirflowBlockCount;
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPreviousPosition() {
		return this.previousPosition;
	}

	public void setPreviousPosition(int previousPosition) {
		this.previousPosition = previousPosition;
	}

	public DataProcess () {

		this.data = null;
	}

	public DataProcess (Data data) {
		this.data = data;
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
			if (Double.parseDouble(data.getValue()) < 30) {
				Log.d(TAG, "airflow < 128");
				noAirflowCount++;
			}
			else 
				noAirflowCount = 0;
			Log.d(TAG, "noAirflowCmpt =" + noAirflowCount);
			if (noAirflowCount == 10) {
				Log.d(TAG, "airflow = 10");
				alertes.add("Apnee");
				noAirflowCount = 0;
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
			this.setPreviousPosition(position);
			if (Double.parseDouble(data.getValue()) == 1) {
				Log.d("alerte", "position couchée");
				this.setPosition(1);
			} else if (Double.parseDouble(data.getValue()) == 2) {
				Log.d("alerte", "position couchée");
				this.setPosition(2);
			} else if (Double.parseDouble(data.getValue()) == 3) {
				Log.d("alerte", "position couchée");
				this.setPosition(3);
			} else if (Double.parseDouble(data.getValue()) == 4) {
				Log.d("alerte", "position couchée");
				this.setPosition(4);
			} else if (Double.parseDouble(data.getValue()) == 5) {
				Log.d("alerte", "position debout ou couchée");
				this.setPosition(5);
			}
			break;
		case "T":
			if (Double.parseDouble(data.getValue()) > 38) {
				Log.d("alerte", "hyperthermie");
				alertes.add("Hyperthermie");
				this.dataNames.add("T");
			}
			else if (Double.parseDouble(data.getValue()) < 35) {
				Log.d("alerte", "hypothermie");
				alertes.add("Hypothermie");
				this.dataNames.add("T");
			}
			break;
		case "C":
			// Sueur déjà déterminée par la résistance
			break;
		case "R":
			// Sueur : R < 1500 Ohm
			if (Math.abs(Double.parseDouble(data.getValue())) < 1500) {
				
				Log.d("alerte", "sueur");
				alertes.add("Sueur");
			}
			break;
		case "V":
			// Sueur déjà déterminée par la résistance
			break;
		default :
			break;
		}
	}
	
	public Alert correlation () {
		
		if (alertes.contains("Apnee")) {
			alertes = new ArrayList<String>();
			alert = new Alert(this.data.getIdPatient(), this.data.getDate(), "Apnee");
			dataNames.add("A");
			return alert;
		}
		
		
		if (alertes.contains("Tachycardie") && alertes.contains("Sueur") && this.getPosition() != 5 && this.getPreviousPosition() == 5) {
			alertes = new ArrayList<String>();
			alert = new Alert(data.getIdPatient(), data.getDate(), "Malaise");
			this.dataNames.add("B");
			this.dataNames.add("R");
//			this.dataNames.add("P");
			return alert;
		}
		
		if (alertes.contains("Bradycardie") && alertes.contains("Hypoxémie")) {
			alertes = new ArrayList<String>();
			if (alertes.contains("Sueur")) {
				
				alert = new Alert(data.getIdPatient(), data.getDate(), "Angine de poitrine");
				this.dataNames.add("B");
				this.dataNames.add("O");
				this.dataNames.add("R");
				return alert;
			}
			
			else {
				
				alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
				this.dataNames.add("B");
				this.dataNames.add("O");
				return alert;
			}
		}
		
		if (alertes.contains("Tachycardie") && alertes.contains("Hypoxémie") && alertes.contains("Sueur")) {
			alertes = new ArrayList<String>();
			alert = new Alert(data.getIdPatient(), data.getDate(), "Problème cardiaque");
			this.dataNames.add("B");
			this.dataNames.add("O");
			this.dataNames.add("R");

			return alert;
		}
		
		if (alertes.contains("Hypoxémie")) {
			alertes = new ArrayList<String>();
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypoxémie");
			this.dataNames.add("O");
			return alert;
		}
		
		if  (alertes.contains("Hypothermie")) {
			alertes = new ArrayList<String>();
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
			this.dataNames.add("T");
			return alert;
		}
		
		if  (alertes.contains("Hyperthermie")) {
			alertes = new ArrayList<String>();
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
			this.dataNames.add("T");
			return alert;
		}
		
		return null;
	}
}