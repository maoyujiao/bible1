/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.protocol.news;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;

import java.io.IOException;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class ReadCountRequest extends BaseXMLRequest {

    public ReadCountRequest(String ids) {
        setAbsoluteURI("http://daxue.iyuba.cn/appApi/UnicomApi?protocol=70002&format=xml&appName=music&voaids="
                + ids);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ReadCountResponse();
    }

}
