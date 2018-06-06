package com.blogspot.kunmii.beaconadmin.network;

import java.io.IOException;

import okhttp3.Response;

public class ServerResponse {

    String jsonBody = null;
    int code;
    boolean exception;
    String reason;

    public ServerResponse(Response response)
    {
        try {
            jsonBody = response.body().string();
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }

        code = response.code();
        exception = !response.isSuccessful();
        reason = response.message();
    }

    public int getCode() {
        return code;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public boolean hasException() {
        return exception;
    }

    public String getReason() {
        return reason;
    }
}
