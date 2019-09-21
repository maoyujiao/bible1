package com.iyuba.core.thread;

import android.os.Environment;

import com.iyuba.core.listener.OperateCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 *
 * @author 陈彤
 */
public class UploadFile {
    private static String success;

    public static void post(String filepath, String actionUrl, OperateCallBack or)
            throws JSONException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());

            File files = new File(filepath);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\""
                    + files + "\"" + ";filename=\""
                    + System.currentTimeMillis() + ".jpg\"" + end);
            ds.writeBytes(end);

            FileInputStream fStream = new FileInputStream(files);
			/* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            success = b.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(success.substring(
                success.indexOf("{"), success.lastIndexOf("}") + 1));
        if (jsonObject.getString("status").equals("0")) {
            or.success(null);
        } else {
            or.fail(null);
        }
    }

    public static void postHead(String filepath, String actionUrl, OperateCallBack or)
            throws JSONException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            File files = new File(filepath);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\""
                    + files + "\"" + ";filename=\""
                    + System.currentTimeMillis() + ".jpg\"" + end);
            ds.writeBytes(end);
            FileInputStream fStream = new FileInputStream(files);
			/* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            success = b.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(success.substring(
                success.indexOf("{"), success.lastIndexOf("}") + 1));
        if (!jsonObject.getString("result").equals("1")) {
            or.success(jsonObject.getString("message"));
        } else {
            or.fail(jsonObject.getString("message"));
        }
    }

    public static void post2(String filepath, String actionUrl, OperateCallBack or)
            throws JSONException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            File files = new File(filepath);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\""
                    + files + "\"" + ";filename=\""
                    + System.currentTimeMillis() + ".mar\"" + end);
            ds.writeBytes(end);
            FileInputStream fStream = new FileInputStream(files);
			/* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            success = b.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(success.substring(
                success.indexOf("{"), success.lastIndexOf("}") + 1));
        if (jsonObject.getString("status").equals("0")) {
            or.success(null);
        } else {
            or.fail(null);
        }
    }

    public static void post(String actionUrl, OperateCallBack or)
            throws IOException, JSONException {
        String filepath = Environment.getExternalStorageDirectory() + "/uploadImage.jpg";
        post(filepath, actionUrl, or);
    }

    public static void post(String actionUrl, Map<String, String> params,
                            Map<String, File> files, OperateCallBack or) throws
            JSONException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            // 首先组拼文本类型的参数

            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"" + end + end);
                ds.writeBytes(entry.getValue());
                ds.writeBytes(end);
            }

            if (files != null) {
                for (HashMap.Entry<String, File> file : files.entrySet()) {
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + file.getKey() + "\"" + ";filename=\""
                            + System.currentTimeMillis() + ".amr\"" + end);
                    ds.writeBytes(end);
                    FileInputStream fStream = new FileInputStream(
                            file.getValue());
					/* 设定每次写入1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
					/* 从文件读取数据到缓冲区 */
                    while ((length = fStream.read(buffer)) != -1) {
                        ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
					/* close streams */
                    fStream.close();
                }
            }
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            success = b.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(success.substring(
                success.indexOf("{"), success.lastIndexOf("}") + 1));
        if (jsonObject.getString("status").equals("0")) {
            or.success(null);
        } else {
            or.fail(null);
        }
    }
}
