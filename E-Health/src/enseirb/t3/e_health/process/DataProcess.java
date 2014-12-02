package enseirb.t3.e_health.process;

import java.util.ArrayList;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Patient;

public class DataProcess {
	private static Alert alert;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	
	Patient patient = new Patient();
	
	public DataProcess (Data data) {
		this.data = data;
	}
	
	public void process (Data data) {

		switch (data.getDataname()) {
		case "A":
			break;
		case "B":
			if (Integer.parseInt(data.getValue()) > 100)
				alertes.add("Tachycardie");
			else if (Integer.parseInt(data.getValue()) < 60)
				alertes.add("Bradycardie");
			break;
		case "O":
			if (Integer.parseInt(data.getValue()) < 90)
				alertes.add("Hypoxémie");
			break;
		case "P":
			break;
		case "T":
			if (Integer.parseInt(data.getValue()) > 38)
				alertes.add("Hyperthermie");
			else if (Integer.parseInt(data.getValue()) < 35)
				alertes.add("Hypothermie");
			break;
		default :
			break;
		}
	}
	
	public Alert correlation () {
		patient.setFirstname("Jean");
		patient.setLastname("Polo");
		
		if (alertes.contains("Bradycardie")) {
			if (alertes.contains("Hypoxémie")) {
				alert = new Alert(patient, data.getDate(), "Apnée");
				return alert;
			} else {
				alert = new Alert(patient, data.getDate(), "Bradycardie");
				return alert;
			}
		} else if (alertes.contains("Hypoxémie")) {
			alert = new Alert(patient, data.getDate(), "Hypoxémie");
			return alert;
		} else if  (alertes.contains("Hypothermie")) {
			alert = new Alert(patient, data.getDate(), "Hypothermie");
			return alert;
		} else if  (alertes.contains("Hyperthermie")) {
			alert = new Alert(patient, data.getDate(), "Hyperthermie");
			return alert;
		}
		return null;
	}

}
