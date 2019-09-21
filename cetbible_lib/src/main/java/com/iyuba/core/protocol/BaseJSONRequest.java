package com.iyuba.core.protocol;

import com.iyuba.core.network.INetStateReceiver;
import com.iyuba.core.network.NetworkData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseJSONRequest extends BaseHttpRequest {

    protected String requestVersion = BaseProtocolDef.PROTOCOL_VERSION_1;

    public BaseJSONRequest() {
        if (NetworkData.sessionId != null && !NetworkData.sessionId.equals("")) {
            setAbsoluteURI(NetworkData.accessPoint + ";jsessionid="
                    + NetworkData.sessionId);
        } else {
            setAbsoluteURI(NetworkData.accessPoint);
        }
    }

    @Override
    public void fillOutputStream(int cookie, OutputStream output,
                                 INetStateReceiver stateReceiver) {
    }

    @Override
    public String[][] getExtraHeaders() {
        String[][] aryHeaders = new String[1][2];
        aryHeaders[0][0] = "Content-Type";
        aryHeaders[0][1] = "application/json; charset=UTF-8";
        return aryHeaders;
    }

    /**
     * 请求包体填充抽象接口，子类实现此接口完成具体请求包体的构建
     *
     * @param serializer ：xml流构建器
     */
    protected abstract void fillBody(JSONObject jsonObject)
            throws JSONException;

    @SuppressWarnings("unused")
    private void fillHeader(JSONObject jsonObject) {

    }

}
