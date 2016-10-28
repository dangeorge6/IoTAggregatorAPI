package com.shoppertrak;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shoppertrak.domain.TrafficRecord;

public class TestHelper {
	
	public static Date parseDate(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date theDate = null;
        try {
			theDate = dateFormat.parse(date);
		} catch (ParseException e) {
			
		}
        return theDate;
	}
	public static List<TrafficRecord> getSampleData(){
		List<TrafficRecord> l = new ArrayList<TrafficRecord>();
		l.add(generateRecord(1,300,3000,"201603010005", 6, 11));
		l.add(generateRecord(2,300,3000,"201603010010",0,0));
		l.add(generateRecord(3,300,3000,"201603010015",2,1));
		l.add(generateRecord(4,300,3000,"201603010020",4,2));
		l.add(generateRecord(5,300,3000,"201603010025",3,5));
		l.add(generateRecord(6,300,3000,"201603010030",10,2));
		l.add(generateRecord(7,300,3000,"201603010035",2,5));
		l.add(generateRecord(8,300,3000,"201603010040",7,12));
		l.add(generateRecord(9,300,3000,"201603010045",4,0));
		l.add(generateRecord(10,300,3000,"201603010050",0,3));
		l.add(generateRecord(11,300,3000,"201603010055",2,1));
		l.add(generateRecord(12,300,3000,"201603010100",0,0));
		
		l.add(generateRecord(13,300,3001,"201603010010",2,3));
		l.add(generateRecord(14,300,3001,"201603010015",2,1));
		l.add(generateRecord(15,300,3001,"201603010020",4,2));
		l.add(generateRecord(16,300,3001,"201603010025",3,5));
		l.add(generateRecord(17,300,3001,"201603010030",6,2));
		l.add(generateRecord(18,300,3001,"201603010050",25,3));
		l.add(generateRecord(19,300,3001,"201603010055",2,1));
		l.add(generateRecord(20,300,3001,"201603010100",0,5));
		l.add(generateRecord(21,300,3001,"201603010105",6,11));
		return l;
	}
	
	
	private static TrafficRecord generateRecord(int id, int clientId, int storeId, String interval, int enters, int exits) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date dt = null;
		try {
			dt = df.parse(interval);
		} catch (ParseException e) {}
		TrafficRecord r = new TrafficRecord();
		return r.setId(id).setClientId(clientId).setStoreId(storeId).setEnters(enters).setExits(exits).setMin5_dt(dt);
			
	}
}
