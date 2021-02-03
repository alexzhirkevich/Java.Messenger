package com.alexz.messenger.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static final int year = new Date().getYear();

    public static String getDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        return sdf.format(date);
    }

    public static String getTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("k:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
