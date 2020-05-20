package com.iyuba.CET4bible.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.protocol.info.JpBlogContentRequest;
import com.iyuba.CET4bible.protocol.info.JpBlogContentResponse;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.CET4bible.sqlite.op.BlogOp;
import com.iyuba.CET4bible.util.AdBannerUtil;
import com.iyuba.CET4bible.util.Share;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;

import okhttp3.Call;

public class JpBlogActivity extends BaseActivity {
    private View backBtn;
    private View moreBtn;
    private TextView title, createtime, Title;
    private org.sufficientlysecure.htmltextview.HtmlTextView content;
    private Blog blog;
    private BlogOp blogOp;
    private String message;
    private CustomDialog waitting;
    private Spanned spanned;
    private Thread thread;
    private RecyclerView recyclerView ;
    private

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    ExeProtocol.exe(new JpBlogContentRequest(blog.id + ""), new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            JpBlogContentResponse response = (JpBlogContentResponse) bhr;
                            Log.d("bible",response.bean.getMessage());
                            message = response.bean.getMessage();
                            blog.essay = message;
                            blogOp.saveData(blog);
                            handler.sendEmptyMessage(3);
                        }

                        @Override
                        public void error() {
                            handler.sendEmptyMessage(5);
                        }
                    });
                    break;
                case 3:
                    waitting.dismiss();
                    title.setText(blog.title);
                    createtime.setText(blog.createtime);
                    Log.e("---", message);
                    content.setHtml(message,new HtmlHttpImageGetter(content,null,true));
                    content.refreshDrawableState();
                    break;
                case 5:
                    waitting.dismiss();
                    CustomToast.showToast(mContext, "加载失败");
                    break;
                default:
                    break;
            }

        }
    };

    //学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private long time;
    private AdBannerUtil adBannerUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog);
        blogOp = new BlogOp(mContext);
        waitting = WaittingDialog.showDialog(this);

        blog = (Blog) getIntent().getSerializableExtra("blog");
        CrashApplication.getInstance().addActivity(this);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Title = findViewById(R.id.title_info);
        Title.setText("资讯");
        moreBtn = findViewById(R.id.more);
        moreBtn.setClickable(false);
//        moreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                contextMenu.setText(mContext.getResources().getStringArray(
//                        R.array.blog));
//                contextMenu.setCallback(new ResultIntCallBack() {
//                    @Override
//                    public void setResult(int result) {
//                        switch (result) {
//                            case 0:
//                                contextMenu.dismiss();
//                                new Share(mContext).prepareMessage(blog.title,
//                                        blog.desccn, blog.url);
//                                break;
//                            case 1:
//                                Uri uri = Uri.parse(blog.url);
//                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                                mContext.startActivity(it);
//                                break;
//                            default:
//                                break;
//                        }
//                        contextMenu.dismiss();
//                    }
//                });
//                contextMenu.show();
//            }
//        });
        if (Constant.APP_CONSTANT.isEnglish()) {
            moreBtn.setVisibility(View.VISIBLE);
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    share();
                }
            });
        }
        init();
        blog = blogOp.selectData(blog.id);

        if (TextUtils.isEmpty(blog.essay)) {
            waitting.show();
            handler.sendEmptyMessage(2);
        } else {
            message = blog.essay;
            handler.sendEmptyMessage(3);
        }

        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = "1";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = blog.id + "";
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        studyRecordInfo.TestNumber = blog.id + "";
        time = System.currentTimeMillis();

        adBannerUtil = new AdBannerUtil(mContext);
        adBannerUtil.setView(findViewById(R.id.youdao_ad), (ImageView) findViewById(R.id.photoImage), (TextView) findViewById(R.id.close));
//        adBannerUtil.setAddamView(findViewById(R.id.ad_addam));
        adBannerUtil.setMiaozeView((ViewGroup) findViewById(R.id.adMiaozeParent));
        adBannerUtil.loadAd();
    }

    private void init() {
        title = findViewById(R.id.title);
        createtime = findViewById(R.id.tv_time);
        content = findViewById(R.id.content);
        findViewById(R.id.rl_desc).setVisibility(View.GONE);
    }

    private void share() {
        Share share = new Share(getApplicationContext());
        share.setListener(getApplicationContext(), blog.id + "");
        share.prepareMessage(blog.title, "爱语吧 " + blog.title,
                "http://m.iyuba.cn/news.html?id=" + blog.id + "&type=cet" + Constant.APP_CONSTANT.TYPE());
    }

    @Override
    protected void onDestroy() {
        adBannerUtil.destroy();

        if (System.currentTimeMillis() - time < 1000 * 15) {
            e("--- 时间不够15秒 ---");
            super.onDestroy();
            return;
        }
        int wordsCount;
        try {
            wordsCount = content.getText().toString().split(" ").length;
        } catch (Exception e) {
            e.printStackTrace();
            wordsCount = 0;
        }

        studyRecordInfo.EndTime = deviceInfo.getCurrentTime();
        if (!TextUtils.isEmpty(studyRecordInfo.uid) && !"0".equals(studyRecordInfo.uid)) {
            try {
                if (AccountManager.Instace(mContext).checkUserLogin() && !TouristUtil.isTourist()) {
                    Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, "3", wordsCount + ""), new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
