package com.iyuba.core.me.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.adapter.NewDoingsListAdapter;
import com.iyuba.core.me.protocol.RequestBasicUserInfo;
import com.iyuba.core.me.protocol.RequestCancelAttention;
import com.iyuba.core.me.protocol.RequestNewDoingsInfo;
import com.iyuba.core.me.protocol.ResponseBasicUserInfo;
import com.iyuba.core.me.protocol.ResponseCancelAttention;
import com.iyuba.core.me.protocol.ResponseNewDoingsInfo;
import com.iyuba.core.me.sqlite.mode.NewDoingsInfo;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestAddAttention;
import com.iyuba.core.protocol.message.ResponseAddAttention;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.teacher.activity.QuesDetailActivity;
import com.iyuba.core.teacher.activity.ShowLargePicActivity;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ReadBitmap;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;

/**
 * 个人主页
 *
 * @author chentong
 * @version 1.0
 * @para "userid" 当前用户userid（本人或其他人个人主页）
 */
public class PersonalHome extends BasisActivity implements OnScrollListener {
    private Context mContext;
    private Button backButton;
    private View info_background;// 换背景用
    private ImageView person_sex, person_pic;
    private Button detail, message, attent, fix;
    private TextView person_name, person_attention, person_fans, views;
    private String currentuid;
    private int currDoingPage;
    private CustomDialog waiting;
    private UserInfo userInfo;
    //	private ArrayList<DoingsInfo> doingsArrayList = new ArrayList<DoingsInfo>();
    private ArrayList<NewDoingsInfo> doingsArrayList = new ArrayList<NewDoingsInfo>();
    private ListView doingsList;
    private NewDoingsListAdapter doingsListAdapter;
    private Boolean isLastPage = false;
    private OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent;
            int id = v.getId();
            if (id == R.id.button_back) {
                onBackPressed();
            } else if (id == R.id.personal_image) {
                if (currentuid.equals(AccountManager.Instace(mContext).userId)) { // 如果当前为自己的个人主页
                    if (TouristUtil.isTourist()) { // 如果是临时账户
                        TouristUtil.showTouristInfoEditHint(mContext);
                    } else {
                        intent = new Intent();
                        intent.setClass(mContext, UpLoadImage.class);
                        intent.putExtra("reguid",
                                AccountManager.Instace(mContext).userId);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(mContext, ShowLargePicActivity.class);
                    intent.putExtra("pic", "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                            + currentuid + "&size=big");
                    mContext.startActivity(intent);
                }
            } else if (id == R.id.personal_attent) {
                if (TouristUtil.isTourist()) {
                    TouristUtil.showTouristHint(mContext);
                    return;
                }
                String state = attent.getText().toString();
                if (state.equals(mContext.getString(R.string.person_attention))) {
                    if (TextUtils.isEmpty(userInfo.username) || currentuid.equals(userInfo.username)) {
                        showShort("该用户为临时账户，无法添加关注");
                        return;
                    }
                    handler.sendEmptyMessage(30);
                } else {
                    showAlertDialog();
                }
            } else if (id == R.id.personal_detail) {
                intent = new Intent(mContext, UserDetailInfo.class);
                intent.putExtra("currentuid", currentuid);
                intent.putExtra("currentname", userInfo.username);
                startActivity(intent);
            } else if (id == R.id.personal_message) {
                intent = new Intent();
                intent.putExtra("friendid", currentuid);
                intent.putExtra("currentname", userInfo.username);
                intent.setClass(mContext, Chatting.class);
                startActivity(intent);
            } else if (id == R.id.personal_fix) {
                intent = new Intent(mContext, EditUserInfoActivity.class);
                startActivity(intent);
            } else if (id == R.id.fans_fans) {
                intent = new Intent();
                intent.putExtra("userid", currentuid);
                intent.setClass(mContext, FansCenter.class);
                startActivity(intent);
            } else if (id == R.id.fans_attention) {
                intent = new Intent();
                intent.putExtra("userid", currentuid);
                intent.setClass(mContext, AttentionCenter.class);
                startActivity(intent);
            } else {
            }
        }
    };
    private Handler binderAdapterDataHandler = new Handler();
    private Runnable binderAdapterDataRunnable = new Runnable() {
        public void run() {
            doingsListAdapter.clearList();
            doingsListAdapter.addList(doingsArrayList);
            doingsListAdapter.notifyDataSetChanged();
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currDoingPage = 1;
                    ExeProtocol.exe(
                            new RequestNewDoingsInfo(currentuid, currDoingPage),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseNewDoingsInfo responseDoingsInfo = (ResponseNewDoingsInfo) bhr;
                                    if (responseDoingsInfo.result.equals("391")) {
                                        if (responseDoingsInfo.counts.equals("0")) {
                                        } else {

                                            if (responseDoingsInfo.newDoingslist.size() > 0) {
                                                doingsArrayList.clear();
                                                doingsArrayList.addAll(responseDoingsInfo.newDoingslist);

                                                binderAdapterDataHandler.post(binderAdapterDataRunnable);

//											doingsListAdapter.clearList();
//											doingsListAdapter.addList(responseDoingsInfo.newDoingslist);
//											doingsListAdapter.notifyDataSetChanged();

                                            }

//										handler.sendEmptyMessage(10);
                                        }
                                    } else if (responseDoingsInfo.result.equals("392")) {
                                        handler.sendEmptyMessage(12);
                                        handler.sendEmptyMessage(10);
                                    } else {
                                        handler.sendEmptyMessage(9);
                                    }
                                    currDoingPage += 1;
//								handler.sendEmptyMessage(10);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(4);
                                }
                            });
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestNewDoingsInfo(currentuid, currDoingPage),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseNewDoingsInfo responseDoingsInfo = (ResponseNewDoingsInfo) bhr;
                                    if (responseDoingsInfo.result.equals("391")) {
                                        if (responseDoingsInfo.counts.equals("0")) {
                                        } else {

                                            if (responseDoingsInfo.newDoingslist.size() > 0) {

                                                doingsArrayList.addAll(responseDoingsInfo.newDoingslist);
                                                binderAdapterDataHandler.post(binderAdapterDataRunnable);
//											doingsListAdapter.addList(responseDoingsInfo.newDoingslist);
//											doingsListAdapter.notifyDataSetChanged();
                                            }
//										handler.sendEmptyMessage(10);
                                        }
                                    } else if (responseDoingsInfo.result.equals("392")) {
                                        handler.sendEmptyMessage(12);
                                        handler.sendEmptyMessage(10);
                                    } else {
                                        handler.sendEmptyMessage(9);
                                    }
                                    currDoingPage += 1;
//								handler.sendEmptyMessage(10);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(4);
                                }
                            });
                    break;
                case 2:
                    waiting.show();
                    break;
                case 3:
                    waiting.dismiss();
                    break;
                case 4:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 5:
                    CustomToast.showToast(mContext,
                            R.string.social_success_attention);
                    break;
                case 6:
                    CustomToast.showToast(mContext,
                            R.string.social_failed_attention);
                    break;
                case 7:
                    CustomToast.showToast(mContext,
                            R.string.social_success_cancle_attention);
                    break;
                case 8:
                    CustomToast.showToast(mContext,
                            R.string.social_failed_cancle_attention);
                    break;
                case 9:
                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 10:
                    doingsListAdapter.notifyDataSetChanged();
                    break;
                case 12:
                    CustomToast.showToast(mContext, R.string.action_no_more);
                    break;
                case 20:
                    ExeProtocol.exe(new RequestBasicUserInfo(currentuid,
                                    AccountManager.Instace(mContext).userId),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseBasicUserInfo responseBasicUserInfo = (ResponseBasicUserInfo) bhr;
                                    if (responseBasicUserInfo.result.equals("201")) {
                                        userInfo = responseBasicUserInfo.userInfo;
                                        handler.sendEmptyMessage(50);// 处理用户关系
                                    } else {
                                        handler.sendEmptyMessage(9);
                                    }
                                    handler.sendEmptyMessage(21);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(4);
                                }
                            });
                    break;
                case 21:
                    // 更新UI
                    views.setText(userInfo.views + mContext.getString(R.string.me_view));

                    if (TextUtils.isEmpty(userInfo.username) || "null".equals(userInfo.username)) {
                        userInfo.username = userInfo.uid;
                    }
                    person_name.setText(userInfo.username);
                    person_fans.setText(mContext
                            .getString(R.string.person_fans_count) + userInfo.follower);
                    person_attention.setText(mContext
                            .getString(R.string.person_attention_count) + userInfo.following);

                    if (userInfo.gender != null) {
                        if (userInfo.gender.equals("2")) {
                            person_sex.setImageBitmap(ReadBitmap.readBitmap(
                                    mContext, R.drawable.user_info_female));
                        } else {
                            person_sex.setImageBitmap(ReadBitmap.readBitmap(
                                    mContext, R.drawable.user_info_male));
                        }
                    } else {
                        person_sex.setImageBitmap(ReadBitmap.readBitmap(mContext,
                                R.drawable.user_info_male));
                    }
                    GitHubImageLoader.Instace(mContext).setCirclePic(currentuid,
                            person_pic);
                    break;
                case 30:
                    // 加关注
                    ExeProtocol.exe(
                            new RequestAddAttention(AccountManager
                                    .Instace(mContext).userId, currentuid),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseAddAttention res = (ResponseAddAttention) bhr;
                                    if (res.result.equals("500")) {
                                        handler.sendEmptyMessage(5);
                                        handler.sendEmptyMessage(0);// 修改为已关注
                                    } else {
                                        handler.sendEmptyMessage(6);
                                    }
                                    handler.sendEmptyMessage(31);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(4);
                                }
                            });
                    break;
                case 31:
                    // 关注成功后更新UI
                    attent.setText(R.string.person_attention_already);
                    break;
                case 40:
                    // 取消关注
                    ExeProtocol.exe(
                            new RequestCancelAttention(AccountManager
                                    .Instace(mContext).userId, currentuid),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseCancelAttention responseFansList = (ResponseCancelAttention) bhr;
                                    if (responseFansList.result.equals("510")) {
                                        handler.sendEmptyMessage(7);
                                        handler.sendEmptyMessage(0);// 修改为已关注
                                    } else {
                                        handler.sendEmptyMessage(8);
                                    }
                                    handler.sendEmptyMessage(41);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(4);
                                }

                            });
                    break;
                case 41:
                    // 取消关注成功后更新UI
                    attent.setText(R.string.person_attention);
                    break;
                case 50:
                    if (currentuid.equals(AccountManager.Instace(mContext).userId)) {
                    } else if (userInfo.relation.equals("0")) {
                        // 我没关注了这个人
                        attent.setText(R.string.person_attention);
                    } else if (userInfo.relation.equals("1")) {
                        // 他关注我
                        attent.setText(R.string.person_attention);
                    } else {
                        char[] relation = {userInfo.relation.charAt(0),
                                userInfo.relation.charAt(1),
                                userInfo.relation.charAt(2)};
                        if (relation[0] == '1' && relation[2] == '1') {
                            // 相互关注
                            attent.setText(R.string.person_attention_mutually);
                        } else if (relation[0] == '1' && relation[2] == '0') {
                            // 我关注了这个人
                            attent.setText(R.string.person_attention_already);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalhome);
        mContext = this;
        Intent intent = getIntent();
        currentuid = intent.getStringExtra("Uid");
        CrashApplication.getInstance().addActivity(this);
        initWidget();
        handler.sendEmptyMessage(0);
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentuid = SocialDataManager.Instance().userid;
        if (currentuid.equals(AccountManager.Instace(mContext).userId)) {
            fix.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            attent.setVisibility(View.GONE);
            userInfo = AccountManager.Instace(mContext).userInfo;
            handler.sendEmptyMessage(21);
            handler.sendEmptyMessage(50);
            handler.sendEmptyMessage(20);
        } else {
            fix.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            attent.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(20);
        }
    }

    private void initWidget() {
        backButton = findViewById(R.id.button_back);
        info_background = findViewById(R.id.personal_info);
        person_pic = findViewById(R.id.personal_image);
        person_sex = findViewById(R.id.name_sex);
        detail = findViewById(R.id.personal_detail);
        message = findViewById(R.id.personal_message);
        fix = findViewById(R.id.personal_fix);
        views = findViewById(R.id.personal_view);
        attent = findViewById(R.id.personal_attent);
        person_name = findViewById(R.id.name_text);
        person_fans = findViewById(R.id.fans_fans);
        person_attention = findViewById(R.id.fans_attention);
        doingsList = findViewById(R.id.personalhome_doingslist);
        waiting = WaittingDialog.showDialog(mContext);

        doingsListAdapter = new NewDoingsListAdapter(mContext);
//		doingsListAdapter = new NewDoingsListAdapter(mContext,doingsArrayList);


        doingsListAdapter.clearList();
//		doingsArrayList.clear();

        setClickListener();
    }

    private void setClickListener() {
        backButton.setOnClickListener(ocl);
        info_background.setOnClickListener(ocl);
        person_pic.setOnClickListener(ocl);
        detail.setOnClickListener(ocl);
        message.setOnClickListener(ocl);
        fix.setOnClickListener(ocl);
        attent.setOnClickListener(ocl);
        person_fans.setOnClickListener(ocl);
        person_attention.setOnClickListener(ocl);
        doingsList.setOnScrollListener(this);

        if (doingsListAdapter != null) {
            doingsList.setAdapter(doingsListAdapter);
        }
    }

    private void setAdapter() {
//		if (doingsListAdapter == null) {
//			doingsListAdapter = new NewDoingsListAdapter(mContext, doingsArrayList);
//			doingsList.setAdapter(doingsListAdapter);
//		} else {
//			handler.sendEmptyMessage(10);
//		}
//		doingsList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v,
//					int position, long id) {
//				if(doingsArrayList.get(position).idtype.equals("doid")){
//					Intent intent = new Intent(mContext, ReplyDoing.class);
//					intent.putExtra("doid", doingsArrayList.get(position).id);
//					startActivity(intent);
//				}else if(doingsArrayList.get(position).idtype.equals("questionid")){
//					Intent intent = new Intent();
//					intent.setClass(mContext, QuesDetailActivity.class);
//					intent.putExtra("qid",doingsArrayList.get(position).id);
//					startActivity(intent);
//				}else if(doingsArrayList.get(position).idtype.equals("picid")){
//					String  pic="";
//
//					pic=Constant.PIC_ABLUM__ADD+doingsArrayList.get(position).image;
//
//					Intent intent = new Intent(mContext, ShowLargePicActivity.class);
////					intent.putExtra("pic", pic.replace("-s.jpg", ".jpg"));
//					intent.putExtra("pic", pic);
//					mContext.startActivity(intent);
//				}
//
//			}
//		});
        doingsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (((NewDoingsInfo) doingsListAdapter.getItem(position)).idtype.equals("doid")) {
                    Intent intent = new Intent(mContext, ReplyDoing.class);
                    intent.putExtra("doid", ((NewDoingsInfo) doingsListAdapter.getItem(position)).id);
                    startActivity(intent);
                } else if (((NewDoingsInfo) doingsListAdapter.getItem(position)).idtype.equals("questionid")) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, QuesDetailActivity.class);
                    intent.putExtra("qid", ((NewDoingsInfo) doingsListAdapter.getItem(position)).id);
                    startActivity(intent);
                } else if (((NewDoingsInfo) doingsListAdapter.getItem(position)).idtype.equals("picid")) {
                    String pic = "";

                    pic = Constant.PIC_ABLUM__ADD + ((NewDoingsInfo) doingsListAdapter.getItem(position)).image;

                    Intent intent = new Intent(mContext, ShowLargePicActivity.class);
//					intent.putExtra("pic", pic.replace("-s.jpg", ".jpg"));
                    intent.putExtra("pic", pic);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    private void showAlertDialog() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.alert_title);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setMessage(mContext
                .getString(R.string.person_attention_relieve_alert));
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                mContext.getString(R.string.alert_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(40);
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                mContext.getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alert.show();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    if (!isLastPage) {
                        handler.sendEmptyMessage(1);
                    }
                }
                break;
        }
    }

}
