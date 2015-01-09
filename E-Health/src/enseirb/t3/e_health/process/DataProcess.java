package enseirb.t3.e_health.process;

import java.util.ArrayList;

import android.util.Log;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;

public class DataProcess {
	private static Alert alert;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	private ArrayList<String> dataNames;
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
	private int standCount;

	// Temprature processing variables
	private int hypoTCount;
	private int hyperTCount;

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

	public int getHypoTCount() {
		return this.hypoTCount;
	}

	public void setHypoTCount(int hypoTCount) {
		this.hypoTCount = hypoTCount;
	}

	public int getHyperTCount() {
		return this.hyperTCount;
	}

	public void setHyperTCount(int hyperTCount) {
		this.hyperTCount = hyperTCount;
	}

	public int getStandCount() {
		return this.standCount;
	}

	public void setStandCount(int standCount) {
		this.standCount = standCount;
	}

	public void process (Data data) {

		this.data = data;

		switch (data.getDataname()) {
		case "A":
			if (Double.parseDouble(data.getValue()) < 30) {
				Log.d(TAG, "Valeur airflow : " + Double.parseDouble(data.getValue()));
				Log.d(TAG, "airflow < 128");
				this.noAirflowCount++;
			}
			else  {
				this.noAirflowCount = 0;
			}
			Log.d(TAG, "noAirflowCmpt = " + this.noAirflowCount);
			if (this.noAirflowCount == 10) {
				Log.d(TAG, "airflow = 10");
				alertes.add("Apnee");
				this.noAirflowCount = 0;
			}
			break;
		case "B":
			if (Double.parseDouble(data.getValue()) > 100) {
				alertes.add("Tachycardie");
			}
			else if (Double.parseDouble(data.getValue()) < 95) {
				alertes.add("Bradycardie");
			}
			break;
		case "O":
			if (Double.parseDouble(data.getValue()) < 1) {
				break;
			}
			else {
				if (Double.parseDouble(data.getValue()) < 99)
					alertes.add("Hypoxemie");
				if (Double.parseDouble(data.getValue()) < 5) {
					this.setZeroOxygenCount(this.getZeroOxygenCount() + 1);	
					//				Log.d(TAG, "Hypoxemie");
					//				Log.d(TAG, "zeroOxygenCount = " + this.getZeroOxygenCount());
					if (this.zeroOxygenBlockCount > 0 && this.positiveOxygen == true) {

						alertes.add("Apnee");
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

					this.setZeroOxygenCount(0);
					this.positiveOxygen = true;
				}
			}
			break;
		case "P":
			this.setPreviousPosition(position);
			if (Double.parseDouble(data.getValue()) == 1) {
				this.setPosition(1);
			} else if (Double.parseDouble(data.getValue()) == 2) {
				this.setPosition(2);
			} else if (Double.parseDouble(data.getValue()) == 3) {
				this.setPosition(3);
			} else if (Double.parseDouble(data.getValue()) == 4) {
				this.setPosition(4);
			}
			// position debout
			else if (Double.parseDouble(data.getValue()) == 5) {
				this.setPosition(5);
				this.standCount++;
				if (standCount == 20) {

					alertes.add("Somnambulisme");
				}
			}
			break;
		case "T":
			if (Double.parseDouble(data.getValue()) > 38) {
				this.hyperTCount++;

				if (this.hyperTCount == 11) {

					this.hyperTCount = 0;
					Log.d("alerte", "hyperthermie");
					alertes.add("Hyperthermie");
					//	this.dataNames.add("T");
				}
			}
			else if (Double.parseDouble(data.getValue()) < 35) {
				this.hypoTCount ++;

				if (this.hypoTCount == 10) {

					this.hypoTCount = 0;
					Log.d("alerte", "hypothermie");
					alertes.add("Hypothermie");
				}
			}
			break;
		case "C":
			if (Math.abs(Double.parseDouble(data.getValue())) > 5) {
				Log.d("alerte", "sueur");
				alertes.add("Sueur");
			}
			break;
		case "R":
			// Sueur déterminée par la conductivité
			// Sueur : R < 1500 Ohm
//			if (Math.abs(Double.parseDouble(data.getValue())) < 1500) {
//				
//				Log.d("resistance", "R = " + Double.parseDouble(data.getValue()));
//				alertes.add("Sueur");
//			}
			break;
		case "V":
			// Sueur déjà déterminée par la conductivité
			break;
		default :
			break;
		}
	}

	public Alert correlation () {
		alert = null;
		this.dataNames = new ArrayList<String>();

		if (alertes.contains("Tachycardie") && alertes.contains("Sueur") && this.getPosition() != 5 && this.getPreviousPosition() == 5) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Malaise");
			this.dataNames.add("B");
			this.dataNames.add("R");
			this.dataNames.add("P");
			return alert;
		}

		if (alertes.contains("Bradycardie") && alertes.contains("Hypoxemie")) {
			if (alertes.contains("Sueur")) {
				alert = new Alert(data.getIdPatient(), data.getDate(), "Angine de poitrine");
				this.dataNames.add("B");
				this.dataNames.add("O");
				this.dataNames.add("R");
			}
		}

		if (alertes.contains("Apnee")) {

			alert = new Alert(data.getIdPatient(), data.getDate(), "Apnee");
			this.dataNames.add("A");
			this.dataNames.add("B");
			this.dataNames.add("O");
		}

		if (alertes.contains("Tachycardie") && alertes.contains("Hypoxemie") && alertes.contains("Sueur")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Problème cardiaque");
			this.dataNames.add("B");
			this.dataNames.add("O");
			this.dataNames.add("R");
		}

		if  (alertes.contains("Hypothermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
			this.dataNames.add("T");
		}

		if  (alertes.contains("Hyperthermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
			this.dataNames.add("T");
		}

		if  (alertes.contains("Somnambulisme")) {
			this.dataNames.add("P");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Somnambulisme");
		}

		alertes = new ArrayList<String>();
		return alert;
	}
}