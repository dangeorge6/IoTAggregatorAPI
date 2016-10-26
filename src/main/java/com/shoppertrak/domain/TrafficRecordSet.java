package com.shoppertrak.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class TrafficRecordSet {

	private Collection<TrafficRecord> recordSet;
	private String errorMsg;
	private boolean hasError;
	
	public TrafficRecordSet(Collection<TrafficRecord> recordSet, String errorMsg){
		
        this.recordSet = recordSet;
        this.errorMsg = errorMsg;
        this.hasError = false;
        if(errorMsg.length() != 0){
        	this.hasError = true;
        }
      
	}
	
	public Collection<TrafficRecord> getRecordSet() {
		return recordSet;
	}
	public void setRecordSet(Collection<TrafficRecord> recordSet) {
		this.recordSet = recordSet;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public boolean hasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	

}
