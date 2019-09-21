/**
 *
 */
package com.iyuba.core.me.protocol;

import android.text.TextUtils;

import com.iyuba.core.me.sqlite.mode.DoingsCommentInfo;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * @author yao 回复心情、评论心情
 */
public class RequestDoingSendComments extends VOABaseJsonRequest {

    public static final String protocolCode = "30007";

    public RequestDoingSendComments(DoingsCommentInfo doingsCommentInfo,
                                    String doid, String fromUid, String fromMsg, String orignUid,
                                    String orignMsg) {
        super(protocolCode);
        setRequestParameter("uid", doingsCommentInfo.uid);
        if (TextUtils.isEmpty(doingsCommentInfo.username) || "null".equals(doingsCommentInfo.username)) {
            doingsCommentInfo.username = doingsCommentInfo.uid;
        }

        setRequestParameter("username",
                URLEncoder.encode(doingsCommentInfo.username));
        setRequestParameter("doid", doid);
        setRequestParameter("upid", doingsCommentInfo.upid);
        setRequestParameter("message",
                URLEncoder.encode(doingsCommentInfo.message));
        if (doingsCommentInfo.grade == null
                || doingsCommentInfo.grade.equals("")) {

        } else {
            setRequestParameter("grade", doingsCommentInfo.grade);
        }
        setRequestParameter("fromUid", fromUid);// 要回复的人的id
        setRequestParameter("fromMsg",
                URLEncoder.encode(URLEncoder.encode(fromMsg)));// 要回复的人的内容
        setRequestParameter("orignUid", orignUid);// 该doing 作者的id
        setRequestParameter("orignMsg",
                URLEncoder.encode(URLEncoder.encode(orignMsg)));// 该doing 的内容
        setRequestParameter(
                "sign",
                MD5.getMD5ofStr(protocolCode + doingsCommentInfo.uid
                        + doingsCommentInfo.username
                        + doingsCommentInfo.message + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseDoingSendComments();
    }

}
