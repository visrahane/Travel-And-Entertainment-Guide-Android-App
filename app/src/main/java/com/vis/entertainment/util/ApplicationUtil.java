package com.vis.entertainment.util;

import com.vis.entertainment.constants.ApplicationConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ApplicationUtil {

    public static String getDateFromEpoch(String epochDate){
        Date date=new Date(Long.parseLong(epochDate)*1000);
        DateFormat format = new SimpleDateFormat(ApplicationConstants.DATE_PATTERN);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }
}
