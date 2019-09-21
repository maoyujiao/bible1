package com.iyuba.http;

import com.iyuba.http.BaseHttpRequest.Method;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 请求处理辅助类
 */
public class RequestHandleHelper {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    // HttpURLConnection is set to use GZIP by default

    static BaseResponse getResponse(int rspCookie, BaseHttpRequest request,
                                    INetStateReceiver stateReceiver) {
        HttpURLConnection connection = null;
        ErrorResponse err = null;

        try {
            String url = request.getAbsoluteURI();
            URL parsedUrl = new URL(url);
            connection = openConnection(parsedUrl, request);

            setConnectionParametersForRequest(connection, request);

            connection.connect(); // Maybe this is unnecessary

            BaseResponse rsp = receiveAndParse(connection, request);

            return rsp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            err = new ErrorResponse(ErrorResponse.ERROR_PARAM_INVALID);
        } catch (IOException e) {
            e.printStackTrace();
            err = new ErrorResponse(ErrorResponse.ERROR_NET_IO);
        } finally {
            connection.disconnect();
        }

        if (stateReceiver != null && err != null) {
            stateReceiver.onNetError(request, rspCookie, err);
        }
        return err;
    }

    private static HttpURLConnection openConnection(URL url, BaseHttpRequest request)
            throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int timeout = request.getConnectionTimeout();
        connection.setReadTimeout(timeout);
        connection.setConnectTimeout(timeout);
        connection.setDoInput(true);

        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection,
                                                  BaseHttpRequest request) throws IOException {
        switch (request.getMethod()) {
            case Method.GET:
                connection.setRequestMethod("GET");
                break;
            case Method.POST:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            default:
                break;
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, BaseHttpRequest request)
            throws IOException {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private static BaseResponse receiveAndParse(HttpURLConnection connection, BaseHttpRequest request)
            throws IOException {
        BaseHttpResponse httpResponse = null;
        ErrorResponse err;
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        if (responseCode == HttpURLConnection.HTTP_OK) {
            LOGUtils.e("http233", "get responseCode : " + responseCode);
            InputStream is = connection.getInputStream();
            httpResponse = request.createResponse();
            httpResponse.setResponseHeaders(flatHeaders(connection.getHeaderFields()));
            err = httpResponse.parseInputStream(is);
        } else {
            err = new ErrorResponse(ErrorResponse.ERROR_UNKNOWN);
        }
        return (err == null) ? httpResponse : err;
    }

    private static Map<String, String> flatHeaders(Map<String, List<String>> rawHeader) {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<Entry<String, List<String>>> it = rawHeader.entrySet().iterator();
        Entry<String, List<String>> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (entry.getKey() != null) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                value = value.substring(1, value.length() - 1);
                result.put(key, value);
            }
        }
        return result;
    }

}
