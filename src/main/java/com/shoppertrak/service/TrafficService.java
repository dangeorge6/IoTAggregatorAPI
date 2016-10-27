package com.shoppertrak.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppertrak.dao.TrafficDao;
import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.domain.TrafficRecordSet;

@Service
public class TrafficService {
	
	TrafficDao dao;
	 
	@Autowired
	public TrafficService(TrafficDao dao) {
		this.dao = dao;
	}
	
	public TrafficRecord get(int id) {
		return dao.get(id);
	}

	public void save(TrafficRecord r) {
		dao.save(r);
	}

	public Collection<TrafficRecord> getAll() {
		return dao.getAll();
	}

	public void delete(int id) {
		dao.delete(id);
	}

	public TrafficRecordSet getTrafficByClient(int clientId, String startTime, String endTime) {
		String errorMsg = null;
		Collection<TrafficRecord> returnList = null;
		Collection<TrafficRecord> clientRecords = dao.getByClientId(clientId);
		if(clientRecords == null || clientRecords.isEmpty()){
			errorMsg = "Client Doesn't Exist!";
		} else {
			//client records are already ordered by startdate in DAO.
			//I thought about narrowing the client records between start and end date first, then aggregating, 
			//but I think I'd rather do it all in a single pass through the list to improve runtime.
			returnList = get15MinAggregatesBetweenDates(clientRecords, startTime, endTime);
		}
		return new TrafficRecordSet(clientId, null, returnList, errorMsg);
	}

	private Collection<TrafficRecord> get15MinAggregatesBetweenDates(Collection<TrafficRecord> records, String startTime, String endTime) {
		// TODO Auto-generated method stub
		return records;
	}

	public TrafficRecordSet getTrafficByClientForStore(int clientId, int storeId, String startTime, String endTime) {
		String errorMsg = null;
		Collection<TrafficRecord> returnList = null;
		Collection<TrafficRecord> clientRecords = dao.getByClientId(clientId);
		if(clientRecords == null || clientRecords.isEmpty()){
			errorMsg = "Client Doesn't Exist!";
		} else {
			Collection<TrafficRecord> storeRecords = dao.getByStoreId(storeId);
			if(storeRecords == null || storeRecords.isEmpty()){
				errorMsg = "Store Doesn't Exist!";
			} else {
				//store and client exists
				returnList = get15MinAggregatesBetweenDates(clientRecords, startTime, endTime);
			}			
		}
		return new TrafficRecordSet(clientId, null, returnList, errorMsg);
	}
}
