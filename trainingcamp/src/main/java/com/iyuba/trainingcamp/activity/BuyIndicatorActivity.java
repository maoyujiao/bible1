package com.iyuba.trainingcamp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AddCreditModule;
import com.iyuba.trainingcamp.event.PayEvent;
import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.utils.SP;
import com.iyuba.trainingcamp.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 */
public class BuyIndicatorActivity extends BaseActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    boolean showBuy;
    String flag;
    TextView haveATry, buy ;
    ImageView img;

    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        String header = "http://static2.iyuba.com/golden/img/buy/";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent().getExtras().getBoolean("flag", true)) {
            showBuy = true;
        } else {
            showBuy = false;
        }

        setContentView(R.layout.trainingcamp_gold_buy_pic);
        img = findViewById(R.id.img);
        haveATry = (TextView)findViewById(R.id.have_a_try);
        buy = (TextView)findViewById(R.id.buy);
        if ((Boolean)SP.get(context,"has_tried",false)){
            haveATry.setVisibility(View.INVISIBLE);
        }else {
            haveATry.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

        try {
            flag = Base64.encodeToString(
                    URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!showBuy) {
            haveATry.setVisibility(View.GONE);
            buy.setVisibility(View.GONE);
        }

        Glide.with(context).load(header+GoldApp.getApp(context).getLessonType()+"Introduce.png").placeholder(R.drawable.trainingcamp_adss).into(img);
        haveATry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(GoldApp.getApp(context).userId)){
                    ToastUtil.showLongToast(context,"请登录后操作");
                    return;
                }
                Call<AddCreditModule> call = VipRequestFactory.getupdateScoreApi()
                        .addCredit(GoldApp.getApp(context).getUserId(), "52", flag,"1");
                call.enqueue(new Callback<AddCreditModule>() {
                    @Override
                    public void onResponse(Call<AddCreditModule> call, Response<AddCreditModule> response) {
                        if (response.body().result == 200) {
//                                GoldApp.getApp(context).init(AccountManager.Instace(context).userId,
//                                        AccountManager.Instace(context).userName,Constant.mListen);
                            Intent intent = new Intent(context, GoldActivity.class);
                            intent.putExtra("in_trial_mode",true);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            SP.put(context,"has_tried",true);
                            ToastUtil.showToast(context, "积分扣除成功");

                        } else {
                            ToastUtil.showToast(context, "积分扣除失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<AddCreditModule> call, Throwable t) {
                        ToastUtil.showToast(context, "积分扣除失败");
                    }
                });


            }
        });

        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                String price;
                String subject;
                int amount;
                String body;
                subject = "冲刺会员";
                price = "99";
                amount = 1;
                body = "花费" + price + "元购买冲刺会员";

                if(TextUtils.isEmpty(GoldApp.getApp(context).userId)){
                    ToastUtil.showLongToast(context,"请登录后操作");
                    return;
                }
                PayEvent event = new PayEvent(price, subject, amount, body, getOutTradeNo());
                EventBus.getDefault().post(event);

            }
        });
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

//    private void showPayDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View dialogVIew = LayoutInflater.from(context).inflate(R.layout.trainingcamp_payorder,null);
//
//        builder.setView(dialogVIew);
//        builder.setPositiveButton("确认支付", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setNegativeButton("取消",null);
//    }

}
