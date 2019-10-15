package com.lzkj.mobile.util;

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
}
