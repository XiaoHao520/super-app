package com.school.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XIAOHAO-PC on 2017-11-06.
 */

public class Time {
    public static String getTime(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        long nowDate = calendar.getTime().getTime(); //Date.getTime() 获得毫秒型日期
        try {
            long specialDate = sdf.parse(dateString).getTime();

            long betweenDate = (nowDate - specialDate) / (1000 * 60 * 60 * 24); //计算间隔多少天，则除以毫秒到天的转换公式
            if (betweenDate == 0) {
                long hour = ((nowDate - specialDate) % (1000 * 60 * 60 * 24)) / (60 * 60 * 1000);
                if (hour == 0) {
                    Date date = new Date();
                    int hours = date.getHours();
                    int min = date.getMinutes();
                    return hours + " : " + min;
                }
                return String.valueOf(hour) + "小时前";
            }
            return String.valueOf(betweenDate) + "小时前";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date parse(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.parse(date);
    }

    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
