package com.blogspot.kunmii.beaconadmin.network;

import okhttp3.Response;

public class ServerResponse {

    String jsonBody;
    int code;
    boolean exception;
    String reason;


    public ServerResponse(Response response)
    {

    }
}
