/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.BlogDataManager;
import com.iyuba.CET4bible.protocol.info.UpdateBlogReadTimesRequest;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.CET4bible.util.Share;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.ContextMenu;

import java.io.InputStream;
import java.net.URL;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class BlogActivity extends BasisActivity {
    private Context mContext;
    private View backBtn, moreBtn;
    private TextView title, source, createtime, content, desccn, Title;
    private Blog blog;
    private ContextMenu contextMenu;
    private Spanned sp;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    content.setText(sp);
                    break;
                case 1:
                    ExeProtocol.exe(new UpdateBlogReadTimesRequest(blog.id),
                            new ProtocolResponse() {
                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                }

                                @Override
                                public void error() {

                                }
                            });
                    break;
                default:
                    break;
            }

        }
    };
    private String message;
    private Thread thread = new Thread() {
        @Override
        public void run() {

            super.run();
            sp = Html.fromHtml(message, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    InputStream is = null;
                    try {
                        is = (InputStream) new URL(source).getContent();
                        Drawable d = Drawable.createFromStream(is, "src");
                        d.setBounds(0, 0, d.getIntrinsicWidth(),
                                d.getIntrinsicHeight());
                        is.close();
                        return d;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }, null);
            handler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        blog = BlogDataManager.Instance().blog;
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        Title = findViewById(R.id.title_info);
        Title.setText(getIntent().getStringExtra("title"));
        contextMenu = findViewById(R.id.context_menu);
        moreBtn = findViewById(R.id.more);
        moreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                contextMenu.setText(mContext.getResources().getStringArray(
                        R.array.blog));
                contextMenu.setCallback(new ResultIntCallBack() {
                    @Override
                    public void setResult(int result) {
                        switch (result) {
                            case 0:
                                contextMenu.dismiss();
                                String imagePath = Constant.iconAddr;

                                new Share(mContext).shareMessage(imagePath,blog.desccn,
                                        blog.url,blog.title);
                                break;
                            case 1:
                                Uri uri = Uri.parse(blog.url);
                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                mContext.startActivity(it);
                                break;
                            default:
                                break;
                        }
                        contextMenu.dismiss();
                    }
                });
                contextMenu.show();
            }
        });
        handler.sendEmptyMessage(1);
        init();
    }

    private void init() {

        title = findViewById(R.id.title);
        source = findViewById(R.id.source);
        createtime = findViewById(R.id.createtime);
        content = findViewById(R.id.content);
        desccn = findViewById(R.id.desccn);
        title.setText(blog.title);
        source.setText(mContext.getString(R.string.source) + blog.source);
        createtime.setText(blog.createtime);
        desccn.setText(Html.fromHtml(mContext.getString(R.string.desc)
                + blog.desccn));
        message = TextAttr.decode(blog.essay.replace("%09", ""));
        thread.start();
    }

    @Override
    public void onBackPressed() {
        if (contextMenu.isShown()) {
            contextMenu.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
