package com.shoppertrak.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppertrak.dao.TrafficDao;
import com.shoppertrak.domain.DateRange;
import com.shoppertrak.domain.TrafficRecord;
import com.shoppertrak.domain.TrafficRecordAggregate;
import com.shoppertrak.domain.TrafficRecordSet;
import com.shoppertrak.util.DateUtil;

@Service
public class TrafficService {
	
	TrafficDao dao;
	private Logger logger = Logger.getLogger(this.getClass());
	 
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

	public TrafficRecordSet getTrafficByClient(int clientId, DateRange dr) {
		String errorMsg = null;
		Collection<TrafficRecordAggregate> returnList = null;
		List<TrafficRecord> clientRecords = dao.getByClientId(clientId);
		
//		if(clientRecords == null || clientRecords.isEmpty()){
//			errorMsg = "Client Doesn't Exist!";
//		} else {
		
			//client records are already ordered by startdate in DAO.
			//I thought about narrowing the client records between start and end date first, then aggregating, 
			//but I think I'd rather do it all in a single pass through the list to improve runtime.
			returnList = get15MinAggregatesBetweenDates(clientRecords, dr);
			
//		}
		return new TrafficRecordSet(clientId, null, returnList, errorMsg);
	}

	public TrafficRecordSet getTrafficByClientForStore(int clientId, int storeId, DateRange dr) {
		String errorMsg = null;
		Collection<TrafficRecordAggregate> returnList = null;
		List<TrafficRecord> clientRecords = dao.getByClientId(clientId);
		
//		if(clientRecords == null || clientRecords.isEmpty()){
//			errorMsg = "Client Doesn't Exist!";
//		} else {
		
			List<TrafficRecord> storeRecords = clientRecords == null? null:dao.getByStoreId(storeId);
			
//			if(storeRecords == null || storeRecords.isEmpty()){
//				errorMsg = "Store Doesn't Exist!";
//			} else {
				//store and client exists
				returnList = get15MinAggregatesBetweenDates(storeRecords, dr);
//			}			
//		}
		return new TrafficRecordSet(clientId, null, returnList, errorMsg);
	}
	
	
	private Collection<TrafficRecordAggregate> get15MinAggregatesBetweenDates(List<TrafficRecord> records, DateRange dr) {
		Date startDate = dr.getStartDate();
		Date endDate = dr.getEndDate();
		List<TrafficRecordAggregate> returnList = new ArrayList<TrafficRecordAggregate>();
		//NOTE: this algorithm depends on the records being sorted by date, this is done in the TrafficDAO
		
		//loop to first row where startDate between start and end
		//this could be a modified binarysearch but just going linear for now, may return if time
		int recordPointer = 0;
		while(records != null 
				&& recordPointer < records.size() 
				&& records.get(recordPointer).min5_dt.compareTo(startDate)<0){
			recordPointer++;
		}
		
		//breakup start and end into list of ranges
		List<Date> intervals = DateUtil.breakDateRangeIntoIntervals(dr,15); 
		
		logger.info("intervals are " + intervals);
		
		for(Date d: intervals){
	
			int enters = 0;
			int exits = 0;	
			
			logger.info("Fetching for interval " + d);
			//loop through records and increment while record is less or equal to interval
			while(records !=null
					&& recordPointer < records.size() 
					&& records.get(recordPointer).min5_dt.compareTo(d)<=0){
				
				logger.info("processing record " + records.get(recordPointer));
				enters+=records.get(recordPointer).enters;
				exits+=records.get(recordPointer).exits;
				recordPointer++;
			}
			
			//add interval to the List
			TrafficRecordAggregate intervalObj = new TrafficRecordAggregate(d,enters,exits); 
			returnList.add(intervalObj);
		}
		
		return returnList;
	}

	

}
