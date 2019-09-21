/**
 *
 */
package com.iyuba.core.me.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.DoingsCommentAdapter;
import com.iyuba.core.me.protocol.RequestDoingById;
import com.iyuba.core.me.protocol.RequestDoingSendComments;
import com.iyuba.core.me.sqlite.mode.DoingsCommentInfo;
import com.iyuba.core.me.sqlite.mode.Emotion;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestDoingsCommentInfo;
import com.iyuba.core.protocol.message.ResponseDoingSendComments;
import com.iyuba.core.protocol.message.ResponseDoingsCommentInfo;
import com.iyuba.core.sqlite.mode.me.DoingsInfo;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.Expression;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;

/**
 * 心情详细界面
 *
 * @author chentong
 * @version 1.0
 * @para "doid" 心情id
 */
public class ReplyDoing extends BasisActivity implements OnScrollListener {
    private static String fromUid, fromMsg, orignUid, orignMsg;
    private Context mContext;
    private Button backButton;
    private ImageView doings_userPortrait;
    private TextView doings_username, doings_time, doings_message,
            doings_replyNum;
    private ListView doings_commentlist;
    private DoingsCommentAdapter doingsCommentListAdapter;
    private CustomDialog waitingDialog;
    private int curPage = 1;
    private ArrayList<DoingsCommentInfo> doingsCommentArrayList = new ArrayList<DoingsCommentInfo>();
    private int[] imageIds = new int[30];
    private DoingsInfo doingsInfo = new DoingsInfo();
    private Boolean isLastPage = false;
    private EditText editText;
    private Button emotionButton, sendButton;
    private GridView emotion_GridView;
    private RelativeLayout emotionShow;
    private String sendStr;
    private DoingsCommentInfo item = new DoingsCommentInfo();
    private String doid;
    /**
     * 按键时监听
     */
    private View.OnClickListener l = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == backButton) {
                finish();
            } else if (v == sendButton) {
                String str = editText.getText().toString();
                // String sendStr;
                if (str != null
                        && (sendStr = str.trim().replaceAll("\r", "")
                        .replaceAll("\t", "").replaceAll("\n", "")
                        .replaceAll("\f", "")) != "") {
                    sendMessage(sendStr);
                }
                editText.setText("");
            } else if (v == emotionButton) {
                if (emotionShow.getVisibility() == View.GONE) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ReplyDoing.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    emotionShow.setVisibility(View.VISIBLE);
                    initEmotion();
                    emotion_GridView.setVisibility(View.VISIBLE);
                    emotion_GridView
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {

                                    Bitmap bitmap = BitmapFactory
                                            .decodeResource(getResources(),
                                                    imageIds[position
                                                            % imageIds.length]);
                                    ImageSpan imageSpan = new ImageSpan(
                                            mContext, bitmap);
                                    String str = "image" + position;
                                    String str1 = Emotion.express[position];
                                    SpannableString spannableString = new SpannableString(
                                            str);
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
                                    editText.append(spannableString1);
                                }
                            });
                } else {
                    emotionShow.setVisibility(View.GONE);
                }
            } else if (v == editText) {
                if (emotionShow.getVisibility() == View.VISIBLE) {
                    emotionShow.setVisibility(View.GONE);
                }
            }
        }

        private void sendMessage(String sendStr) {
            item.dateline = String.valueOf(System.currentTimeMillis() / 1000);
            item.message = sendStr;
            item.uid = AccountManager.Instace(mContext).getId();
            item.username = AccountManager.Instace(mContext).getUserName();
            item.upid = "0";
            handler.sendEmptyMessage(20);
            doingsCommentArrayList.add(item);
            doingsCommentListAdapter.setData(doingsCommentArrayList);
            if (doingsCommentListAdapter != null) {
                doingsCommentListAdapter.notifyDataSetChanged();
            }
            emotionShow.setVisibility(View.GONE);
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(ReplyDoing.this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }

    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    ExeProtocol.exe(new RequestDoingsCommentInfo(doingsInfo.doid,
                            String.valueOf(curPage)), new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {

                            ResponseDoingsCommentInfo response = (ResponseDoingsCommentInfo) bhr;
                            if (response.result.equals("311")) {
                                doingsCommentArrayList
                                        .addAll(response.doingsCommentlist);
                                doingsCommentListAdapter
                                        .setData(response.doingsCommentlist);
                                if (Integer.parseInt(response.counts) <= doingsCommentArrayList
                                        .size()) {
                                    isLastPage = true;
                                }
                            } else {
                                handler.sendEmptyMessage(6);
                            }
                            curPage += 1;
                            handler.sendEmptyMessage(4);
                        }

                        @Override
                        public void error() {

                            handler.sendEmptyMessage(5);

                        }
                    });
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler.sendEmptyMessage(3);
                    doingsCommentListAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 6:
                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 20:
                    // 发送评论
                    ExeProtocol.exe(new RequestDoingSendComments(item,
                                    doingsInfo.doid, fromUid, fromMsg, orignUid, orignMsg),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseDoingSendComments res = (ResponseDoingSendComments) bhr;
                                    if (res.result.equals("361")) {
                                    } else {
                                        handler.sendEmptyMessage(6);
                                    }
                                    handler.sendEmptyMessage(4);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(5);
                                }
                            });
                    break;
                case 30:
                    ExeProtocol.exe(new RequestDoingById(doid),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    com.iyuba.core.protocol.message.ResponseDoingById res = (com.iyuba.core.protocol.message.ResponseDoingById) bhr;
                                    res.doingInfo.doid = doid;
                                    doingsInfo = res.doingInfo;
                                    handler.sendEmptyMessage(31);
                                    fromUid = doingsInfo.uid;
                                    fromMsg = doingsInfo.message;
                                    orignUid = doingsInfo.uid;
                                    orignMsg = doingsInfo.message;
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(5);
                                }
                            });
                    handler.sendEmptyMessage(2);
                    break;
                case 31:
                    initWidget();
                    break;
                case 100:
                    doingsCommentListAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_doings);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        waitingDialog = WaittingDialog.showDialog(mContext);
        doid = getIntent().getStringExtra("doid");
        handler.sendEmptyMessage(30);
    }

    private void setListener() {

        doings_commentlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                editText.findFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(editText, 0);
                editText.setText("回复 "
                        + doingsCommentArrayList.get(arg2).username + ":");
                Editable etext = editText.getText();
                int position = etext.length();
                Selection.setSelection(etext, position);
                item.grade = doingsCommentArrayList.get(arg2).grade;
                fromUid = doingsCommentArrayList.get(arg2).uid;
                fromMsg = doingsCommentArrayList.get(arg2).message;
                orignUid = doingsInfo.uid;
                orignMsg = doingsInfo.message;
            }
        });
        sendButton.setOnClickListener(l);
        emotionButton.setOnClickListener(l);
        editText.setOnClickListener(l);
    }

    private void initWidget() {

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        doings_userPortrait = findViewById(R.id.doings_userPortrait);
        doings_username = findViewById(R.id.doings_username);
        doings_time = findViewById(R.id.doings_time);
        doings_message = findViewById(R.id.doings_message);
        doings_replyNum = findViewById(R.id.doings_replynum);
        doings_commentlist = findViewById(R.id.doings_commentlist);
        emotionButton = findViewById(R.id.doingcommment_emotion);
        sendButton = findViewById(R.id.doingcomment_send);
        editText = findViewById(R.id.doingcomment_edit);
        emotionShow = findViewById(R.id.emotion_show);
        emotion_GridView = emotionShow
                .findViewById(R.id.grid_emotion);
        doingsCommentListAdapter = new DoingsCommentAdapter(mContext);
        setText();
    }

    private void setText() {
        String zhengze = "image[0-9]{2}|image[0-9]";
        Emotion emotion = new Emotion();
        doingsInfo.message = emotion.replace(doingsInfo.message);
        try {
            SpannableString spannableString = Expression.getExpressionString(
                    mContext, doingsInfo.message, zhengze);
            doings_message.setText(spannableString);
            handler.sendEmptyMessage(100);// 更新UI
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        doings_username.setText(doingsInfo.username);
        handler.sendEmptyMessage(100);
        doings_replyNum.setText(doingsInfo.replynum);
        handler.sendEmptyMessage(100);
        doings_time.setText(DateFormat.format("yyyy-MM-dd kk:mm",
                Long.parseLong(doingsInfo.dateline) * 1000));
        handler.sendEmptyMessage(100);
        doings_commentlist.setAdapter(doingsCommentListAdapter);
        doings_commentlist.setOnScrollListener(this);
        GitHubImageLoader.Instace(mContext).setCirclePic(doingsInfo.uid,
                doings_userPortrait);
        handler.sendEmptyMessage(0);
        setListener();

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
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)
                        && !isLastPage) {
                    handler.sendEmptyMessage(1);
                }
                break;
        }
    }

}
