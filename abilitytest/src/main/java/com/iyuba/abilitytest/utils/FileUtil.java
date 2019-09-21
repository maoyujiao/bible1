package com.iyuba.abilitytest.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class FileUtil {
    /**
     * 文件复制
     *
     * @param s
     * @param t
     */
    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fi != null)//11.3添加
                    fi.close();
                if (in != null)//11.3添加
                    in.close();//11.3 这里空指针
                if (fo != null)//11.3添加
                    fo.close();
                if (out != null)//11.3添加
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static String getTextFromFile(String str_filepath) {// 转码
        File file = new File(str_filepath);
        BufferedReader reader;
        String text = "";
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] first3bytes = new byte[3];
                in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
                in.reset();
                if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                        && first3bytes[2] == (byte) 0xBF) {// utf-8
                    reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                } else if (first3bytes[0] == (byte) 0xFF
                        && first3bytes[1] == (byte) 0xFE) {
                    reader = new BufferedReader(
                            new InputStreamReader(in, "unicode"));
                } else if (first3bytes[0] == (byte) 0xFE
                        && first3bytes[1] == (byte) 0xFF) {
                    reader = new BufferedReader(new InputStreamReader(in, "utf-16be"));
                } else if (first3bytes[0] == (byte) 0xFF
                        && first3bytes[1] == (byte) 0xFF) {
                    reader = new BufferedReader(new InputStreamReader(in,
                            "utf-16le"));
                } else {
                    reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                }
                String str = reader.readLine();
                while (str != null) {
                    text = text + str + "\n";
                    str = reader.readLine();
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {//文件不存在
//            text = "Answer the question below.";
            text = "";
        }
        return text;
    }
}
