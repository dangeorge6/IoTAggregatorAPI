package com.shoppertrak.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.shoppertrak.domain.TrafficRecord;

@Repository
public class TrafficDao {
	
	private Map<Integer, TrafficRecord> data = new HashMap<Integer, TrafficRecord>();
	/*
	I decided to put these lookups of rows by client and store because they are similar to database indexing by column
	and provide constant time lookup for common queries about particular clients and stores. I will perform some of the 
	more specific data massaging/aggregating further up the chain (aggregating entrance tallies, etc) and keep it broad while working in this DAO.
	These lookup hashes are kept current even when new TrafficRecords are added or deleted. 
	*/
	private Map<Integer, List<TrafficRecord>> clientLookup;
	private Map<Integer, List<TrafficRecord>> storeLookup;
	
	private int counter = 1;
	
	public TrafficDao() {
		initData();
		initLookups();
	}
	
	private void initLookups() {
		createClientLookup();
		createStoreLookup();
	}

	private void createClientLookup() {
		clientLookup = new HashMap<Integer, List<TrafficRecord>>();
		for(Map.Entry<Integer,TrafficRecord> entry: data.entrySet()){
			TrafficRecord value = entry.getValue();
			if(clientLookup.containsKey(value.clientId)){
				clientLookup.get(value.clientId).add(value);
			} else {
				//no rows for this client yet initialize list
				List<TrafficRecord> l = new ArrayList<TrafficRecord>();
				l.add(value);
				clientLookup.put(value.clientId, l);
			}
		}	
		
		//data HashMap is not guaranteed to be ordered by key, so the Traffic Record Lists are not necessarily ordered by startDate.
		//my aggregations algorithm depends on ordering so I'll order here.
		orderMapListsByStartDate(clientLookup);
		
	}
	

	private void createStoreLookup() {
		//will refactor this and above method if I have time using reflection
		storeLookup = new HashMap<Integer, List<TrafficRecord>>();
		for(Map.Entry<Integer,TrafficRecord> entry: data.entrySet()){
			TrafficRecord value = entry.getValue();
			if(storeLookup.containsKey(value.storeId)){
				storeLookup.get(value.storeId).add(value);
			} else {
				//no rows for this client yet initialize list
				List<TrafficRecord> l = new ArrayList<TrafficRecord>();
				l.add(value);
				storeLookup.put(value.storeId, l);
			}
		}
		
		orderMapListsByStartDate(storeLookup);
	}
	
	private void saveToClientLookup(TrafficRecord r) {
		//need to keep lookups updated when records are added and updated
		//refactor with below if you have time
		if(clientLookup != null){
			//only perform when clientLookup is initialized (not when data is first loaded. 
			//This function is only for one-off adds/updates. Don't want to sort after every insert on the initial data load.
			List<TrafficRecord> l;
			boolean isAdd = true;
			if(clientLookup.containsKey(r.clientId)){
				l = clientLookup.get(r.clientId);
				
				for (int i = 0; i < l.size(); i++){
					if(l.get(i).id == r.id){
						//this is an update, replace element
						l.set(i,r);
						isAdd = false;
						break;
					}
				}
				//this is an add, add it to list
				if(isAdd) l.add(r);
				
			} else {
				//no rows for this client yet initialize list
				l = new ArrayList<TrafficRecord>();
				l.add(r);
				clientLookup.put(r.clientId, l);
			}
			//keep the list in order
			Collections.sort(l, new Comparator<TrafficRecord>(){
				@Override
				public int compare(TrafficRecord a, TrafficRecord b){
					return a.min5_dt.compareTo(b.min5_dt);
				}
			});
		}
	}
		
	private void saveToStoreLookup(TrafficRecord r) {
		List<TrafficRecord> l;
		boolean isAdd = true;
		if(storeLookup != null){
			if(storeLookup.containsKey(r.storeId)){
				l = storeLookup.get(r.storeId);
				
				for (int i = 0; i < l.size(); i++){
					if(l.get(i).id == r.id){
						//this is an update, replace element
						l.set(i,r);
						isAdd = false;
						break;
					}
				}
				//this is an add, add it to list
				if(isAdd) l.add(r);
				
			} else {
				//no rows for this client yet initialize list
				l = new ArrayList<TrafficRecord>();
				l.add(r);
				storeLookup.put(r.storeId, l);
			}
			//keep the list in order
			Collections.sort(l, new Comparator<TrafficRecord>(){
				@Override
				public int compare(TrafficRecord a, TrafficRecord b){
					return a.min5_dt.compareTo(b.min5_dt);
				}
			});
		}
		
	}
	
	private void deleteFromStoreLookup(int id, int storeId) {
		List<TrafficRecord> l = storeLookup.get(storeId);
		if(l != null){
			for (int i = 0; i < l.size(); i++){
				if(l.get(i).id == id){
					//this is an update, replace element
					l.remove(i);
					break;
				}
			}
			//keep the list in order
			Collections.sort(l, new Comparator<TrafficRecord>(){
				@Override
				public int compare(TrafficRecord a, TrafficRecord b){
					return a.min5_dt.compareTo(b.min5_dt);
				}
			});			
		}
	}

	private void deleteFromClientLookup(int id, int clientId) {
		// TODO Auto-generated method stub
		List<TrafficRecord> l = clientLookup.get(clientId);
		if(l != null){
			for (int i = 0; i < l.size(); i++){
				if(l.get(i).id == id){
					//this is an update, replace element
					l.remove(i);
					break;
				}
			}
			//keep the list in order
			Collections.sort(l, new Comparator<TrafficRecord>(){
				@Override
				public int compare(TrafficRecord a, TrafficRecord b){
					return a.min5_dt.compareTo(b.min5_dt);
				}
			});			
		}
	}
	
	private void orderMapListsByStartDate(Map<Integer, List<TrafficRecord>> mapToOrder) {
		for(Map.Entry<Integer,List<TrafficRecord>> entry: mapToOrder.entrySet()){
			List<TrafficRecord> value = entry.getValue();
			Collections.sort(value, new Comparator<TrafficRecord>(){
					@Override
					public int compare(TrafficRecord a, TrafficRecord b){
						return a.min5_dt.compareTo(b.min5_dt);
					}
			});
		}
	}

	public List<TrafficRecord> getByClientId(int id) {
		return clientLookup.get(id);
	}
	
	public List<TrafficRecord> getByStoreId(int id) {
		return storeLookup.get(id);
	}
	
	public TrafficRecord get(int id) {
		return data.get(id);
	}

	public void save(TrafficRecord r) {
		data.put(r.id, r);
		//need to keep the indexes updated as new data comes in
		saveToClientLookup(r);
		saveToStoreLookup(r);
	}
	
	

	public Collection<TrafficRecord> getAll() {
		return data.values();
	}
	
	public void delete(int id) {
		TrafficRecord tr = data.get(id);
		int clientId = tr.clientId;
		int storeId = tr.storeId;
		data.remove(id);
		//keep the indexes up to date as items are deleted
		deleteFromClientLookup(id, clientId);
		deleteFromStoreLookup(id, storeId);
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
