package com.lzkj.mobile.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    public static String format(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date old = simpleDateFormat.parse(str);
        String newDate = simpleDateFormat.format(old);
        return newDate;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Long  GetNowDate() {
        long current = System.currentTimeMillis();
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        return zero;
    }

    public static String getNow() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(date);
    }

    public static String getInitial(){
        long zero =System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24) -TimeZone.getDefault().getRawOffset();
        return new Timestamp(zero).toString();
    }

    public static String getYesterday(){
        long zero=System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset() - 24*3600*1000;
        String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zero);
        return yesterday;
    }

    // 获取本周开始时间
    public static String startWeek() {
        Date date =new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 使用Calendar类进行时间的计算
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会计算到下一周去。
        // dayWeek值（1、2、3...）对应周日，周一，周二...
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 7;
        } else {
            dayWeek -= 1;
        }
        // 计算本周开始的时间
        cal.add(Calendar.DAY_OF_MONTH, 1 - dayWeek);
        Date startDate = cal.getTime();
        return sdf.format(startDate)+" 00:00:00";
    }

    // 获取本月的开始时间
    public static String startMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date =new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 设置为1号,当前日期既为本月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        return sdf.format(startDate)+" 00:00:00";
    }

}
