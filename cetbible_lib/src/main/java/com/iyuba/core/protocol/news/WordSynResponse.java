package com.iyuba.core.protocol.news;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;
import com.iyuba.core.sqlite.mode.Word;

import java.util.ArrayList;
import java.util.Vector;

public class WordSynResponse extends BaseXMLResponse {
    public ArrayList<Word> wordList = new ArrayList<Word>();
    public int total;
    public int firstPage;
    public int prevPage;
    public int nextPage;
    public int lastPage;
    private String user;

    public WordSynResponse(String user) {
        this.user = user;
    }

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {

        Vector rankVector = bodyElement.getChildren();
        total = Integer.parseInt(Utility
                .getSubTagContent(bodyElement, "counts"));
        firstPage = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                "firstPage"));
        prevPage = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                "prevPage"));
        nextPage = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                "nextPage"));
        lastPage = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                "lastPage"));
        int size = rankVector.size();
        kXMLElement ranKXMLElement;
        Word word;
        for (int i = 0; i < size; i++) {
            ranKXMLElement = (kXMLElement) rankVector.elementAt(i);
            if (ranKXMLElement.getTagName().equals("row")) {
                word = new Word();
                try {
                    word.key = Utility.getSubTagContent(ranKXMLElement, "Word");
                } catch (Exception e) {

                }
                try {
                    word.audioUrl = Utility.getSubTagContent(ranKXMLElement,
                            "Audio");
                } catch (Exception e) {

                    e.printStackTrace();
                }
                try {
                    word.pron = Utility
                            .getSubTagContent(ranKXMLElement, "Pron");
                } catch (Exception e) {

                }
                try {
                    word.def = Utility.getSubTagContent(ranKXMLElement, "Def");
                } catch (Exception e) {

                    e.printStackTrace();
                }
                word.userid = user;
                wordList.add(word);
            }
        }
        return true;
    }

}
