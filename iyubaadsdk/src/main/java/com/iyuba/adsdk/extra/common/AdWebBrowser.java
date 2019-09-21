package com.iyuba.adsdk.extra.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;

import com.iyuba.adsdk.other.BrowserCloseOCL;
import com.iyuba.adsdk.other.BrowserLeftOCL;
import com.iyuba.adsdk.other.BrowserRefreshOCL;
import com.iyuba.adsdk.other.BrowserRightOCL;
import com.iyuba.adsdk.other.PicResource;
import com.iyuba.adsdk.other.WebChromeClientEx;
import com.iyuba.adsdk.other.WebViewClientEX;

public class AdWebBrowser extends Activity {
    private Context mContext;
    private WebView mWebview;
    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private ImageButton refreshBtn;
    private ImageButton closeBtn;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        mContext = this;
        setResult(-1);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, -1);
        setContentView(buildContentView());

        initWebview();
        initBottomBar();
        initCookie();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebview() {
        WebSettings localWebSettings = this.mWebview.getSettings();

        localWebSettings.setJavaScriptEnabled(true);

        localWebSettings.setSupportZoom(true);
        localWebSettings.setBuiltInZoomControls(true);
        localWebSettings.setUseWideViewPort(true);

        this.mWebview.loadUrl(getIntent().getStringExtra("URL"));
        this.mWebview.setWebViewClient(new WebViewClientEX(this));

        this.mWebview.setWebChromeClient(new WebChromeClientEx(this));
        this.mWebview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void initBottomBar() {
        this.leftBtn.setBackgroundColor(0);
        this.leftBtn.setOnClickListener(new BrowserLeftOCL(this));

        this.rightBtn.setBackgroundColor(0);
        this.rightBtn.setOnClickListener(new BrowserRightOCL(this));

        this.refreshBtn.setBackgroundColor(0);
        this.refreshBtn.setOnClickListener(new BrowserRefreshOCL(this));

        this.closeBtn.setBackgroundColor(0);
        this.closeBtn.setOnClickListener(new BrowserCloseOCL(this));
    }

    private void initCookie() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
    }

    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    private View buildContentView() {
        LinearLayout localLinearLayout1 = new LinearLayout(this);
        LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        localLinearLayout1.setLayoutParams(localLayoutParams1);
        localLinearLayout1.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout localRelativeLayout = new RelativeLayout(this);
        LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        localRelativeLayout.setLayoutParams(localLayoutParams2);
        localLinearLayout1.addView(localRelativeLayout);

        LinearLayout localLinearLayout2 = new LinearLayout(this);
        localLinearLayout2.setId(1);
        RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        localLayoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        localLinearLayout2.setLayoutParams(localLayoutParams3);
        localLinearLayout2.setBackgroundDrawable(PicResource.BACKGROUND.decodeImage(this));
        localRelativeLayout.addView(localLinearLayout2);

        this.leftBtn = setImageButton(PicResource.LEFT_ARROW.decodeImage(this));
        this.rightBtn = setImageButton(PicResource.RIGHT_ARROW.decodeImage(this));
        this.refreshBtn = setImageButton(PicResource.REFRESH.decodeImage(this));
        this.closeBtn = setImageButton(PicResource.CLOSE.decodeImage(this));

        localLinearLayout2.addView(this.leftBtn);
        localLinearLayout2.addView(this.rightBtn);
        localLinearLayout2.addView(this.refreshBtn);
        localLinearLayout2.addView(this.closeBtn);

        this.mWebview = new WebView(this);
        RelativeLayout.LayoutParams localLayoutParams4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        localLayoutParams4.addRule(RelativeLayout.ABOVE, 1);
        this.mWebview.setLayoutParams(localLayoutParams4);
        localRelativeLayout.addView(this.mWebview);

        return localLinearLayout1;
    }

    private ImageButton setImageButton(Drawable paramDrawable) {
        ImageButton localImageButton = new ImageButton(this);

        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0F);
        localLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        localImageButton.setLayoutParams(localLayoutParams);

        localImageButton.setImageDrawable(paramDrawable);

        return localImageButton;
    }

    public static ImageButton getInstanceLeftBtn(AdWebBrowser ydBrowser) {
        return ydBrowser.leftBtn;
    }

    public static ImageButton getInstanceRightBtn(AdWebBrowser ydBrowser) {
        return ydBrowser.rightBtn;
    }

    public static ImageButton getInstanceRefreshBtn(AdWebBrowser ydBrowser) {
        return ydBrowser.refreshBtn;
    }

    public static ImageButton getInstanceCloseBtn(AdWebBrowser ydBrowser) {
        return ydBrowser.closeBtn;
    }

    public static WebView getInstanceWebview(AdWebBrowser ydBrowser) {
        return ydBrowser.mWebview;
    }
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.extra.common.YouDaoBrowser JD-Core Version: 0.6.2
 */