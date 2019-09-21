package com.iyuba.adsdk.other;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebChromeClientEx extends WebChromeClient {

	private AdWebBrowser mYouDaoBrowser;
	
	public WebChromeClientEx(AdWebBrowser paramYouDaoBrowser) {
		this.mYouDaoBrowser = paramYouDaoBrowser;
	}

	@Override
	public void onProgressChanged(WebView webView, int progress) {
		this.mYouDaoBrowser.setTitle("Loading...");
		this.mYouDaoBrowser.setProgress(progress * 100);
		if (progress == 100)
			this.mYouDaoBrowser.setTitle(webView.getUrl());
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.aw JD-Core Version: 0.6.2
 */