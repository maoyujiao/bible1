package com.iyuba.core.me.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.activity.Web;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.MyGridAdapter;
import com.iyuba.core.me.pay.BuyIyubiActivity;
import com.iyuba.core.me.pay.PayOrderActivity;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.MyGridView;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.headlinelibrary.util.CircleImageTransform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class VipCenter extends BasisActivity {
    private TextView tv_username;
    private TextView tv_iyucoin;
    private MyGridView gv_tequan;
    private String username;
    private String iyubi;
    private CustomDialog wettingDialog;
    private CheckBox quarter;
    private CheckBox month;
    private CheckBox half_year;
    private CheckBox year;
    private CheckBox threeyear;
    //    private RelativeLayout lifelong;
    private ImageView iv_back;

    private double price;
    private CheckBox detail;
    private CheckBox detail2;
    private CheckBox detail3;
    private CheckBox detail4;
    private CheckBox detail5;
    private CheckBox detail6;
    private TabHost th;
    private TextView localVip;
    private TextView goldVip;

    private TextView date;
    private TextView userName;
    private CircleImageView userIcon;

    private Button go_buy;

    TextView state;
    TextView deadline;
    TextView tv_vip_detail;

    RelativeLayout rl2, rl3, rl4, rl5, rl6; // 全站会员
    RelativeLayout rl7, rl8, rl9, rl10; // 黄金会员
    RelativeLayout rl11, rl12; // 本应用会员
    TextView priceGold1, priceGold3,priceGold6,priceGold12 ;
    TextView allVip1, allVip3,allVip6,allVip12,allVip36 ;
    TextView thisVip1, thisVip2 ;
    Intent intent;

    public static void start(Context context ,boolean toGold){
        Intent intent = new Intent(context,VipCenter.class);
        intent.putExtra("toGold",toGold );
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_center_new);
        CrashApplication.getInstance().addActivity(this);

//        if (!AccountManager.Instace(mContext).checkUserLogin()) {
//            startActivity(new Intent(mContext, Login.class));
//            finish();
//            return;
//        }

        userIcon = findViewById(R.id.user_icon);
        userName = findViewById(R.id.user_name);
        date = findViewById(R.id.expiredate);
        rl2 = findViewById(R.id.rl2);
        rl3 = findViewById(R.id.rl3);
        rl4 = findViewById(R.id.rl4);
        rl5 = findViewById(R.id.rl5);
        rl6 = findViewById(R.id.rl6);
        rl7 = findViewById(R.id.rl7);
        rl8 = findViewById(R.id.rl8);
        rl9 = findViewById(R.id.rl9);
        rl10 = findViewById(R.id.rl10);
        rl11 = findViewById(R.id.rl11);
        rl12 = findViewById(R.id.rl12);
        allVip1 = findViewById(R.id.tv3_price);
        allVip3 = findViewById(R.id.tv4_price);
        allVip6 = findViewById(R.id.tv5_price);
        allVip12 = findViewById(R.id.tv6_price);
        allVip36 = findViewById(R.id.tv8_price);
        thisVip1 = findViewById(R.id.tv3_price);
        thisVip2 = findViewById(R.id.tv3_price);
        rl2.setOnClickListener(ocl);
        rl3.setOnClickListener(ocl);
        rl4.setOnClickListener(ocl);
        rl5.setOnClickListener(ocl);
        rl6.setOnClickListener(ocl);
        rl7.setOnClickListener(ocl);
        rl8.setOnClickListener(ocl);
        rl9.setOnClickListener(ocl);
        rl10.setOnClickListener(ocl);
        rl11.setOnClickListener(ocl);
        rl12.setOnClickListener(ocl);


        username = AccountManager.Instace(mContext).userName;
        if (AccountManager.Instace(mContext).userInfo!=null){
            iyubi = AccountManager.Instace(mContext).userInfo.iyubi;
        }else {
            iyubi = "0";
        }
        initView();
        if (getIntent().getBooleanExtra("toGold", false)){
            th.getTabWidget().getChildAt(0).setBackgroundColor(0x00FFFFFF);
            th.getTabWidget().getChildAt(1).setBackgroundColor(0x00FFFFFF);
            th.getTabWidget().getChildAt(2).setBackgroundColor(0xFFFDDA94);
            setVisibleGroup(3);
        }
        state = findViewById(R.id.buy_state);
        deadline = findViewById(R.id.buy_deadline);
        go_buy = findViewById(R.id.go_buy);
        go_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null) {
                    if (!AccountManager.Instace(mContext).checkUserLogin()){
                        ToastUtil.showLongToast(mContext,"请注册正式账号后操作");
                        return ;
                    }
                    if (TouristUtil.isTourist()){
                        ToastUtil.showLongToast(mContext,"请注册正式账号后操作");
                    }else{
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVip();
    }

    @SuppressLint("SetTextI18n")
    public void setVip() {
        int isvip = AccountManager.Instace(mContext).getVipStatus();
        if (isvip > 0) {
            state.setText("VIP");
            deadline.setText(AccountManager.Instace(mContext).userInfo.deadline);
            date.setText(AccountManager.Instace(mContext).userInfo.deadline);
        } else {
            if (TouristUtil.isTourist()) {
                state.setText("临时用户");
            } else {
                state.setText(R.string.person_common_user);
            }
            date.setText(R.string.person_not_vip);
        }
        tv_iyucoin.setText(iyubi);
        tv_username.setText(AccountManager.Instace(mContext).getUserName().equals("0")?"未登录":AccountManager.Instace(mContext).getUserName());
        userName.setText(AccountManager.Instace(mContext).getUserName().equals("0")?"未登录":AccountManager.Instace(mContext).getUserName());
        String userid = AccountManager.Instace(mContext).userId;
        Glide.with(mContext).load("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + userid + "&size=middle").asBitmap().placeholder(R.drawable.defaultavatar)
                .fitCenter()
                .transform(new CircleImageTransform(mContext))
                .into(new BitmapImageViewTarget(userIcon) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        userIcon.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        wettingDialog = WaittingDialog.showDialog(mContext);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VipCenter.this.finish();
            }
        });
        tv_username = findViewById(R.id.tv_username);
        if (TextUtils.isEmpty(username) || "null".equals(username)) {
            username = AccountManager.Instace(mContext).userId;
        }
        tv_username.setText(username);
        tv_iyucoin = findViewById(R.id.tv_iyucoin);
        tv_iyucoin.setText(iyubi);
        btn_buyiyubi = findViewById(R.id.btn_buyiyuba);
        btn_buyiyubi.setOnClickListener(ocl);

        month = findViewById(R.id.btn_buyapp1);
        quarter = findViewById(R.id.btn_buyapp2);
        half_year = findViewById(R.id.btn_buyapp3);
        year = findViewById(R.id.btn_buyapp4);
        threeyear = findViewById(R.id.btn_buyapp5);
        detail = findViewById(R.id.btn_detail);
        detail2 = findViewById(R.id.btn_detail2);
        detail3 = findViewById(R.id.btn_detail3);
        detail4 = findViewById(R.id.btn_detail4);
        detail5 = findViewById(R.id.btn_detail5);
        detail6 = findViewById(R.id.btn_detail6);
        th = findViewById(R.id.tabhost);
        gv_tequan = findViewById(R.id.gv_tequan);
        localVip = findViewById(R.id.view2);
        goldVip = findViewById(R.id.view3);
        TextView tv9 = findViewById(R.id.tv9);
        TextView tv10 = findViewById(R.id.tv10);
        TextView tv11 = findViewById(R.id.tv11);
        TextView tv12 = findViewById(R.id.tv12);
        TextView tv13 = findViewById(R.id.tv13);
        TextView tv14 = findViewById(R.id.tv14);
        TextView tv13_price = findViewById(R.id.tv13_price);
        TextView tv14_price = findViewById(R.id.tv14_price);

        priceGold1 = findViewById(R.id.tv9_price);
        priceGold3 = findViewById(R.id.tv10_price);
        priceGold6 = findViewById(R.id.tv11_price);
        priceGold12 = findViewById(R.id.tv12_price);
        setGoldPrice();
        if (Constant.APP_CONSTANT.TYPE().equals("4")) {
            tv9.setText("四级黄金会员1个月");
            tv10.setText("四级黄金会员3个月");
            tv11.setText("四级黄金会员6个月");
            tv12.setText("四级黄金会员12个月");
            tv13.setText("本应用会员12个月");
            tv14.setText("本应用永久会员");
        } else {
            tv9.setText("六级黄金会员1个月");
            tv10.setText("六级黄金会员3个月");
            tv11.setText("六级黄金会员6个月");
            tv12.setText("六级黄金会员12个月");
            tv13.setText("本应用会员12个月");
            tv14.setText("本应用永久会员");
        }
        tv13_price.setText(String.format("¥%s",getResources().getStringArray(R.array.benyingyong_vip_price)[0]));
        tv14_price.setText(String.format("¥%s",getResources().getStringArray(R.array.benyingyong_vip_price)[1]));

        month.setOnClickListener(ocl);
        quarter.setOnClickListener(ocl);
        half_year.setOnClickListener(ocl);
        year.setOnClickListener(ocl);
        threeyear.setOnClickListener(ocl);

        detail.setOnClickListener(ocl);
        detail2.setOnClickListener(ocl);
        detail3.setOnClickListener(ocl);
        detail4.setOnClickListener(ocl);
        detail5.setOnClickListener(ocl);
        detail6.setOnClickListener(ocl);

        th.setup();
        th.addTab(th.newTabSpec("tab1").setIndicator(composeLayout("全站VIP\n(不含微课)", R.drawable.all_vip)).setContent(R.id.gv_tequan));
        th.addTab(th.newTabSpec("tab2").setIndicator(composeLayout("本应用VIP\n(不含微课)", R.drawable.forever_vip)).setContent(R.id.view2));
        th.addTab(th.newTabSpec("tab3").setIndicator(composeLayout("黄金会员\n(全部微课)", R.drawable.gold_vip1)).setContent(R.id.view3));

        final MyGridAdapter adapter = new MyGridAdapter(mContext);
        gv_tequan.setAdapter(adapter);
        gv_tequan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String hint = adapter.getHint(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(hint);
                builder.setTitle("权限介绍");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
        localVip.setText(
                "1. 去除广告，专注学习\n" +
                        "2. 显示尊贵标识\n" +
                        "3. 可选择自由调节语速\n" +
                        "4. 享受高速通道、无限下载\n" +
                        "5. 可进行智慧化评测,查看不同试题解析\n" +
                        "6. 解锁单词闯关全部关卡\n" +
                        "7. 真题新闻PDF智能导出\n"+
                        "8. 新闻语音评测全部开放(非会员只能评测5句)\n"+
                        "本应用VIP仅限Android端本应用使用(不含微课)");
        if (Constant.APP_CONSTANT.TYPE() .equals("4") ) {
            goldVip.setText(
                    "1. 免费送四级真题+英语四级词根词汇速记(电子书)\n" +
                            "2. 微课：四六级名师陈苏灵、李尚龙、石雷鹏、尹延、章敏等；VOA英语Lisa博士、Rachel美语发音；" +
                            "Alex博士BBC英式发音；新概念英语全4册、托福、雅思、考研、学位英语、中职英语等名师讲解所有课程全部免费学习；\n" +
                            "3. 尊享个性化学习：历年真题详解一键解锁\n" +
                            "4. 智慧化学习系统：评测系统+学习刷题系统开放\n" +
                            "5. 黄金会员训练营全开通\n"+
                            "6. 享受爱语吧全站VIP特权。");
        } else {
            goldVip.setText(
                    "1. 免费送六级真题+英语六级词根词汇速记(电子书)\n" +
                            "2. 微课：四六级名师陈苏灵、李尚龙、石雷鹏、尹延、章敏等；VOA英语Lisa博士、Rachel美语发音；" +
                            "Alex博士BBC英式发音；新概念英语全4册、托福、雅思、考研、学位英语、中职英语等名师讲解所有课程全部免费学习；\n" +
                            "3. 尊享个性化学习：历年真题详解一键解锁\n" +
                            "4. 智慧化学习系统：评测系统+学习刷题系统开放\n" +
                            "5. 黄金会员训练营全开通\n"+
                            "6. 享受爱语吧全站VIP特权。");
        }
        setVisibleGroup(1);

        View view = th.getTabWidget().getChildAt(0);

        view.setBackgroundColor(0xFFFDDA94);

        th.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.e("tabid", tabId);
                View view;
                switch (tabId) {
                    case "tab1":
                        view = th.getTabWidget().getChildAt(0);
                        view.setBackgroundColor(0xFFFDDA94);
                        view = th.getTabWidget().getChildAt(1);
                        view.setBackgroundColor(0x00FFFFFF);
                        view = th.getTabWidget().getChildAt(2);
                        view.setBackgroundColor(0x00FFFFFF);
                        setVisibleGroup(1);
                        break;
                    case "tab2":
                        view = th.getTabWidget().getChildAt(0);
                        view.setBackgroundColor(0x00FFFFFF);
                        view = th.getTabWidget().getChildAt(1);
                        view.setBackgroundColor(0xFFFDDA94);
                        view = th.getTabWidget().getChildAt(2);
                        view.setBackgroundColor(0x00FFFFFF);
                        setVisibleGroup(2);

                        break;
                    case "tab3":
                        view = th.getTabWidget().getChildAt(0);
                        view.setBackgroundColor(0x00FFFFFF);
                        view = th.getTabWidget().getChildAt(1);
                        view.setBackgroundColor(0x00FFFFFF);
                        view = th.getTabWidget().getChildAt(2);
                        view.setBackgroundColor(0xFFFDDA94);
                        setVisibleGroup(3);
                        break;
                }
            }
        });


        tv_vip_detail = findView(R.id.tv_vip_detail);
        tv_vip_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Web.class);
                intent.putExtra("url", "http://vip." + "iyuba.cn/" + "vip/vip.html?"
                        + "&uid=" + AccountManager.Instace(mContext).getId()
                        + "&sign=" + MD5.getMD5ofStr("iyuba" + AccountManager.Instace(mContext).getId() + "camstory")
                        + "&username=" + AccountManager.Instace(mContext).getUserName()
                        + "&platform=android&appid="
                        + Constant.APPID);
                startActivity(intent);
            }
        });
    }

    private void setGoldPrice() {
        priceGold1.setText(String.format("¥%s",getResources().getStringArray(R.array.gold_vip_price)[0]));
        priceGold3.setText(String.format("¥%s",getResources().getStringArray(R.array.gold_vip_price)[1]));
        priceGold6.setText(String.format("¥%s",getResources().getStringArray(R.array.gold_vip_price)[2]));
        priceGold12.setText(String.format("¥%s",getResources().getStringArray(R.array.gold_vip_price)[3]));
    }


    private void setVisibleGroup(int i) {
        clearCheckBox();
        rl2.setVisibility(View.GONE);
        rl3.setVisibility(View.GONE);
        rl4.setVisibility(View.GONE);
        rl5.setVisibility(View.GONE);
        rl6.setVisibility(View.GONE);
        rl7.setVisibility(View.GONE);
        rl8.setVisibility(View.GONE);
        rl9.setVisibility(View.GONE);
        rl10.setVisibility(View.GONE);
        rl11.setVisibility(View.GONE);
        rl12.setVisibility(View.GONE);
        switch (i) {
            case 1:
                rl2.setVisibility(View.VISIBLE);
                rl3.setVisibility(View.VISIBLE);
                rl4.setVisibility(View.VISIBLE);
                rl5.setVisibility(View.VISIBLE);
                rl6.setVisibility(View.VISIBLE);
                handler.obtainMessage(0, 1, 0).sendToTarget();
                month.setChecked(true);
                break;

            case 2:
                rl11.setVisibility(View.VISIBLE);
                rl12.setVisibility(View.VISIBLE);
                buyVip(99);
                detail5.setChecked(true);
                break;

            case 3:
                rl7.setVisibility(View.VISIBLE);
                rl8.setVisibility(View.VISIBLE);
                rl9.setVisibility(View.VISIBLE);
                rl10.setVisibility(View.VISIBLE);
                buySprintOrGoldVip(1);
                detail.setChecked(true);
                break;

            default:
                break;
        }
    }

    public View composeLayout(String s, int i) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(this);
        iv.setImageResource(i);
        iv.setAdjustViewBounds(true);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp2px(mContext, 20), 0, 20);
        lp.gravity = Gravity.CENTER;
        layout.addView(iv, lp);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
