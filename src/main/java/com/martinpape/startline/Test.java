package com.martinpape.startline;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class Test {

	public static String getCountry(String input) {
		String country = "";
		if (!StringUtils.isEmpty(input)) {
			for (String key : MySqlToExcel.COUNTRY_TO_CODE.keySet()) {
				String code = MySqlToExcel.COUNTRY_TO_CODE.get(key);
				if (code.equals(input)) {
					country = key;
					break;
				}
			}
		}
		return country;
	}
	
	public static void main(String[] args) {
		String country = getCountry("aus");
		System.out.println(country);

	}
}
