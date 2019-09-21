package com.iyuba.core.protocol.test;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;
import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;
import java.util.Vector;

public class CetResponse extends BaseXMLResponse {
    public ArrayList<CetAnswer> answerList = new ArrayList<CetAnswer>();
    public ArrayList<CetExplain> explainList = new ArrayList<CetExplain>();
    public ArrayList<CetText> textList = new ArrayList<CetText>();
    public int type;

    public CetResponse(int type) {
        this.type = type;
    }

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {

        Vector rankVector = bodyElement.getChildren();
        int size = rankVector.size();
        kXMLElement ranKXMLElement;
        Vector testVector;
        int length;
        kXMLElement rowKXMLElement;
        CetAnswer cetAnswer;
        CetText cetText;
        CetExplain cetExplain;
        for (int i = 0; i < size; i++) {
            ranKXMLElement = (kXMLElement) rankVector.elementAt(i);
            if (ranKXMLElement.getTagName().equals("testData")) {
                testVector = ranKXMLElement.getChildren();
                length = testVector.size();
                for (int j = 0; j < length; j++) {
                    rowKXMLElement = (kXMLElement) testVector.elementAt(j);
                    if (rowKXMLElement.getTagName().equals("row")) {
                        cetAnswer = new CetAnswer();
                        cetAnswer.a1 = Utility.getSubTagContent(rowKXMLElement,
                                "answer1");
                        cetAnswer.a2 = Utility.getSubTagContent(rowKXMLElement,
                                "answer2");
                        cetAnswer.a3 = Utility.getSubTagContent(rowKXMLElement,
                                "answer3");
                        cetAnswer.a4 = Utility.getSubTagContent(rowKXMLElement,
                                "answer4");
                        cetAnswer.id = Utility.getSubTagContent(rowKXMLElement,
                                "number");
                        cetAnswer.question = Utility.getSubTagContent(
                                rowKXMLElement, "question");
                        cetAnswer.rightAnswer = Utility.getSubTagContent(
                                rowKXMLElement, "answer");
                        cetAnswer.sound = Utility.getSubTagContent(
                                rowKXMLElement, "sound");
                        cetAnswer.qsound = "Q" + cetAnswer.id + ".mp3";
                        cetAnswer.flag = Utility.getSubTagContent(
                                rowKXMLElement, "flg");
                        String year = Utility.getSubTagContent(rowKXMLElement,
                                "testTime");
                        String url = "http://cetsoundsvip.iyuba.cn/" + type
                                + "/" + year + "/";
                        cetAnswer.sound = url + cetAnswer.sound;
                        cetAnswer.qsound = url + cetAnswer.qsound;
                        cetAnswer.yourAnswer = "";
                        answerList.add(cetAnswer);
                    }
                }
            }

            if (ranKXMLElement.getTagName().equals("textData")) {
                testVector = ranKXMLElement.getChildren();
                length = testVector.size();
                for (int j = 0; j < length; j++) {
                    rowKXMLElement = (kXMLElement) testVector.elementAt(j);
                    if (rowKXMLElement.getTagName().equals("row")) {
                        cetText = new CetText();
                        cetText.id = Utility.getSubTagContent(rowKXMLElement,
                                "number");
                        cetText.index = Utility.getSubTagContent(
                                rowKXMLElement, "indexNumber");
                        cetText.time = Utility.getSubTagContent(rowKXMLElement,
                                "timing");
                        cetText.sentence = Utility.getSubTagContent(
                                rowKXMLElement, "sentence");
                        cetText.sex = Utility.getSubTagContent(rowKXMLElement,
                                "sex");
                        textList.add(cetText);
                    }
                }
            }
            if (ranKXMLElement.getTagName().equals("explainData")) {
                testVector = ranKXMLElement.getChildren();
                length = testVector.size();
                for (int j = 0; j < length; j++) {
                    rowKXMLElement = (kXMLElement) testVector.elementAt(j);
                    if (rowKXMLElement.getTagName().equals("row")) {
                        cetExplain = new CetExplain();
                        cetExplain.explain = Utility.getSubTagContent(
                                rowKXMLElement, "Explains");
                        cetExplain.id = Utility.getSubTagContent(
                                rowKXMLElement, "Number");
                        cetExplain.keys = Utility.getSubTagContent(
                                rowKXMLElement, "Keys");
                        cetExplain.knowledge = Utility.getSubTagContent(
                                rowKXMLElement, "Knowledges");
                        explainList.add(cetExplain);
                    }
                }
            }
        }
        return true;
    }

}
