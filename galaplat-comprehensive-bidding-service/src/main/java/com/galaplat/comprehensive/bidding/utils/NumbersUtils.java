package com.galaplat.comprehensive.bidding.utils;

public class NumbersUtils {
	public static boolean isNumeric(String str){
		String reg = "^[+-]?\\d+(\\.\\d+)?$";
		return str.matches(reg);
	}
	
	public static String getRound(String str){
		if(!NumbersUtils.isNumeric(str)) {
			return str;
		}else {
			double number = Double.valueOf(str);
			long n = (long) number;
			return n+"";
		}
	}
	
	public static boolean isNumberOrWord(String str){
		String reg = "^[A-Za-z0-9]+$";
		return str.matches(reg);
	}

}
