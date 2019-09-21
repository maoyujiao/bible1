package com.iyuba.abilitytest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.iyuba.configation.RuntimeManager;

/**
 * 判断网络类型
 * 
 * @author Liuzhenli
 * 
 */
public class NetWorkState {

	private Context mContext;

	public NetWorkState(Context context) {
		this.mContext = context;
	}

	/**
	 * 是否连接网络
	 * 
	 * @return
	 */
	public static boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) RuntimeManager
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	/**
	 * 当前的网络类型
	 * 0 --无网络连接
	 * 1 --移动网络
	 * 2 --wifi网络
	 * 
	 * @return
	 */
	public static int getAPNType() {
		int netType = 0;
		ConnectivityManager connMgr = (ConnectivityManager) RuntimeManager
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
//			if (nType == TelephonyManager.NETWORK_TYPE_GPRS
//					|| nType == TelephonyManager.NETWORK_TYPE_CDMA
//					|| nType == TelephonyManager.NETWORK_TYPE_EDGE) {// 2G
//			}
			netType = 1;
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 2;
		}
		return netType;
	}
}
