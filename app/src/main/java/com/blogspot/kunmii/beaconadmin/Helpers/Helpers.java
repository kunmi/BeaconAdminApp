package com.blogspot.kunmii.beaconadmin.Helpers;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.constraint.solver.widgets.Helper;
import android.support.v7.app.AlertDialog;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.network.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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

    public static void showDialog(Context activity, String title,String text)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(activity);
        build.setTitle(title);
        build.setMessage(text);
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


    public static void clearALlData(Application application)
    {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        editor.clear();
        editor.apply();
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


    public static ServerRequest craftProjectRetrieveRequest(Application application)
    {
        String token = Helpers.getUserToken(application);
        ServerRequest request = new ServerRequest(application, Config.PROJECT_URL);
        request.putHeader("Authorization", token);
        request.putHeader("Content-Type", "application/json");
        return request;
    }

    public static ServerRequest craftUploadRequest(Application application, List<Beacon> beacons, String projectId, String floorplanId)
    {
        String token = Helpers.getUserToken(application);
        ServerRequest request = new ServerRequest(application, Config.UPLOAD_URL + "/" + projectId + "/" + floorplanId);
        request.putHeader("Authorization", token);
        request.putHeader("Content-Type", "application/json");

        JSONArray array = new JSONArray();

        try {
            for(Beacon b: beacons)
            {
                JSONObject jsonObject = new JSONObject(b.getBeaconData());
                array.put(jsonObject);
            }

        }
        catch (JSONException exp)
        {
            exp.printStackTrace();

        }
        request.setBody(array.toString());

        return request;
    }

    public static ServerRequest craftBeaconUpdateRequest(Application application, Beacon beacon)
    {
        String token = Helpers.getUserToken(application);

        ServerRequest request = new ServerRequest(application, Config.BEACON_UPDATE_URL + "/" +
                beacon.getProjectId() + "/" +
                beacon.getFloorPlanId() + "/" +
                beacon.getObjectId());

        request.putHeader("Authorization", token);
        request.putHeader("Content-Type", "application/json");

        request.setBody(beacon.getBeaconData());

        return request;
    }

    public static ServerRequest craftSendMessageRequest(Application application, String project, String floorplan,String data)
    {

        String token = Helpers.getUserToken(application);

        ServerRequest request = new ServerRequest(application, Config.SEND_MESSAGE_URL + "/" +

                project + "/" +
                floorplan
        );


        request.putHeader("Authorization", token);
        request.putHeader("Content-Type", "application/json");

        request.setBody(data);

        return request;
    }

    public static JSONObject createIBeaconJSON (Application application){
        JSONObject jsonObject = null;

        try {
            StringBuilder buf=new StringBuilder();
            InputStream json = application.getAssets().open("ibeacon.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            jsonObject = new JSONObject(buf.toString());
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }
        catch (JSONException exp)
        {
            exp.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject createEddystoneJson (Application application){
        JSONObject jsonObject = null;

        try {
            StringBuilder buf=new StringBuilder();
            InputStream json = application.getAssets().open("eddystone.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            jsonObject = new JSONObject(buf.toString());
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }
        catch (JSONException exp)
        {
            exp.printStackTrace();
        }

        return jsonObject;
    }


}
