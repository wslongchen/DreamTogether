package com.example.mrpan.dreamtogether.utils;


import java.text.ParseException;
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


    public static String getShortSpellMonth(Date date){
        String[] monthSpells={"Jan.","Feb.","Mar.","Apr.","May.","Jun.","Jul.","Aug.","Sep.","Oct.","Nov.","Dec."};
        return monthSpells[date.getMonth()];
    }

    public static String getCurrentTimeStr(){
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mDate=dateFormat.format(date);
        return mDate;
    }

    public static String getCustomStr(Date date,String format){
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getAutoTimeStr(String d){
        Date date=StrToDate(d);
        SimpleDateFormat dateFormat=null;
        String str="";
        Date currentDate=new Date(System.currentTimeMillis());
        if(date!=null){
            long diff=currentDate.getTime()-date.getTime();
            int dayDiff=(int)diff/(1000*60*60*24);
            if(dayDiff<0){
                str="未来";
                return str;
            }
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
            int w = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            switch (dayDiff){
                case 0:
                    dateFormat=new SimpleDateFormat("HH:mm");
//                int hourDiff=currentDate.getHours()-date.getHours();
                    str=dateFormat.format(date);
                    break;
                case 1:
                    str="昨天";
                    break;
                case 2:
                    str=weekDays[w];
                    break;
                case 3:
                    str=weekDays[w];
                    break;
                case 4:
                    str=weekDays[w];
                    break;
                case 5:
                    str=weekDays[w];
                    break;
                case 6:
                    str=weekDays[w];
                    break;
                default:
                    dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    str=dateFormat.format(date);
                    break;
            }
        }


        return str;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
