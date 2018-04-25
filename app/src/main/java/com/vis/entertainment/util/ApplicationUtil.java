package com.vis.entertainment.util;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vis.entertainment.constants.ApplicationConstants;
import com.vis.entertainment.models.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ApplicationUtil {

    public static String getDateFromEpoch(String epochDate){
        Date date=new Date(Long.parseLong(epochDate)*1000);
        DateFormat format = new SimpleDateFormat(ApplicationConstants.DATE_PATTERN);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    public static List<Result> retrieveFromSharedPref(SharedPreferences sharedPreferences) {
        List<Result> resultList = new ArrayList<>();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ApplicationConstants.FAV_LIST_KEY, ApplicationConstants.BLANK);
        if (json != ApplicationConstants.BLANK) {
            resultList = gson.fromJson(json,new TypeToken<List<Result>>(){}.getType());
        }
        return resultList;
    }

    public static void saveToSharedPref(SharedPreferences sharedPreferences,List<Result> resultList) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(resultList);
        editor.putString(ApplicationConstants.FAV_LIST_KEY,json);
        editor.commit();
    }

    public static boolean isFavorite(String placeId, List<Result> favList) {
        for(Result fav:favList){
            if(placeId.equals(fav.getPlaceId()))
                return true;
        }
        return false;
    }
}
