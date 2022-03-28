package edu.wj.sport.android.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {


    public static String formatMill(long duration){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(duration);
    }

    public static String formatDate(long duration){

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(duration - TimeZone.getDefault().getRawOffset());
    }


    public static int diffDayNumber(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int lastDay = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        return currentDay - lastDay;
    }


    public static String formatNumber(String value){
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.toString();
    }
}
