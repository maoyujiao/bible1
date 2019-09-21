package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.protocol.UserEngLevelRequest;
import com.iyuba.core.me.protocol.UserEngLevelResponse;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.INetStateReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

public class IntelLearningActivity extends Activity {
    final int RESULT_OK = 1;
    Context mContext = this;
    String result;
    private ImageView img;
    private View goallevel;
    private View goalls;
    private View goalrw;
    private View nowlevel, nowls, nowrw;
    private TextView enlevel;
    private TextView lslevel;
    private TextView rwlevel;
    private TextView nowEnLevel, nowLsLevel, nowRwLevel;
    private TextView name;
    private Button commit, backBtn;
    private CustomDialog waitting;
    private ResponseUserDetailInfo userDetailInfo;
    private String[] levelsAll = new String[]{"初中", "高中", "三级", "四级", "六级",
            "专业八级", "托福", "雅思"};
    private String[] levelsPart = new String[]{"0级", "生疏", "一般", "熟练", "精通",
            "专家"};
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitting.show();
                    handler.sendEmptyMessage(1);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestUserDetailInfo(AccountManager
                                    .Instace(mContext).userId),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
                                    if (responseUserDetailInfo.result.equals("211")) {
                                        userDetailInfo = responseUserDetailInfo;
                                    }
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {
                                }
                            });
                    break;
                case 2:
                    waitting.dismiss();
                    setText();
                    break;
                case 3:
                    Toast.makeText(mContext, "上传成功", 1000).show();
                    break;
                default:
                    break;
            }
        }

    };
    private INetStateReceiver mNetStateReceiver = new INetStateReceiver() {

        @Override
        public void onStartConnect(BaseHttpRequest request, int rspCookie) {
        }

        @Override
        public void onConnected(BaseHttpRequest request, int rspCookie) {
        }

        @Override
        public void onStartSend(BaseHttpRequest request, int rspCookie,
                                int totalLen) {
        }

        @Override
        public void onSend(BaseHttpRequest request, int rspCookie, int len) {
        }

        @Override
        public void onSendFinish(BaseHttpRequest request, int rspCookie) {
        }

        @Override
        public void onStartRecv(BaseHttpRequest request, int rspCookie, int totalLen) {
        }

        @Override
        public void onRecv(BaseHttpRequest request, int rspCookie, int len) {
        }

        @Override
        public void onRecvFinish(BaseHttpRequest request, int rspCookie) {
        }

        @Override
        public void onNetError(BaseHttpRequest request, int rspCookie, ErrorResponse errorInfo) {
        }

        @Override
        public void onCancel(BaseHttpRequest request, int rspCookie) {
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowledge_level);
        mContext = this;

        enlevel = findViewById(R.id.chosen_enlevel);
        lslevel = findViewById(R.id.chosen_lslevel);
        rwlevel = findViewById(R.id.chosen_rwlevel);
        nowEnLevel = findViewById(R.id.chosen_now_enlevel);
        nowLsLevel = findViewById(R.id.chosen_now_lklevel);
        nowRwLevel = findViewById(R.id.chosen_now_rwlevel);
        name = findViewById(R.id.intel_username);
        img = findViewById(R.id.intel_me_pic);
        goallevel = findViewById(R.id.goal_eng_level);
        goalls = findViewById(R.id.goal_lire_level);
        goalrw = findViewById(R.id.goal_rewr_level);
        nowlevel = findViewById(R.id.intel_eng_level);
        nowls = findViewById(R.id.intel_lire_level);
        nowrw = findViewById(R.id.intel_rewr_level);
        commit = findViewById(R.id.button_level_commit);
        backBtn = findViewById(R.id.button_back);
        waitting = WaittingDialog.showDialog(mContext);

        GitHubImageLoader.Instace(mContext).setCirclePic(
                AccountManager.Instace(mContext).userId, img);
        name.setText("&  " + (TouristUtil.isTourist()
                ? AccountManager.Instace(mContext).getId() :
                AccountManager.Instace(mContext).getUserName())
                + "  &");

        commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String plevel = String.valueOf(findString((String) nowEnLevel
                        .getText()));
                String pls = String.valueOf(findString((String) nowLsLevel
                        .getText()));
                String prw = String.valueOf(findString((String) nowRwLevel
                        .getText()));
                String glevel = String.valueOf(findString((String) enlevel
                        .getText()));
                String greadLevel = String.valueOf(findString((String) rwlevel
                        .getText()));
                String gtalkLevel = String.valueOf(findString((String) lslevel
                        .getText()));
                if (plevel.equals("") || pls.equals("") || prw.equals("")
                        || glevel.equals("") || greadLevel.equals("")
                        || gtalkLevel.equals(""))
                    Toast.makeText(mContext, "请完善英语学习相关信息", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        Toast.makeText(mContext, "正在上传...", Toast.LENGTH_SHORT).show();
                        ClientSession.Instance().asynGetResponse(
                                new UserEngLevelRequest(AccountManager
                                        .Instace(mContext).userId, plevel, prw,
                                        pls, glevel, gtalkLevel, greadLevel),
                                new IResponseReceiver() {
                                    @Override
                                    public void onResponse(
                                            BaseHttpResponse response,
                                            BaseHttpRequest request,
                                            int rspCookie) {
                                        UserEngLevelResponse tr = (UserEngLevelResponse) response;
                                        if (tr.result == 221)
                                            handler.sendEmptyMessage(3);

                                    }
                                }, null, mNetStateReceiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        goallevel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "all");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        goalls.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "part");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        goalrw.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "part");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        nowlevel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "all");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        nowls.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "part");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        nowrw.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("levels", "part");
                intent.setClass(mContext, LevelPickActivity.class);
                startActivityForResult(intent, 6);
            }
        });
        handler.sendEmptyMessage(0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            result = data.getExtras().getString("result");// 得到新Activity
            // 关闭后返回的数据
            switch (requestCode) {
                case 1:
                    enlevel.setText(result);
                    break;
                case 2:
                    lslevel.setText(result);
                    break;
                case 3:
                    rwlevel.setText(result);
                    break;
                case 4:
                    nowEnLevel.setText(result);
                    break;
                case 5:
                    nowLsLevel.setText(result);
                    break;
                case 6:
                    nowRwLevel.setText(result);
                    break;
                default:
                    break;

            }
        }
    }

    public void setText() {
        try {
            nowEnLevel.setText(levelsAll[Integer.valueOf(userDetailInfo.editUserInfo.getPlevel()) - 1]);
            nowLsLevel.setText(levelsPart[Integer.valueOf(userDetailInfo.editUserInfo.getPtalklevel()) - 1]);
            nowRwLevel.setText(levelsPart[Integer.valueOf(userDetailInfo.editUserInfo.getPreadlevel()) - 1]);
            enlevel.setText(levelsAll[Integer.valueOf(userDetailInfo.editUserInfo.getGlevel()) - 1]);
            lslevel.setText(levelsPart[Integer.valueOf(userDetailInfo.editUserInfo.getGtalklevel()) - 1]);
            rwlevel.setText(levelsPart[Integer.valueOf(userDetailInfo.editUserInfo.getGreadlevel()) - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int findString(String level) {
        for (int i = 0; i < levelsAll.length; i++) {
            if (levelsAll[i].equals(level))
                return i + 1;
        }
        for (int i = 0; i < levelsPart.length; i++) {
            if (levelsPart[i].equals(level))
                return i + 1;
        }
        return -1;

    }

}
