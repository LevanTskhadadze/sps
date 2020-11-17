package com.azry.sps.console.client.utils;

import com.google.gwt.i18n.client.NumberFormat;

import java.math.BigDecimal;

public class NumberFormatUtils {

	public final static NumberFormat AMOUNT_NUMBER_FORMAT = NumberFormat.getFormat("#,##0.00");

	public static String format(Number number) {
		if (number == null) {
			number = BigDecimal.ZERO;
		}
		return AMOUNT_NUMBER_FORMAT.format(number);
	}

	public static boolean equalsWithFormat(BigDecimal a, BigDecimal b) {
		return format(a).equals(format(b));
	}
}