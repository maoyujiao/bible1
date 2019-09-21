package com.iyuba.adsdk.other;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

import android.view.View;
import android.view.View.OnClickListener;

public class BrowserCloseOCL implements View.OnClickListener {

	private AdWebBrowser mYouDaoBrowser;
	
	public BrowserCloseOCL(AdWebBrowser paramYouDaoBrowser) {
		this.mYouDaoBrowser = paramYouDaoBrowser;
	}

	public void onClick(View view) {
		this.mYouDaoBrowser.finish();
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.aA JD-Core Version: 0.6.2
 */