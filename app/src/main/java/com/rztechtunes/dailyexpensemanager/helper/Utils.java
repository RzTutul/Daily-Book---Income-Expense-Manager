package com.rztechtunes.dailyexpensemanager.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDateWithTime() {
        DateFormat dateFormat = new SimpleDateFormat("EEE-dd-hh:mm-aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getMonthName() {
        DateFormat dateFormat = new SimpleDateFormat("MMM");
        Date date = new Date();
        return dateFormat.format(date);
    } public static String getMonthNumber() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }



      public static String getYear() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static String getCurrentDateWithDay() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE.dd.MMMM.yyyy hh:mm-aa");
        Date date = new Date();
        return dateFormat.format(date);
    }





}
