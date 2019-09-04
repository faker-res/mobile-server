package com.lzkj.mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String format(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date old = simpleDateFormat.parse(str);
        String newDate = simpleDateFormat.format(old);
        return newDate;
    }
}
