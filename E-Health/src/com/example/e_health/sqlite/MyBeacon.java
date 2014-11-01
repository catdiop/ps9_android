package com.example.e_health.sqlite;

public class MyBeacon {
	
	private int id;
	private String name;
	private String macAddr;
	private String uuid;
	private String major;
	private String minor;

	public MyBeacon(){}
	
	public MyBeacon(String name, String macAddr, String uuid, String major, String minor){
		this.setMacAddr(macAddr);
		this.setName(name);
		this.setUuid(uuid);
		this.setMajor(major);
		this.setMinor(minor);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
