package com.azry.sps.console.client.utils;

import java.util.List;

public class StringUtil {

	public static String join(List<String> list, String del) {

		StringBuilder sb = new StringBuilder();

		String loopDel = "";

		for(String s : list) {

			sb.append(loopDel);
			sb.append(s);

			loopDel = del;
		}

		return sb.toString();
	}

	public static String joinMes(List<String> list, String del) {

		StringBuilder sb = new StringBuilder();

		String loopDel = "";

		for(String s : list) {

			sb.append(loopDel);
			sb.append(Mes.get(s));

			loopDel = del;
		}

		return sb.toString();
	}
}
