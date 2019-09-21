package com.iyuba.core.discover.protocol;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;


public class WordUpdateResponse extends BaseXMLResponse {
    // public List<Word> words;

    public int result;
    public String word;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {
        result = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                "result"));
        word = Utility.getSubTagContent(bodyElement, "word");
        return true;
    }

}
