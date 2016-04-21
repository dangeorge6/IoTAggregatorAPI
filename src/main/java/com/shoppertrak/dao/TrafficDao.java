package com.shoppertrak.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.shoppertrak.domain.TrafficRecord;

@Repository
public class TrafficDao {
	
	private Map<Integer, TrafficRecord> data = new HashMap<Integer, TrafficRecord>();
	private int counter = 1;
	
	public TrafficDao() {
		initData();
	}
	
	public TrafficRecord get(int id) {
		return data.get(id);
	}

	public void save(TrafficRecord r) {
		data.put(r.id, r);
	}
	
	public Collection<TrafficRecord> getAll() {
		return data.values();
	}
	
	public void delete(int id) {
		data.remove(id);
	}
	
	private void initData() {
		addRec(100,1001,"201603010005", 6, 11);
		addRec(100,1001,"201603010010",0,0);
		addRec(100,1001,"201603010015",2,1);
		addRec(100,1001,"201603010020",4,2);
		addRec(100,1001,"201603010025",3,5);
		addRec(100,1001,"201603010030",10,2);
		addRec(100,1001,"201603010035",2,5);
		addRec(100,1001,"201603010040",7,12);
		addRec(100,1001,"201603010045",4,0);
		addRec(100,1001,"201603010050",0,3);
		addRec(100,1001,"201603010055",2,1);
		addRec(100,1001,"201603010100",0,0);
						
		addRec(100,1002,"201603010010",2,3);
		addRec(100,1002,"201603010015",2,1);
		addRec(100,1002,"201603010020",4,2);
		addRec(100,1002,"201603010025",3,5);
		addRec(100,1002,"201603010030",6,2);
		addRec(100,1002,"201603010050",25,3);
		addRec(100,1002,"201603010055",2,1);
		addRec(100,1002,"201603010100",0,5);
        addRec(100,1002,"201603010105",6,11);
	}
	
	private void addRec(int clientId, int storeId, String interval, int enters, int exits) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date dt = null;
		try {
			dt = df.parse(interval);
		} catch (ParseException e) {}
		TrafficRecord r = new TrafficRecord();
		r.setId(counter++).setClientId(clientId).setStoreId(storeId).setEnters(enters).setExits(exits).setMin5_dt(dt);
		save(r);	
	}
}
