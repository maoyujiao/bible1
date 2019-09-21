package com.iyuba.adsdk.other;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class BrowserRefreshOCL implements View.OnClickListener {

	private AdWebBrowser mYouDaoBrowser;

	public BrowserRefreshOCL(AdWebBrowser paramYouDaoBrowser) {
		this.mYouDaoBrowser = paramYouDaoBrowser;
	}

	public void onClick(View view) {
		// YouDaoBrowser.c(this.mYouDaoBrowser).reload();
		AdWebBrowser.getInstanceWebview(this.mYouDaoBrowser).reload();
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.az JD-Core Version: 0.6.2
 */