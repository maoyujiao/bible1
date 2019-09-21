package com.iyuba.abilitytest.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class BaseJsonObjectRequest extends Request<JSONObject> {
    private static final String TAG = BaseJsonObjectRequest.class.getSimpleName();

    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format(
            "application/json; charset=%s", PROTOCOL_CHARSET);

    private Map<String, String> mMap;
    private Listener<JSONObject> mListener;
    private final String mRequestBody;

    public BaseJsonObjectRequest(int method, String url, String requestBody,
                                 Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        Log.i(TAG, "Request url => " + url);
        mListener = listener;
        mRequestBody = requestBody;
    }

    public BaseJsonObjectRequest(int method, String url, String requestBody,
                                 Map<String, String> map, Listener<JSONObject> listener,
                                 ErrorListener errorListener) {
        super(method, url, errorListener);
        Log.i(TAG, "Request url => " + url);
        mMap = map;
        mListener = listener;
        mRequestBody = requestBody;
    }

    public BaseJsonObjectRequest(int method, String url, String requestBody,
                                 Map<String, String> map, ErrorListener errorListener) {
        super(method, url, errorListener);
        mMap = map;
        mRequestBody = requestBody;
    }

    public BaseJsonObjectRequest(int method, String url, String requestBody,
                                 Map<String, String> map) {
        super(method, url, defaultErrListener);
        Log.i(TAG, "Request url => " + url);
        mListener = defaultListener;
        mRequestBody = requestBody;
        mMap = map;
    }

    public BaseJsonObjectRequest(int method, String url, Map<String, String> map) {
        this(method, url, null, map);
    }

    /**
     * request contains method info and url, no body part
     *
     * @param method
     * @param url
     */
    public BaseJsonObjectRequest(int method, String url) {
        //this(method, url, null, null);
        this(method, url, defaultErrListener);
    }

    public BaseJsonObjectRequest(int method, String url, ErrorListener el) {
        this(method, url, null, null, null, el);
    }

    /**
     * request contains method info , url and request body string
     *
     * @param method
     * @param url
     * @param requestBody
     */
    public BaseJsonObjectRequest(int method, String url, String requestBody) {
        this(method, url, requestBody, null);
    }

    /**
     * GET request no body part
     *
     * @param url
     */
    public BaseJsonObjectRequest(String url) {
        super(Method.GET, url, defaultErrListener);
        Log.e(TAG, "Request url => " + url);
        mListener = defaultListener;
        mRequestBody = null;
    }

    /**
     * GET request
     *
     * @param url
     * @param errorListener
     */
    public BaseJsonObjectRequest(String url, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        Log.i(TAG, "Request url => " + url);
        mListener = defaultListener;
        mRequestBody = null;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() {
        return mMap;
    }

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody
                    .getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf(
                    "Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    /**
     * 默认的 error listener
     */
    private static ErrorListener defaultErrListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    /**
     * 默认的listener
     */
    private static Listener<JSONObject> defaultListener = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i(TAG, response.toString());
        }
    };

    /**
     * 子类封装请求是否成功的逻辑
     *
     * @return
     */
    public abstract boolean isRequestSuccessful();

    /**
     * 要处理返回的数据，必须调用此函数，否则默认只Log返回信息
     *
     * @param resListener
     */
    public void setResListener(Listener<JSONObject> resListener) {
        this.mListener = resListener;
    }

}
