package com.iyuba.adsdk.other;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iyuba.adsdk.extra.common.AdWebBrowser;

public class WebViewClientEX extends WebViewClient {

    private AdWebBrowser mYouDaoBrowser;

    public WebViewClientEX(AdWebBrowser paramYouDaoBrowser) {
        this.mYouDaoBrowser = paramYouDaoBrowser;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(this.mYouDaoBrowser, "MoPubBrowser error: " + description, 0).show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url == null) {
            return false;
        }

        // if the url is a market or not starts with http/https , call the system browser to
        // finish this action, not by the browser itself
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if ((EEE.isUrlMarketOrNonHttp(url)) && (EEE.isIntentAvailable(this.mYouDaoBrowser, intent))) {
            this.mYouDaoBrowser.startActivity(intent);
            this.mYouDaoBrowser.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        // YouDaoBrowser.a(this.mYouDaoBrowser).setImageDrawable(
        // PicResource.UNRIGHT_ARROW.decodeImage(this.mYouDaoBrowser));

        AdWebBrowser.getInstanceRightBtn(this.mYouDaoBrowser).setImageDrawable(
                PicResource.UNRIGHT_ARROW.decodeImage(this.mYouDaoBrowser));
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        Drawable localDrawable1 = view.canGoBack() ? PicResource.LEFT_ARROW
                .decodeImage(this.mYouDaoBrowser) : PicResource.UNLEFT_ARROW
                .decodeImage(this.mYouDaoBrowser);
        // YouDaoBrowser.b(this.mYouDaoBrowser).setImageDrawable(localDrawable1);
        AdWebBrowser.getInstanceLeftBtn(this.mYouDaoBrowser).setImageDrawable(localDrawable1);

        Drawable localDrawable2 = view.canGoForward() ? PicResource.RIGHT_ARROW
                .decodeImage(this.mYouDaoBrowser) : PicResource.UNRIGHT_ARROW
                .decodeImage(this.mYouDaoBrowser);
        // YouDaoBrowser.a(this.mYouDaoBrowser).setImageDrawable(localDrawable2);
        AdWebBrowser.getInstanceRightBtn(this.mYouDaoBrowser).setImageDrawable(localDrawable2);
    }
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.av JD-Core Version: 0.6.2
 */