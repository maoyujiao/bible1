/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class RequestSetMessageLetterRead extends BaseJSONRequest {
    public static final String protocolCode = "60003";

    public RequestSetMessageLetterRead(String uid, String plid) {
        // super(protocolCode);


        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&plid=" + plid
                // + "&pageNumber=" + 50
                + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + plid + "iyubaV2"));
        /*
		 * MD5 m=new MD5(); setRequestParameter("uid", uid);
		 * setRequestParameter("plid", plid);
		 * setRequestParameter("sign",MD5.md5(protocolCode+uid+plid+"iyubaV2")
		 * );
		 */
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSetMessageLetterRead();
    }

}
