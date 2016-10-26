package com.shoppertrak.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class TrafficRecordSet {

	private int clientId;
	private int storeId;
	private Collection<TrafficRecord> recordSet;
	private String errorMsg;
	private boolean hasError;
	
	public TrafficRecordSet(Integer clientId, Integer storeId, Collection<TrafficRecord> recordSet, String errorMsg){
		
		this.clientId = clientId;
		this.storeId = storeId;
        this.recordSet = recordSet;
        this.errorMsg = errorMsg;
        this.hasError = false;
        if(errorMsg.length() != 0){
        	this.hasError = true;
        }
      
	}
	

    @JsonInclude(JsonInclude.Include.NON_NULL)
    
	public Collection<TrafficRecord> getRecordSet() {
		return recordSet;
	}
	public void setRecordSet(Collection<TrafficRecord> recordSet) {
		this.recordSet = recordSet;
	}
	
    @JsonIgnore
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
    @JsonIgnore
	public boolean hasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}


	public int getClientId() {
		return clientId;
	}


	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	//only include this if it's not null (will appear when querying for specific store
    @JsonInclude(JsonInclude.Include.NON_NULL) 
	public int getStoreId() {
		return storeId;
	}


	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	

}
