/**
 *
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author 获取相互关注者列表
 */
public class RequestMutualAttentionList extends BaseJSONRequest {
    public static final String protocolCode = "51003";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestMutualAttentionList(String uid, String page) {
        // super(protocolCode);
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&pageNumber=" + page
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
        /*
		 * MD5 m=new MD5(); setRequestParameter("uid", uid);
		 * setRequestParameter("pageNumber", page);
		 * //setRequestParameter("pageCounts", "10");
		 * setRequestParameter("sign",MD5.md5(protocolCode+uid+"iyubaV2"));
		 */

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseMutualAttentionList();
    }

}
