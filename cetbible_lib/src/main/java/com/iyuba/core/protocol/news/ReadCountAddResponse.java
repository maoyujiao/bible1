/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.protocol.news;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class ReadCountAddResponse extends BaseJSONResponse {

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        return true;
    }

}
