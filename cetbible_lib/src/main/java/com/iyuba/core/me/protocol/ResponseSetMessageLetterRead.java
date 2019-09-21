/**
 *
 */
package com.iyuba.core.me.protocol;

import com.iyuba.core.me.sqlite.mode.MessageLetter;
import com.iyuba.core.me.sqlite.mode.MessageLetterContent;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 */
public class ResponseSetMessageLetterRead extends BaseJSONResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public MessageLetter letter = new MessageLetter();
    public JSONArray data;
    public ArrayList<MessageLetterContent> list;
    public int num;
    public String plid;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        list = new ArrayList<MessageLetterContent>();
        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            message = jsonObjectRootRoot.getString("message");
            if (result.equals("621")) {
                System.out.println("设置成已读");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
        // return false;
    }

}
