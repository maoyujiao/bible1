package com.iyuba.CET4bible.protocol.info;

import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;

public class UpdateBlogReadTimesResponse extends BaseXMLResponse {

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {

        return true;
    }
}
