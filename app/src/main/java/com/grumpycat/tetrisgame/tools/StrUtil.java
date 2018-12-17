package com.grumpycat.tetrisgame.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cc.he on 2018/11/14
 */
public class StrUtil {
    private static DateFormat YYMMDDHHMM = new SimpleDateFormat("yyyy:MM:dd HH:mm");
    public static String formatYYMMDDHHMM(long time) {
        Date date = new Date(time);
        return YYMMDDHHMM.format(date);
    }
}
