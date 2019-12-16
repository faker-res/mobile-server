package com.lzkj.mobile.util;


import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PayUtil {

    public static String GetOrderIDByPrefix(String prefix,Integer userId) {
//        int num = 32;
//        int num2 = 6;
        String stringBuffer = new String();
        stringBuffer += prefix;
        stringBuffer += GetDateTimeLongString();
//        if (stringBuffer.length() + num2 > num) {
//            num2 = num - stringBuffer.length();
//        }
        stringBuffer +=userId.toString();
       // stringBuffer += CreateRandom(num2, 1, 0, 0, 0, "");
        return stringBuffer;
    }


    public static String GetDateTimeLongString() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String temp_str = sdf.format(dt);
        return temp_str;
    }

    public static String CreateRandom(int length, int useNum, int useLow, int useUpp, int useSpe, String custom) {
        byte[] array = new byte[4];
        new SecureRandom().setSeed(array);
        Random arandom = new Random(toInt32_2(array, 0));

        String text = "";
        String text2 = custom;
        if (useNum == 1) {
            text2 += "0123456789";
        }
        if (useLow == 1) {
            text2 += "abcdefghijklmnopqrstuvwxyz";
        }
        if (useUpp == 1) {
            text2 += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (useSpe == 1) {
            text2 += "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        }
        for (int i = 0; i < length; i++) {
            text += arandom.nextInt(text2.length() - 1);
        }
        return text;
    }


    public static int toInt32_2(byte[] bytes, int index) {
        int a = (int) ((int) (0xff & bytes[index]) << 32 | (int) (0xff & bytes[index + 1]) << 40 | (int) (0xff & bytes[index + 2]) << 48 | (int) (0xff & bytes[index + 3]) << 56);
        return a;
    }


    public static void main(String[] args) {
        System.out.println("GetDateTimeLongString:" + GetDateTimeLongString());
       // System.out.println("encrypData:" + GetOrderIDByPrefix("e"));
    }

}