//        tv.setSingleLine(true);
        tv.setText(s);
        tv.setTextColor(0xFF598aad);
        tv.setTextSize(14);
        LinearLayout.LayoutParams lpo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpo.setMargins(0, 0, 0, dp2px(mContext, 20));
        layout.addView(tv, lpo);
        return layout;
    }

    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void buyVip(int month) {
        intent = new Intent(mContext, PayOrderActivity.class);
        price = getSpend(month);
        intent.putExtra("type", month);
        intent.putExtra("out_trade_no", getOutTradeNo());
        if (month == 0) { //本应用永久vip
            price = Double.parseDouble(getResources().getStringArray(R.array.benyingyong_vip_price)[1]);
            intent.putExtra("body", "花费" + price + "元购买永久vip");
            intent.putExtra("subject", "永久vip");
        } else if (month == 99) { //本应用12个月vip
            price = Double.parseDouble(getResources().getStringArray(R.array.benyingyong_vip_price)[0]);
            intent.putExtra("body", "花费" + 99 + "元购买本应用vip12个月");
            intent.putExtra("subject", "本应用vip");
            intent.putExtra("type", 12);
        } else {
            intent.putExtra("body", "花费" + price + "元购买全站vip");
            intent.putExtra("subject", "全站vip");
        }
        intent.putExtra("price", price + "");  //价格
    }


    public double getSpend(int month) {
        double result = 199;
        switch (month) {
            case 1:
                result = Double.parseDouble(getResources().getStringArray(R.array.all_vip_price)[0]);
                break;
            case 3:
                result = Double.parseDouble(getResources().getStringArray(R.array.all_vip_price)[1]);
                break;
            case 6:
                result = Double.parseDouble(getResources().getStringArray(R.array.all_vip_price)[2]);
                break;
            case 12:
                result = Double.parseDouble(getResources().getStringArray(R.array.all_vip_price)[3]);
                break;
            case 36:
                result = Double.parseDouble(getResources().getStringArray(R.array.all_vip_price)[4]);
                break;
        }
        return result;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Dialog dialog;
            switch (msg.what) {
                case 0:
                    final int month = msg.arg1;
                    buyVip(month);
                    break;
                case 1:
                    wettingDialog.dismiss();
                    dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.alert_title)
                            .setMessage(R.string.alert_recharge_content)
                            .setPositiveButton(R.string.alert_btn_recharge,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            buyIyubi();
                                        }
                                    })
                            .setNeutralButton(R.string.alert_btn_cancel, null)
                            .create();
                    dialog.show();// 如果要显示对话框，一定要加上这句
                    break;
                case 2:
                    wettingDialog.dismiss();
                    CustomToast.showToast(mContext, R.string.buy_success);
                    tv_iyucoin.setText(msg.obj.toString());
                    break;
                case 3:
                    wettingDialog.show();
                    break;
                case 4:
                    wettingDialog.dismiss();
                    dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.alert_title)
                            .setMessage(R.string.alert_all_life_vip)
                            .setPositiveButton(R.string.alert_btn_recharge,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            buyIyubi();
                                        }
                                    })
                            .setNeutralButton(R.string.alert_btn_cancel, null)
                            .create();
                    dialog.show();// 如果要显示对话框，一定要加上这句
                    break;
                default:
                    break;
            }
        }
    };
    private OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            /*
             * if (AccountManager.Instance(mContext).userInfo.deadline
             * .equals("终身VIP")) { handler.sendEmptyMessage(4); } else
             */
            clearCheckBox();
            if (v == month || v == rl2) {
                handler.obtainMessage(0, 1, 0).sendToTarget();
                month.setChecked(true);
            } else if (v == quarter || v == rl3 ) {
                handler.obtainMessage(0, 3, 0).sendToTarget();
                quarter.setChecked(true);

            } else if (v == half_year || v == rl4 ) {
                handler.obtainMessage(0, 6, 0).sendToTarget();
                half_year.setChecked(true);

            } else if (v == year || v == rl5 ) {
                handler.obtainMessage(0, 12, 0).sendToTarget();
                year.setChecked(true);

            } else if (v == threeyear || v == rl6 ) {
                handler.obtainMessage(0, 36, 0).sendToTarget();
                threeyear.setChecked(true);

            } else if (v == detail || v == rl7  ) {
                buySprintOrGoldVip(1);
                detail.setChecked(true);

            } else if (v == detail2 || v == rl8 ) {
                buySprintOrGoldVip(3);
                detail2.setChecked(true);

            } else if (v == detail3 || v == rl9 ) {
                buySprintOrGoldVip(6);
                detail3.setChecked(true);

            } else if (v == detail4 || v == rl10) {
                buySprintOrGoldVip(12);
                detail4.setChecked(true);

            } else if (v == detail5 || v == rl11 ) {
                buyVip(99);
                detail5.setChecked(true);

            } else if (v == detail6 || v == rl12 ) {
                handler.obtainMessage(0, 0, 0).sendToTarget();
                detail6.setChecked(true);

            } else if (v == btn_buyiyubi && !TouristUtil.isTourist()) {
                buyIyubi();
            } else if (v == btn_buyiyubi && TouristUtil.isTourist()) {
                Toast.makeText(mContext, "请注册正式用户购买爱语币", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, Login.class));
                finish();
            }

        }
    };

    private void clearCheckBox() {
        quarter.setChecked(false);
        month.setChecked(false);
        half_year.setChecked(false);
        year.setChecked(false);
        threeyear.setChecked(false);

        detail.setChecked(false);
        detail2.setChecked(false);
        detail3.setChecked(false);
        detail4.setChecked(false);
        detail5.setChecked(false);
        detail6.setChecked(false);
    }

    private void buySprintOrGoldVip(int month) {
        String price = "98";
        String subject;
        String body;
        subject = "黄金会员";

        int amount = 1;
        switch (month) {
            case 1:
                price = getResources().getStringArray(R.array.gold_vip_price)[0];
                amount = 1;
                break;
            case 3:
                price = getResources().getStringArray(R.array.gold_vip_price)[1];
                amount = 3;
                break;
            case 6:
                price = getResources().getStringArray(R.array.gold_vip_price)[2];
                amount = 6;
                break;
            case 12:
                price = getResources().getStringArray(R.array.gold_vip_price)[3];
                amount = 12;
                break;
        }
//        if (TouristUtil.isTourist()) {
//            ToastUtil.showLongToast(this, "请注册正式账号后操作");
//            return;
//        }
        intent = new Intent();
        body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "黄金会员";
        intent.setClass(mContext, PayOrderActivity.class);
        intent.putExtra("type", amount);
        intent.putExtra("isGold", true);
        intent.putExtra("out_trade_no", getOutTradeNo());
        intent.putExtra("subject", subject);
        intent.putExtra("body", body);
        intent.putExtra("price", price);
    }

    private Button btn_buyiyubi;

    private void buyIyubi() {
        Intent intent = new Intent();
        intent.setClass(mContext, BuyIyubiActivity.class);
        intent.putExtra("url", "http://app.iyuba.cn/" + "wap/index.jsp?uid="
                + AccountManager.Instace(mContext).userId + "&appid="
                + Constant.APPID);
        intent.putExtra("title", Constant.AppName);
        startActivity(intent);
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + Math.abs(r.nextInt());
        key = key.substring(0, 15);
        return key;
    }
}

