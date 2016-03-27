package com.example.mrpan.dreamtogether.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mrpan on 16/3/21.
 */
public class DateUtils {
    /**
     * 获取当前时间的字符串
     *
     * @return xxxx年xx月xx日 星期x
     */
    public static String getCurrentDateStr() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String mDate = c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1)
                + "月" + c.get(Calendar.DATE) + "日  " + weekDays[w];
        return mDate;
    }


    public static String getCurrentTimeStr(){
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDate=dateFormat.format(date);
        return mDate;
    }
}
