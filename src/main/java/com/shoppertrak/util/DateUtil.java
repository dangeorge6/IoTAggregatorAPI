package com.shoppertrak.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}

	public static Date roundUpNearest15(Date dateToRound) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToRound);
		int unroundedMinutes = calendar.get(Calendar.MINUTE);
		int mod = unroundedMinutes % 15;
		calendar.set(Calendar.MINUTE, unroundedMinutes + mod);
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
}
