package com.uek335.done.activity.helper;

import android.icu.text.SimpleDateFormat;

public abstract class DatePickerUtil {
    /* dateformat */
    private static String dateFormat = "dd/MM/yy";
    private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    public static SimpleDateFormat getSdf() {
        return sdf;
    }
}
