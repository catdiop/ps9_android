package enseirb.t3.e_health.entity;

import java.util.Date;

public class Alert {

	private Patient patient;
	private Date date;
	private String dataName;
	
	public Alert(){
		this.patient=null;
		this.date=null;
		this.dataName=null;
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
	public String getDataName(){
		return this.dataName;
	}
	public void setDataName(String dataName){
		this.dataName=dataName;
	}
}
