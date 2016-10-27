package com.shoppertrak.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TrafficRecordAggregate {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Date interval;
	private int enters;
	private int exits;
	
	public TrafficRecordAggregate(Date interval, int enters, int exits){
		this.interval = interval;
		this.enters = enters;
		this.exits = exits;
	}
	
	public int getEnters() {
		return enters;
	}
	public void setEnters(int enters) {
		this.enters = enters;
	}
	public Date getInterval() {
		return interval;
	}
	public void setInterval(Date interval) {
		this.interval = interval;
	}
	public int getExits() {
		return exits;
	}
	public void setExits(int exits) {
		this.exits = exits;
	}
	
}
