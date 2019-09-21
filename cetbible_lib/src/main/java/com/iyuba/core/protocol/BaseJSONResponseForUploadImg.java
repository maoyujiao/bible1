package com.iyuba.core.protocol;

import com.iyuba.core.network.INetStateReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BaseJSONResponseForUploadImg implements BaseHttpResponse {
    public String Result = "";
    public String resultURL = "";
    protected InputStream inputStream = null;

    @Override
    public InputStream getInputStream() {

        return inputStream;
    }

    @Override
    public boolean isAllowCloseInputStream() {

        return true;
    }

    @Override
    public ErrorResponse parseInputStream(int rspCookie,
                                          BaseHttpRequest request, InputStream inputStream, int len,
                                          INetStateReceiver stateReceiver) {

        this.inputStream = inputStream;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputStream));

            StringBuffer sb = new StringBuffer();

            String result = br.readLine();

            while (result != null) {
                sb.append(result);
                result = br.readLine();
            }
            String json = sb.toString();

            urlResult(json);
            return null;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return new ErrorResponse();
    }

    // public abstract void parseResult(JSONObject resoultsource)
    // throws JSONException;

    public abstract void urlResult(String url);

}
