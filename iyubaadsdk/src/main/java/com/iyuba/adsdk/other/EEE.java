package com.iyuba.adsdk.other;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.List;
/*import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;*/

public class EEE {

	public static boolean isIntentAvailable(Context context, Intent intent) {
		try {
			PackageManager pm = context.getPackageManager();
			List localList = pm.queryIntentActivities(intent, 0);
			return !localList.isEmpty();
		} catch (NullPointerException localNullPointerException) {
		}
		return false;
	}

	public static boolean isUrlHttpOrHttps(String url) {
		if (url == null) {
			return false;
		}

		String str = Uri.parse(url).getScheme();
		return ("http".equals(str)) || ("https".equals(str));
	}

	private static boolean isUrlMarket(String url) {
		if (url == null) {
			return false;
		}

		Uri localUri = Uri.parse(url);
		String str1 = localUri.getScheme();
		String str2 = localUri.getHost();
		if (("play.google.com".equals(str2)) || ("market.android.com".equals(str2))) {
			return true;
		}
        return "market".equals(str1);
    }
	public static boolean isUrlMarketOrNonHttp(String url) {
		return (isUrlMarket(url)) || (!isUrlHttpOrHttps(url));
	}
	public static boolean a(Context context) {
		return a(context, "twitter://timeline", false);
	}

	public static String c(String url) {
		try {
			URI uri = new URI(url);
			List<NameValuePair> localList = URLEncodedUtils.parse(uri, "UTF-8");
			for (NameValuePair localNameValuePair : localList) {
				String str = localNameValuePair.getName();
				if ("youdao_bid".equalsIgnoreCase(str.trim()))
					return localNameValuePair.getValue();
			}
		} catch (Exception localException) {
			return "";
		}
		return "";
	}

	public static boolean d(String url) {
		try {
			URI localURI = new URI(url);
			String str1 = localURI.getPath();

			List<NameValuePair> localList = URLEncodedUtils.parse(localURI, "UTF-8");
			for (NameValuePair localNameValuePair : localList) {
				String str2 = localNameValuePair.getName();
				if ("yd_apk_download".equalsIgnoreCase(str2.trim())) {
					String str3 = localNameValuePair.getValue();
					if ("1".equals(str3))
						return true;
					if ("2".equals(str3)) {
						return false;
					}
				}
			}

			if ((!TextUtils.isEmpty(str1)) && (str1.endsWith(".apk")))
				return true;
		} catch (Exception localException) {
			return false;
		}
		return false;
	}

	public static boolean isUrlYDapkDownload(String url) {
		try {
			URI localURI = new URI(url);
			List<NameValuePair> localList = URLEncodedUtils.parse(localURI, "UTF-8");
			for (NameValuePair localNameValuePair : localList) {
				String str1 = localNameValuePair.getName();
				if ("yd_apk_download".equalsIgnoreCase(str1.trim())) {
					String str2 = localNameValuePair.getValue();
					if ("2".equals(str2))
						return true;
				}
			}
		} catch (Exception localException) {
			return false;
		}
		return false;
	}

	public static boolean a(Context context, String url, boolean ifLog) {
		Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));

		if (!isIntentAvailable(context, localIntent)) {
			if (ifLog) {
				//TODO later maybe the log part will be added
//				ar.b("Could not handle application specific action: " + url + ". "
//						+ "You may be running in the emulator or another device which does not "
//						+ "have the required application.");
			}
			return false;
		}

		return true;
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.E JD-Core Version: 0.6.2
 */