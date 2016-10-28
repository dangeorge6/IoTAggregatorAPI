package com.shoppertrak.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRange {
	
	private Date startDate;
	private Date endDate;
	private String errorMsg;
	private boolean hasError;
	
	public DateRange(String start, String end){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setLenient(false);
        StringBuilder error = new StringBuilder();
        try{
        	startDate = dateFormat.parse(start);
        } catch(ParseException e){//this generic but you can control another types of exception
        	error.append("StartDate is incorrect.  ");
        }
        try{
            endDate = dateFormat.parse(end);
        } catch(ParseException e){//this generic but you can control another types of exception
        	error.append("EndDate is incorrect.  ");
        }
        if(error.length() == 0 && startDate.compareTo(endDate)>0){
        	error.append("StartDate is after EndDate!");
        }
        if(error.length() != 0){
        	hasError = true;
        	errorMsg = error.toString();
        } else {
        	hasError = false;
        	errorMsg = "";
        }
	}
	
	public boolean hasError(){
		return hasError;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
}
