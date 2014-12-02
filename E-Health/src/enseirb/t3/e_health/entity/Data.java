package enseirb.t3.e_health.entity;

import java.util.Date;

public class Data {
	private int id;
	private String dataname;
	private String value;
	private Date date;
	
	public Data (String dataname, String value, Date date) {
		this.dataname = dataname;
		this.value = value;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDataname() {
		return dataname;
	}

	public void setDataname(String dataname) {
		this.dataname = dataname;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
