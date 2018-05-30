package com.blogspot.kunmii.beaconadmin.network;

import android.app.Application;

import com.blogspot.kunmii.beaconadmin.Config;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerRequest {
    Application application;

    //Json body
    RequestBody body;
    String path;
    OkHttpClient client;

    HashMap<String, String> headers = new HashMap<>();

    public ServerRequest(Application application, String path){
        this.application = application;
        this.path = Config.SERVER_URL + path;
        client = new OkHttpClient();
    }

    public void setBody(String jsonBody)
    {
        body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    }

    public void putHeader(String header, String value)
    {
        headers.put(header, value);
    }

    public void execute(){
        Request.Builder request = new Request.Builder()
                .url(path);

        for(String key : headers.keySet())
        {
            request.header(key, headers.get(key));
        }

        request.post(body);

        try {
            Response response = client.newCall(request.build()).execute();
            String val = response.body().string();



        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }




}
