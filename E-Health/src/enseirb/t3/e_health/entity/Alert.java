package enseirb.t3.e_health.entity;

import java.util.Date;

public class Alert {

	private Patient patient;
	private Date date;
	private String alertName;
	
	public Alert(Patient patient, Date date, String alertName){
		this.patient=patient;
		this.date=date;
		this.alertName = alertName;
	}
	
	public Patient getPatient(){
		return this.patient;
	}
	public void setPatient(Patient patient){
		this.patient=patient;
	}
	public Date getDate(){
		return this.date;
	}
	public void setDate(Date date){
		this.date=date;
	}
	public String getAlertName(){
		return this.alertName;
	}
	public void setAlertName(String alertName){
		this.alertName = alertName;
	}
}
