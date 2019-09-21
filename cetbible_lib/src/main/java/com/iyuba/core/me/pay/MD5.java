package com.iyuba.core.me.pay;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5ofStr(String text) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }
        try {
            msgDigest.update(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    public static String md5_16(String text) {
        return getMD5ofStr(text).substring(8, 24);
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;

        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; ++i) {
            out[(j++)] = DIGITS[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = DIGITS[(0xF & data[i])];
        }

        return out;
    }

    public static void main(String[] args) {
        // System.out.println("06120007307:"+md5("06120007307"));
        // System.out.println(md5("Fevy7gGa"));
        System.out.println(getMD5ofStr("哈哈"));
        System.out
                .println(getMD5ofStr("10003011272200dcbfc7a1645b9ecb5dae9c6073e7b09e254e53d9c0097369efbebf2b0f9cea06iyubaV2"));
        System.out.println("24200706:"
                + getMD5ofStr(getMD5ofStr("iyu123456") + "b0d5b5"));

    }
}