package com.azry.sps.fi;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateAdapter {

	public static Date parseDate(String date) {
		return DatatypeConverter.parseDate(date).getTime();
	}

	public static String printDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return DatatypeConverter.printDate(cal);
	}
}
