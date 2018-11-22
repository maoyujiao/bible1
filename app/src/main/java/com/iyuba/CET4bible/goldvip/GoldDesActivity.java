package com.iyuba.CET4bible.goldvip;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.TouristUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by mingyu on 2016/10/29.
 */
public class GoldDesActivity extends BaseActivity implements View.OnClickListener {
    private ImageView vip_99;
    private ImageView vip_199;
    private ImageView vip1_99;
    private ImageView vip1_199;
    private ImageView vip_299;
    private ImageView vip1_299;
    private String out_trade_no;
    private String QQSupport = "2326344291";
    private TextView tv_title;
    //    private int items_purchase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golddes);
//        Intent intent = getIntent();
//        price = intent.getStringExtra("price");
//        subject = intent.getStringExtra("subject");
//        body = intent.getStringExtra("body");
//        out_trade_no = intent.getStringExtra("out_trade_no");
////        items_purchase = intent.getIntExtra("items_purchase", -1);
        findView();

        ImageView header = findView(R.id.iv_header);
        ImageView desc = findView(R.id.iv_goldvip1);
        if (!Constant.APP_CONSTANT.isEnglish()) {
            String type = Constant.APP_CONSTANT.TYPE();
            if ("1".equals(type)) {
                header.setImageResource(R.drawable.vip_header);
                desc.setImageResource(R.drawable.cet_des);
            } else if ("2".equals(type)) {
                header.setImageResource(R.drawable.vip_header2);
                desc.setImageResource(R.drawable.cet_des2);
            } else {
                header.setImageResource(R.drawable.vip_header3);
                desc.setImageResource(R.drawable.cet_des3);
            }
        }
    }

    private void findView() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                url = "mqqwpa://im/chat?chat_type=wpa&uin=" + QQSupport + "&version=1";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(GoldDesActivity.this, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
//        ll_golddes = (ImageView) findViewById(R.id.iv_golddes);
//        if(items_purchase==0){
//            ll_golddes.setBackgroundResource(R.drawable.cet4des);
//        }else if(items_purchase==1){
//            ll_golddes.setBackgroundResource(R.drawable.cet6des);
//        }
//        iv_goldphone = (ImageView) findViewById(R.id.iv_goldphone);
        vip_99 = findViewById(R.id.vip_99);
        vip_199 = findViewById(R.id.vip_199);
        vip1_99 = findViewById(R.id.vip1_99);
        vip1_199 = findViewById(R.id.vip1_199);
        vip_299 = findViewById(R.id.vip_299);
        vip1_299 = findViewById(R.id.vip1_299);
        vip_99.setOnClickListener(this);
        vip_199.setOnClickListener(this);
        vip1_99.setOnClickListener(this);
        vip1_199.setOnClickListener(this);
        vip_299.setOnClickListener(this);
        vip1_299.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        String price;
        String subject;
        String amount;
        String body;
        switch (view.getId()) {
            case R.id.vip_299:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "黄金会员";
                    price = "299";
                    amount = "6";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "黄金会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            case R.id.vip1_299:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "黄金会员";
                    price = "299";
                    amount = "6";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "黄金会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            case R.id.vip_199:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "黄金会员";
                    price = "199";
                    amount = "3";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "黄金会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            case R.id.vip1_199:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "黄金会员";
                    price = "199";
                    amount = "3";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "黄金会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            case R.id.vip_99:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "冲刺会员";
                    price = "99";
                    amount = "1";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "冲刺会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            case R.id.vip1_99:
                if (!AccountManager.Instace(this).checkUserLogin()) {
                    intent.setClass(GoldDesActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    subject = "冲刺会员";
                    price = "99";
                    amount = "1";
                    body = Constant.APP + "-" + "花费" + price + "元购买" + Constant.APP + "冲刺会员";
                    intent.setClass(this, PayOrderActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("out_trade_no", getOutTradeNo());
                    intent.putExtra("subject", subject);
                    intent.putExtra("body", body);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
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
