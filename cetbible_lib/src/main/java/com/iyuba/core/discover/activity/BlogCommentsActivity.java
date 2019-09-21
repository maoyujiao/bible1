package com.iyuba.core.discover.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.discover.adapter.CommentsListAdapter;
import com.iyuba.core.discover.protocol.GetCommentRequest;
import com.iyuba.core.discover.protocol.GetCommentResponse;
import com.iyuba.core.discover.protocol.SendCommentRequest;
import com.iyuba.core.discover.protocol.SendCommentResponse;
import com.iyuba.core.discover.sqlite.mode.BlogComment;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.DataManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;

import java.util.ArrayList;
import java.util.List;

public class BlogCommentsActivity extends THSActivity {

    private Button backButton, send;
    private ListView commentsListView;
    private CommentsListAdapter commentsListAdapter;
    private EditText contentEdit;
    private List<BlogComment> voaComments = new ArrayList<BlogComment>();
    private BlogComment blogCommentForSend = new BlogComment();
    private String blogId, id, uid, title;
    private String type = "blogid";
    private int pageNum = 1;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 获取最新评论
                    ClientSession.Instance().asynGetResponse(

                            new GetCommentRequest(blogId, type, pageNum),
                            new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {
                                    GetCommentResponse responseGetComments = (GetCommentResponse) response;
                                    if (responseGetComments.blogComments != null
                                            && responseGetComments.blogComments
                                            .size() != 0) {
                                        voaComments
                                                .addAll(responseGetComments.blogComments);
                                        handler.sendEmptyMessage(1);
                                    }


                                }
                            }, null, null);
                    break;
                case 1: // 刷新列表
                    commentsListAdapter.notifyDataSetChanged();
                    break;
                case 2: // 发送评论

                    ClientSession.Instance().asynGetResponse(

                            new SendCommentRequest(
                                    uid,
                                    blogId, type,
                                    AccountManager.Instace(mContext).getUserName(),
                                    AccountManager.Instace(mContext).getId(),
                                    blogCommentForSend.message, title),
                            new IResponseReceiver() {


                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {

                                    SendCommentResponse responseSendComments = (SendCommentResponse) response;
                                    if (responseSendComments.isSendSuccess) {
                                        voaComments.add(0, blogCommentForSend);
                                        handler.sendEmptyMessage(3);
                                        handler.sendEmptyMessage(1);
                                    } else {


                                    }
                                }
                            }, null, null);
                    break;
                case 3:
                    contentEdit.setText("");
                    break;
            }
        }
    };
    private TextView comment_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentlist);
        blogId = DataManager.Instance().blogContent.blogid;
        uid = DataManager.Instance().blogContent.uid;
        title = DataManager.Instance().blogContent.subject;
        initWidget();
    }

    private void initWidget() {

        backButton = findViewById(R.id.comment_back_btn);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
        comment_subject = findViewById(R.id.comment_subject);
        comment_subject.setText(DataManager.Instance().blogContent.subject);
        commentsListView = findViewById(R.id.commentsListView);
        commentsListAdapter = new CommentsListAdapter(mContext, voaComments);
        commentsListView.setAdapter(commentsListAdapter);
        commentsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == 0) {

                } else {
                    contentEdit.findFocus();
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(contentEdit, 0);
                    contentEdit.setText("回复 " + voaComments.get(arg2 - 1).author + ":");
                    Editable etext = contentEdit.getText();
                    int position = etext.length();
                    Selection.setSelection(etext, position);
                    //item.grade=voaComments.get(arg2-1).grade;
                }
            }
        });
        contentEdit = findViewById(R.id.comments_content);
        contentEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {


                if (EditorInfo.IME_ACTION_SEND == actionId) {

                    String content = contentEdit.getText().toString();
                    if (content != null && content.length() != 0) {
                        blogCommentForSend = new BlogComment();
                        blogCommentForSend.author = AccountManager
                                .Instace(mContext).getUserName();
                        blogCommentForSend.authorid = AccountManager
                                .Instace(mContext).getId();
                        blogCommentForSend.message = content;
                        blogCommentForSend.dateline = String.valueOf(System
                                .currentTimeMillis() / 1000);
                        handler.sendEmptyMessage(2);
                    } else {
                        Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
                return true;
            }
        });
        send = findViewById(R.id.blogcomment_send);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String content = contentEdit.getText().toString();
                if (content != null && content.length() != 0) {
                    blogCommentForSend = new BlogComment();
                    blogCommentForSend.author = AccountManager
                            .Instace(mContext).getUserName();
                    blogCommentForSend.authorid = AccountManager
                            .Instace(mContext).getId();
                    blogCommentForSend.message = content;
                    blogCommentForSend.dateline = String.valueOf(System
                            .currentTimeMillis() / 1000);
                    handler.sendEmptyMessage(2);
                } else {
                    Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        handler.sendEmptyMessage(0);
    }

}
