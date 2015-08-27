package com.blackboxstudios.rafael.imgurexplorer.objects;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.blackboxstudios.rafael.imgurexplorer.R;


public class Utils {

    public static String getSortedBy(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.sort_list_key),
                context.getString(R.string.sort_list_default_value));
    }

    public static String getSection(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.section_list_key),
                context.getString(R.string.section_list_default_value));
    }

    public static String getWindow(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.window_list_key),
                context.getString(R.string.window_list_default_value));
    }

    public static String getLayout(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.layout_list_key),
                context.getString(R.string.layout_list_default_value));
    }

    public static Boolean showViral(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.viral_checkbox_key),
                true);
    }
}
