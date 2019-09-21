package com.iyuba.core.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Base64Coder {

    public final static String ENCODING = "UTF-8";

    // 加密
    public static String encoded(String data)
            throws UnsupportedEncodingException {
        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    // 加密,遵循RFC标准
    public static String encodedSafe(String data)
            throws UnsupportedEncodingException {
        byte[] b = Base64.encodeBase64(data.getBytes(ENCODING), true);
        return new String(b, ENCODING);
    }

    // 解密
    public static String decode(String data)
            throws UnsupportedEncodingException {
        byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));
        return new String(b, ENCODING);
    }

    public static String encode(String password) {
        String str = password + getTime();
        String result = null;
        try {
            result = Base64Coder.encodedSafe(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    public static void main(String args[]) throws Exception {

        try {
            String username = "userhello";
            String password = "123";
            String datetime = "20150113150501";
            String str = username + password + datetime;
            // 加密该字符串
            String encodedString;
            encodedString = Base64Coder.encodedSafe(str);
            //System.out.println(encodedString);
            // 解密该字符串
            String decodedString = Base64Coder.decode(encodedString);

            //String decodedString = Base64Coder.decode("dXNlcmhlbGxvMTIzMjAxNTAxMTMxNTA1MDE=");
            //System.out.println(decodedString);
            //System.out.println(decodedString.substring(decodedString.length()-14, decodedString.length()));
            Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse("20150113162801");
            Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse("20150113163001");
            System.out.println((date.getTime() - date1.getTime()) / 1000);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }


    }

}
