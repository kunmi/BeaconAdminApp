package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class Helpers {
    public static void showDialog(Context activity, String title,String text, String okText,DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(activity);
        build.setTitle(title);
        build.setMessage(text);
        build.setPositiveButton(okText, listener);
        build.show();
    }

    public static void storeUserToken(String token, Application appContext)
    {
        SharedPreferences defaultPreference = PreferenceManager.getDefaultSharedPreferences(appContext);
        defaultPreference.edit().putString(Config.USER_TOKEN, token).apply();
    }

    public static String getUserToken(Application appContext)
    {
        SharedPreferences defaultPreference = PreferenceManager.getDefaultSharedPreferences(appContext);
        return defaultPreference.getString(Config.USER_TOKEN, null);
    }


    public static boolean StoreUserData(JSONObject userData, Application context)
    {
        try {
            String name = userData.getString("name");
            String username = userData.getString("username");
            String email = userData.getString("email");
            boolean isadmin = userData.getBoolean("isadmin");

            SharedPreferences defaultPrefernce = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor pref =  defaultPrefernce.edit();

            pref.putString(Config.NAME, name);
            pref.putString(Config.USERNAME, username);
            pref.putString(Config.USER_EMAIL, email);
            pref.putBoolean(Config.IS_ADMIN, isadmin);


            pref.apply();
            return true;


        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }



}
