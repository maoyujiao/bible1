package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.LsDetailAdapter;
import com.iyuba.core.me.protocol.ListenDetailRequest;
import com.iyuba.core.me.protocol.ListenDetailResponse;
import com.iyuba.core.me.sqlite.mode.ListenWordDetail;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IErrorReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;


public class ResultDetailActivity extends Activity {
    LsDetailAdapter lsDetailAdapter;
    private Context mContext;
    private ListView lsDetailListView;
    private List<ListenWordDetail> mList = new ArrayList<>();
    private View lsDetailFooter;
    private LayoutInflater inflater;
    private int page = 1;
    private String testMode = "1";
    private Button backBtn;

    private CustomDialog waitDialog;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    page = page + 1;
                    new UpdateLsDetailThread().start();
                    lsDetailFooter.setVisibility(View.GONE);
                    break;
                case 1:
                    Toast.makeText(mContext, "已经到底啦~~", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (mList.size() == 0) {
                        lsDetailFooter.setVisibility(View.INVISIBLE);
                        mHandler.sendEmptyMessage(1);
                    } else if (mList.size() < 20) {
                        lsDetailAdapter.addList((ArrayList<ListenWordDetail>) mList);
                        lsDetailAdapter.notifyDataSetChanged();
                        lsDetailFooter.setVisibility(View.INVISIBLE);
                    } else {
                        lsDetailAdapter.addList((ArrayList<ListenWordDetail>) mList);
                        lsDetailAdapter.notifyDataSetChanged();
                        lsDetailFooter.setVisibility(View.VISIBLE);
                    }
                    break;
                case 10:
                    waitDialog.dismiss();
                    CustomToast.showToast(mContext, "加载失败");
                    break;
                case 11:
                    waitDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intel_listen_detail);
        inflater = getLayoutInflater();
        mContext = this;
        Intent intent = getIntent();
        testMode = intent.getStringExtra("testMode");
        lsDetailListView = findViewById(R.id.detail_list);
        lsDetailFooter = inflater.inflate(R.layout.comment_footer, null);
        lsDetailAdapter = new LsDetailAdapter(mContext);
        lsDetailListView.addFooterView(lsDetailFooter);
        lsDetailFooter.setVisibility(View.INVISIBLE);
        lsDetailListView.setAdapter(lsDetailAdapter);
        backBtn = findViewById(R.id.button_back);

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        lsDetailListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            // 当comment不为空且comment.size()不为0且没有完全加载
                            mHandler.sendEmptyMessage(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        waitDialog = WaittingDialog.showDialog(ResultDetailActivity.this);

        waitDialog.show();
        new UpdateLsDetailThread().start();
    }

    private class UpdateLsDetailThread extends Thread {

        @Override
        public void run() {

            String uid = AccountManager.Instace(mContext).getId();
            String numPerPage = "20";

            ClientSession.Instance().asynGetResponse(
                    new ListenDetailRequest(uid, String.valueOf(page),
                            numPerPage, testMode), new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {

                            ListenDetailResponse tr = (ListenDetailResponse) response;

                            if (tr != null && tr.result.equals("1")) {

                                mList.clear();
                                mList.addAll(tr.mList);

                                mHandler.sendEmptyMessage(2);

                            } else {
                                mHandler.sendEmptyMessage(11);
                            }
                        }
                    }, new IErrorReceiver() {
                        @Override
                        public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                            mHandler.sendEmptyMessage(11);
                        }
                    });

        }
    }

}
