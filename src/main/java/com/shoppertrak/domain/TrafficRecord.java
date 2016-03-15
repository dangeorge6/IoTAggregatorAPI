package com.shoppertrak.domain;

import java.util.Date;

public class TrafficRecord {
	public int id;
	public int clientId;	
	public  int storeId;	
	public  Date min5_dt;	
	public int enters;
	public int exits;

	public TrafficRecord setId(int id) {
		this.id = id;
		return this;
	}
	public TrafficRecord setClientId(int clientId) {
		this.clientId = clientId;
		return this;
	}
	public TrafficRecord setStoreId(int storeId) {
		this.storeId = storeId;
		return this;
	}
	public TrafficRecord setMin5_dt(Date min5_dt) {
		this.min5_dt = min5_dt;
		return this;
	}
	public TrafficRecord setEnters(int enters) {
		this.enters = enters;
		return this;
	}
	public TrafficRecord setExits(int exits) {
		this.exits = exits;
		return this;
	}

}
