package com.iyuba.adsdk.other;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class BrowserRightOCL implements View.OnClickListener {

	private AdWebBrowser mYouDaoBrowser;

	public BrowserRightOCL(AdWebBrowser paramYouDaoBrowser) {
		this.mYouDaoBrowser = paramYouDaoBrowser;
	}

	public void onClick(View paramView) {
		// if (YouDaoBrowser.c(this.mYouDaoBrowser).canGoForward())
		// YouDaoBrowser.c(this.mYouDaoBrowser).goForward();

		if (AdWebBrowser.getInstanceWebview(this.mYouDaoBrowser).canGoForward())
			AdWebBrowser.getInstanceWebview(this.mYouDaoBrowser).goForward();
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.ay JD-Core Version: 0.6.2
 */