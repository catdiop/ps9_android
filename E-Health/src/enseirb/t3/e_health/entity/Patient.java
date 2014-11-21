package enseirb.t3.e_health.entity;

import java.util.ArrayList;
import java.util.List;
import enseirb.t3.e_health.entity.Data;

public class Patient extends User{

	private Doctor doctor;
	private List<Data> dataList;
	
	public Patient(String username, String password) {
		super(username, password);
	}
	
	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public void newData (String name, String value, String date) {
		dataList = new ArrayList<Data>();
		Data data = new Data(name, value, date);
		dataList.add(data);
	}
}