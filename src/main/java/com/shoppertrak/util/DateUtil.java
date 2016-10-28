package com.shoppertrak.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.shoppertrak.domain.DateRange;

public class DateUtil {
	public static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}

	public static Date roundUpToNearest(Date dateToRound, int roundTo) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToRound);
		int unroundedMinutes = calendar.get(Calendar.MINUTE);
		int amountToAdd = roundTo - (unroundedMinutes % roundTo);
		calendar.set(Calendar.MINUTE, unroundedMinutes + amountToAdd);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static boolean between(Date date, Date dateStart, Date dateEnd) {
	    if (date != null && dateStart != null && dateEnd != null) {
	        if (date.after(dateStart) && date.before(dateEnd)) {
	            return true;
	        }
	        else {
	            return false;
	        }
	    }
	    return false;
	}
	
	public static List<Date> breakDateRangeIntoIntervals(DateRange dr, int intervalChunk) {
		
		List<Date> returnList = new ArrayList<Date>();
		
		//round start date up to nearest 15 min interval
		Date startDate = DateUtil.roundUpToNearest(dr.getStartDate(),intervalChunk);
		Date endDate = DateUtil.roundUpToNearest(dr.getEndDate(),intervalChunk);
		
		Date currentInterval = startDate;
		
		while(currentInterval.compareTo(endDate)<0){
			returnList.add(currentInterval);
			//add another 15 min
			currentInterval = addMinutesToDate(intervalChunk, currentInterval);
		}
		
		return returnList;
	}
}
