/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.me.MessageLetterContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 私信内容
 */
public class ResponseMessageLetterContentList extends BaseJSONResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    public MessageLetterContent letter = new MessageLetterContent();
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
            if (result.equals("631")) {
                data = jsonObjectRootRoot.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    JSONObject jsonObject;
                    MessageLetterContent letter;
                    for (int i = 0; i < size; i++) {
                        jsonObject = ((JSONObject) data.opt(i));
                        letter = new MessageLetterContent();
                        letter.message = jsonObject.getString("message");
                        letter.authorid = jsonObject.getString("authorid");
                        letter.dateline = jsonObject.getString("dateline");
                        letter.pmid = jsonObject.getString("authorid");
                        list.add(letter);
                    }
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }
}
