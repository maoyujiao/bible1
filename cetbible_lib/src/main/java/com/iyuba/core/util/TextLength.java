package com.iyuba.core.util;

public class TextLength {// 描述字符串长度的类

    public static boolean isChineseChar(char c) throws Exception {// 判断是否是一个汉字
        return String.valueOf(c).getBytes("GBK").length > 1;// 汉字的字节数大于1
    }

    public static int getChineseCount(String s) throws Exception {// 获得汉字的长度
        char c;
        int chineseCount = 0;
        if (!"".equals("")) {// 判断是否为空
            s = new String(s.getBytes(), "GBK"); // 进行统一编码
        }
        for (int i = 0; i < s.length(); i++) {// for循环
            c = s.charAt(i); // 获得字符串中的每个字符
            if (isChineseChar(c)) {// 调用方法进行判断是否是汉字
                chineseCount++; // 等同于chineseCount=chineseCount+1
            }
        }
        return chineseCount; // 返回汉字个数
    }

    public static int getEnglishCount(String s) {// 获得字母、数字、空格的个数
        char ch;
        int character = 0;
        for (int i = 0; i < s.length(); i++) // for循环
        {
            ch = s.charAt(i);
            if ((ch >= ' ' && ch <= '~'))// 统计字母
                character++; // 等同于character=character+1
        }
        return character;
    }

    public static int getLength(String s) {
        int i = 0;
        try {
            i = getEnglishCount(s) / 2 + getChineseCount(s);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return i;
    }

    public static int getStringInfo(String s) {// 获得字母、数字、空格的个数
        char ch;
        int character = 0, blank = 0, number = 0;
        for (int i = 0; i < s.length(); i++) // for循环
        {
            ch = s.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))// 统计字母
                character++; // 等同于character=character+1
            else if (ch == ' ') // 统计空格
                blank++; // 等同于blank=blank+1
            else if (ch >= '0' && ch <= '9') // 统计数字
                number++; // 等同于number=number+1;
        }
        return character + blank + number;
    }

}
