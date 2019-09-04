package com.lzkj.mobile.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static boolean isEmpty(String s) {
		return s == null || s.equals("");
	}

	public static String getVerificationCode(int len) {
		String vCode = "";
  		Random ran = new Random();
  		for(int i = 0; i < len; i++) {
  			vCode += ran.nextInt(10);
  		}
  		return vCode;
	}

	public static boolean isMobileNumber(String s) {
		 String regex = "^((13[0-9])|(14[0-9])|(15([0-9]))|(17[0-9])|(18[0-9]))\\d{8}$";
		 Pattern p = Pattern.compile(regex);
		 Matcher m = p.matcher(s);
		 return m.matches();
	}

	public static String number2Ip(long number) {
    	long l = number >> 24 & 0xFF;
    	long m = number >> 16 & 0xFF;
    	long n = number >> 8 & 0xFF;
    	long o = number & 0xFF;
    	return l + "." + m + "."  +  n + "." +  o;
    }

    public static long ip2Number(String ip) {
    	String[] ipSplit = ip.split("\\.");

    	return Long.parseLong(ipSplit[0]) * (int)Math.pow(256, 3) +
    			Long.parseLong(ipSplit[1]) * (int)Math.pow(256, 2) +
    			Long.parseLong(ipSplit[2]) * 256 +
    			Long.parseLong(ipSplit[3]);
    }

    public static String delBom(String s) {
    	char[] bomChar = s.toCharArray();
    	if(bomChar[0] != 65279) {
    		return s;
    	}
    	char[] noneBomchar = new char[bomChar.length - 1];
    	for (int j = 0; j < noneBomchar .length; j++) {
    	noneBomchar [j] = bomChar[j + 1];
    	}
    	String first = String.valueOf(noneBomchar );
    	return first;
    }
}
