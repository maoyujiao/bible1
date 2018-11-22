package com.iyuba.CET4bible.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.configation.Constant;

public class WebActivity extends Activity {
    private ImageView backButton;
    private WebView web;
    private TextView textView;
    private ProgressBar progressIndictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lib_web);
        progressIndictor = findViewById(R.id.pb_progress_indictor);
        backButton = findViewById(R.id.lib_button_back);
        textView = findViewById(R.id.web_buyiyubi_title);
        web = findViewById(R.id.webView);
        textView.setText(Constant.APPName);
        textView.setTextColor(getResources().getColor(R.color.trainingcamp_white));
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(this.getIntent().getStringExtra("url"));
        Log.e("1", "1");
        web.requestFocus();
        Log.e("1", "2");
        web.getSettings().setBuiltInZoomControls(true);// 显示放大缩小
        web.getSettings().setSupportZoom(true);// 可放�?
        web.getSettings().setRenderPriority(RenderPriority.HIGH);// 提高渲染,加快加载速度
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebActivity", "" + url);
                if (url.startsWith("http") || url.startsWith("https")) {
                    view.loadUrl(url);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });

        setWebViewIndictror();

        web.setDownloadListener(new DownloadListener() {
            @Override

            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            web.goBack(); // goBack()表示返回webView的上�?���?
            return true;
        } else if (!web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    /**
     * 设置顶部进度条的可见性
     */
    private void setWebViewIndictror() {

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressIndictor.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == progressIndictor.getVisibility()) {
                        progressIndictor.setVisibility(View.VISIBLE);
                    }
                    progressIndictor.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }
}
