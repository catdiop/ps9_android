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
	private boolean positiveOxygen;
	
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
	
	public DataProcess () {
		
		this.data = null;
	}
	
	public DataProcess (Data data) {
		this.data = data;
	}
	
	public void process (Data data) {

		switch (data.getDataname()) {
		case "A":
			break;
		case "B":
			if (Double.parseDouble(data.getValue()) > 100)
				alertes.add("Tachycardie");
			else if (Double.parseDouble(data.getValue()) < 60)
				alertes.add("Bradycardie");
			break;
		case "O":
			if (Double.parseDouble(data.getValue()) < 100) {
				if (Double.parseDouble(data.getValue()) < 99) {

					this.setZeroOxygenCount(this.getZeroOxygenCount() + 1);	
					Log.d("oxygen", "< 5");
					Log.d("zeroOxygenCount", "" + this.getZeroOxygenCount());
					if (this.zeroOxygenBlockCount > 0 && this.positiveOxygen == true) {
						
						alertes.add("Apnée");
						this.setZeroOxygenBlockCount(0);
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
			if (Double.parseDouble(data.getValue()) > 38)
				alertes.add("Hyperthermie");
			else if (Double.parseDouble(data.getValue()) < 35)
				alertes.add("Hypothermie");
			break;
		default :
			break;
		}
	}
	
	public Alert correlation () {
		
		if (alertes.contains("Bradycardie")) {
			this.dataNames.add("B");
			if (alertes.contains("Hypoxémie")) {
				this.dataNames.add("O");
				/* Ajouter valeurs du capteur galvanique pour déterminer une angine de poitrine */ 
				alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
				return alert;
			} else {
				alert = new Alert(data.getIdPatient(), data.getDate(), "Bradycardie");
				return alert;
			}
		} else if (alertes.contains("Apnée")) {
			this.dataNames.add("O");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Apnée");
		} else if (alertes.contains("Hypoxémie")) {
			this.dataNames.add("O");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypoxémie");
			return alert;
		} else if  (alertes.contains("Hypothermie")) {
			this.dataNames.add("T");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
			return alert;
		} else if  (alertes.contains("Hyperthermie")) {
			this.dataNames.add("T");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
			return alert;
		}
		return null;
	}
}
