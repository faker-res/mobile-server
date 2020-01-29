package com.lzkj.mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTransUtil {
    /**
     * 字符转日期
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr,String pattern){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符转日期 默认格式
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr){
        return strToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 日期转字符
     */
    public static String dateToStr(Date date,String pattern){
        String dateStr = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
            dateStr = sdf1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 日期转字符 默认格式
     */
    public static String dateToStr(Date date){
        if(date != null){
            return dateToStr(date,"yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

}
