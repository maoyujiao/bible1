package com.iyuba.core.me.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.adapter.ChattingAdapter;
import com.iyuba.core.me.sqlite.mode.Emotion;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestMessageLetterContentList;
import com.iyuba.core.protocol.message.RequestSendMessageLetter;
import com.iyuba.core.protocol.message.ResponseMessageLetterContentList;
import com.iyuba.core.sqlite.mode.me.MessageLetterContent;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 私信内容界面
 *
 * @author chentong
 * @version 1.1
 * @para "currentname"； 当前用户名 "friendid" 好友id； "search" 是否从搜索而来 可不传； "auto send"
 * 为纠错特化 可不传
 * @修改内容 倒序显示会话内容 修复部分UI
 */
@SuppressLint("NewApi")
public class Chatting extends BasisActivity {
    private ChattingAdapter adapter;
    private ArrayList<MessageLetterContent> list = new ArrayList<MessageLetterContent>();
    private ListView chatContent;
    private Button sendBtn, back, home;
    private EditText textEditor;
    private Button showBtn;
    private RelativeLayout rlShow;
    private CustomDialog waitingDialog;
    private TextView friendName;
    private GridView emotion_GridView;
    private String auto_send;
    private String sendStr;
    private String friendid, search, currentname = null;
    private Context mContext;
    private int page;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    list.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    // 联网获取日志列表，滑到顶部点击更多进行加载
                    if (friendid != null) {
                        ClientSession.Instance().asynGetResponse(
                                new RequestMessageLetterContentList(
                                        AccountManager.Instace(mContext).userId,
                                        friendid, page), new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse response,
                                            BaseHttpRequest request, int rspCookie) {

                                        handler.sendEmptyMessage(3);
                                        ResponseMessageLetterContentList res = (ResponseMessageLetterContentList) response;
                                        if (res.result.equals("631")) {
                                            Collections.reverse(res.list);
                                            list.addAll(0, res.list);
                                            adapter.setList(list);
                                            page++;
                                            handler.sendEmptyMessage(4);
                                        } else if (res.result.equals("632")) {
                                            handler.sendEmptyMessage(5);
                                        }
                                    }
                                });
                    } else {
                        ClientSession
                                .Instance()
                                .asynGetResponse(
                                        new RequestMessageLetterContentList(
                                                AccountManager.Instace(mContext).userId,
                                                SocialDataManager.Instance().letter.friendid,
                                                page), new IResponseReceiver() {
                                            @Override
                                            public void onResponse(
                                                    BaseHttpResponse response,
                                                    BaseHttpRequest request,
                                                    int rspCookie) {

                                                handler.sendEmptyMessage(3);
                                                ResponseMessageLetterContentList res = (ResponseMessageLetterContentList) response;
                                                if (res.result.equals("631")) {
                                                    Collections.reverse(res.list);
                                                    list.addAll(res.list);
                                                    adapter.setList(list);
                                                    page++;
                                                    handler.sendEmptyMessage(4);
                                                } else if (res.result.equals("632")) {
                                                    handler.sendEmptyMessage(5);
                                                }
                                            }

                                        });
                    }
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    CustomToast.showToast(mContext,
                            R.string.message_content_add_all);
                    chatContent.setSelection(0);
                    break;
                case 10:
                    // 发送私信
                    if (search != null && search.equals("search")) {
                        ClientSession
                                .Instance()
                                .asynGetResponse(
                                        new RequestSendMessageLetter(
                                                AccountManager.Instace(mContext).userId,
                                                SocialDataManager.Instance().searchItem.username,
                                                sendStr), new IResponseReceiver() {
                                            @Override
                                            public void onResponse(
                                                    BaseHttpResponse response,
                                                    BaseHttpRequest request,
                                                    int rspCookie) {

                                                handler.sendEmptyMessage(4);
                                            }
                                        });
                    } else if (currentname != null) {
                        ClientSession.Instance().asynGetResponse(
                                new RequestSendMessageLetter(
                                        AccountManager.Instace(mContext).userId,
                                        currentname, sendStr),
                                new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse request,
                                            BaseHttpRequest response, int rspCookie) {

                                        handler.sendEmptyMessage(4);
                                    }

                                });
                    } else {
                        ClientSession.Instance().asynGetResponse(
                                new RequestSendMessageLetter(
                                        AccountManager.Instace(mContext).userId,
                                        SocialDataManager.Instance().letter.name,
                                        sendStr), new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse response,
                                            BaseHttpRequest request, int rspCookie) {

                                        handler.sendEmptyMessage(4);
                                    }

                                });
                    }

                    break;
                default:
                    break;
            }
        }

    };
    private ClipboardManager clipboard;
    /**
     * 按键时监听
     */
    private int[] imageIds = new int[30];
    private View.OnClickListener l = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == sendBtn.getId()) {
                String str = textEditor.getText().toString();
                if (str != null
                        && (sendStr = str.trim().replaceAll("\r", "")
                        .replaceAll("\t", "").replaceAll("\n", "")
                        .replaceAll("\f", "")) != "") {
                    sendMessage(sendStr);
                }
                textEditor.setText("");
            }
            if (v.getId() == showBtn.getId()) {
                if (rlShow.getVisibility() == View.GONE) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Chatting.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    rlShow.setVisibility(View.VISIBLE);
                    initEmotion();
                    emotion_GridView.setVisibility(View.VISIBLE);
                    emotion_GridView
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int arg2, long arg3) {
                                    Bitmap bitmap = BitmapFactory
                                            .decodeResource(getResources(),
                                                    imageIds[arg2
                                                            % imageIds.length]);
                                    ImageSpan imageSpan = new ImageSpan(
                                            mContext, bitmap);
                                    String str = "image" + arg2;
                                    SpannableString spannableString = new SpannableString(
                                            str);
                                    String str1 = null;
                                    str1 = Emotion.express[arg2];
                                    SpannableString spannableString1 = new SpannableString(
                                            str1);
                                    if (str.length() == 6) {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        6,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else if (str.length() == 7) {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        7,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        5,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    textEditor.append(spannableString1);
                                }
                            });
                } else {
                    rlShow.setVisibility(View.GONE);
                }
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        chatContent = findViewById(R.id.chatting_history_lv);
        Intent intent = getIntent();
        friendid = intent.getStringExtra("friendid");
        search = intent.getStringExtra("search");
        currentname = intent.getStringExtra("currentname");
        auto_send = intent.getStringExtra("auto send");
        initWidget();
        waitingDialog = WaittingDialog.showDialog(mContext);
        initMessages();
    }

    private void initWidget() {

        sendBtn = findViewById(R.id.send_button);
        showBtn = findViewById(R.id.show);
        back = findViewById(R.id.messageletterContent_back_btn);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        home = findViewById(R.id.to_home);
        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, PersonalHome.class);
                SocialDataManager.Instance().userid = friendid;
                startActivity(intent);
            }
        });
        rlShow = findViewById(R.id.rl_show);
        textEditor = findViewById(R.id.text_editor);
        emotion_GridView = rlShow.findViewById(R.id.grid_emotion);
        friendName = findViewById(R.id.messagelettercontent_friendname);
        if (friendid != null) {
            if (search != null && search.equals("search")) {
                friendName
                        .setText(SocialDataManager.Instance().searchItem.username);
            } else {
                friendName.setText(currentname);
            }
        } else {
            friendName.setText(SocialDataManager.Instance().letter.name);
        }
        sendBtn.setOnClickListener(l);
        showBtn.setOnClickListener(l);
        textEditor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rlShow.getVisibility() == View.VISIBLE) {
                    rlShow.setVisibility(View.GONE);
                }
            }
        });

        textEditor.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {

                // 检查剪贴板是否有内容
                if (!clipboard.hasPrimaryClip()) {
                    CustomToast.showToast(mContext, "剪贴板无内容");
                } else {
                    StringBuffer resultString = new StringBuffer();
                    ClipData clipData = clipboard.getPrimaryClip();
                    int count = clipData.getItemCount();
                    for (int i = 0; i < count; ++i) {
                        ClipData.Item item = clipData.getItemAt(i);
                        CharSequence str = item.coerceToText(mContext);
                        resultString.append(str);
                    }
                    textEditor.setText(resultString.toString());
                }
                return true;
            }
        });
    }

    // 设置adapter
    private void setAdapterForThis() {
        adapter = new ChattingAdapter(this,
                AccountManager.Instace(mContext).userId);
        chatContent.setAdapter(adapter);
        chatContent.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ClipData clip = ClipData.newPlainText("chat message",
                        adapter.getItem(arg2).message);
                clipboard.setPrimaryClip(clip);
                CustomToast.showToast(mContext, "内容已复制");
            }
        });
        chatContent.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到顶部
                        if (view.getFirstVisiblePosition() == 0) {
                            handler.sendEmptyMessage(1);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


            }
        });
    }

    // 为listView添加数据
    private void initMessages() {
        setAdapterForThis();
        if (auto_send != null) {
            textEditor.setText(auto_send);
        }
        handler.sendEmptyMessage(0);
    }

    private void sendMessage(String sendStr) {
        handler.sendEmptyMessage(10);
        MessageLetterContent letterContent = new MessageLetterContent();
        letterContent.setMessage(sendStr);
        letterContent.setDirection(1);
        letterContent.setAuthorid(AccountManager.Instace(mContext).userId);
        letterContent
                .setDateline(String.valueOf(System.currentTimeMillis() / 1000));
        list.add(letterContent);
        adapter.setList(list);
        rlShow.setVisibility(View.GONE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(Chatting.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initEmotion() {

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                Emotion.initEmotion(),
                R.layout.team_layout_single_expression_cell,
                new String[]{"image"}, new int[]{R.id.image});
        emotion_GridView.setAdapter(simpleAdapter);
        emotion_GridView.setNumColumns(7);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
    }
}
