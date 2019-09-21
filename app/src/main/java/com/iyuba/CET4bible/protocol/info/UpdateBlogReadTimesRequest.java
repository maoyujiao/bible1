package com.iyuba.CET4bible.protocol.info;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;

import java.io.IOException;

/**
 * 用户登录
 *
 * @author chentong
 */
public class UpdateBlogReadTimesRequest extends BaseXMLRequest {

    public UpdateBlogReadTimesRequest(int id) {
        setAbsoluteURI("http://cms.iyuba.cn/cmsApi/updateReadCount.jsp?format=xml&essayids="
                + id);
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new UpdateBlogReadTimesResponse();
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

}
