package com.iyuba.adsdk.other;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class BrowserLeftOCL implements View.OnClickListener {

	private AdWebBrowser mYouDaoBrowser;

	public BrowserLeftOCL(AdWebBrowser paramYouDaoBrowser) {
		this.mYouDaoBrowser = paramYouDaoBrowser;
	}

	public void onClick(View view) {
		// if (YouDaoBrowser.c(this.mYouDaoBrowser).canGoBack())
		// YouDaoBrowser.c(this.mYouDaoBrowser).goBack();

		if (AdWebBrowser.getInstanceWebview(this.mYouDaoBrowser).canGoBack())
			AdWebBrowser.getInstanceWebview(this.mYouDaoBrowser).goBack();
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.ax JD-Core Version: 0.6.2
 */