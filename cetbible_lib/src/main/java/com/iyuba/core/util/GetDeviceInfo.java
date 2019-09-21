package com.iyuba.core.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/*
 * @author:ren
 * @date:2014��7��26��14:53:14
 * @description����ȡ�豸���ͺš�IP��ַ��MAC��ַ��Ϣ�������ϴ�ѧϰ��¼
 * */
public class GetDeviceInfo {
    //	private String DeviceType;
//	private String DeviceIP;
//	private String DeviceMAC;
    private Context mContext;

    public GetDeviceInfo(Context context) {
        this.mContext = context;
    }

    //��ȡ����IP����

    /**
     * <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     *
     * @return
     */
    public String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    //����ǻػ���ַ
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //ֱ�ӷ��ر���IP��ַ
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Error", ex.toString());
        }
        return null;
    }

    //��ȡ����MAC����
    public String getLocalMACAddress() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }

    //��ȡ����MAC����
    public String getLocalDeviceType() {

        return android.os.Build.MODEL;
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
        String strCurrTime = formatter.format(curDate);
        return strCurrTime;
    }
}
