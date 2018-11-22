package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.event.MainMicroClassEvent;
import com.iyuba.CET4bible.goldvip.GoldDesActivity;
import com.iyuba.CET4bible.sqlite.mode.ImageData;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.activity.Web;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<LinearLayout> mViews;
    private List<ImageData> mData;

    public CardPagerAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(ImageData data) {
        mViews.add(null);
        mData.add(data);
    }

    public void clear() {
        mViews.clear();
        mData.clear();
    }


    @Override
    public int getCount() {
//        return mData.size();
        return mData.size() <= 1 ? mData.size() : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        if (mData.size()== 0){
            return view;
        }
        bind(mData.get(position % mData.size()), view);

        LinearLayout cardView = view.findViewById(R.id.cardView);

        mViews.set(position % mData.size(), cardView);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position % mData.size(), null);
    }

    private void bind(final ImageData data, View view) {
        ImageView iv_card = view.findViewById(R.id.iv_card);
//        ImageLoader.getInstance().displayImage("http://app.iyuba.com/dev/" + data.pic, iv_card);
        Glide.with(mContext).load("http://dev.iyuba.com/" + data.pic)
                .placeholder(R.drawable.nearby_no_icon2).dontAnimate().into(iv_card);

        iv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String name = data.name;
                String desc = data.desc1;
                if (name.equals("1")) {
                    intent.setClass(mContext, GoldDesActivity.class);
                    mContext.startActivity(intent);
                } else if (name.equals("2")) {
                    if (AccountManager.Instace(mContext).checkUserLogin()) {
                        if (TouristUtil.isTourist()) {
                            TouristUtil.showTouristHint(mContext);
                            return;
                        }
                        if (BuildConfig.isEnglish) {
                            intent.setClass(mContext, Web.class);
                            intent.putExtra("url", "http://m.iyuba.com/mall/addressManage.jsp?"
                                    + "&uid=" + AccountManager.Instace(mContext).getId()
                                    + "&sign=" + MD5.getMD5ofStr("iyuba" + AccountManager.Instace(mContext).getId() + "camstory")
                                    + "&username=" + AccountManager.Instace(mContext).getUserName()
                                    + "&appid="
                                    + Constant.APPID + "&presentId=" + (BuildConfig.isCET4 ? "52" : "54"));

                            intent.putExtra("title", BuildConfig.isCET4 ? "免费赠送四级真题书" : "免费赠送六级真题书");
                            mContext.startActivity(intent);
                        } else {
                            if (!AccountManager.isVip()) {
                                new AlertDialog.Builder(mContext)
                                        .setTitle("提示")
                                        .setMessage("开通会员后可免费领取日语真题书")
                                        .setPositiveButton("确认", null)
                                        .show();
                                return;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("免费送真题书")
                                    .setMessage("开通iyuba会员，添加QQ 3274422495，免费赠送日语N" + Constant.APP_CONSTANT.TYPE() + "真题书")
                                    .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                String url = "mqqwpa://im/chat?chat_type=wpa&uin=";
                                                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse(url + "3274422495")));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(mContext, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    } else {
                        intent.setClass(mContext, Login.class);
                        mContext.startActivity(intent);
                    }
                } else if (name.equals("3")) {

//                    EventBus.getDefault().post(new MainMicroClassEvent())

                    mContext.startActivity(new Intent(mContext, MobClassActivity.class));


                } else if (name.equals("4")) {
                    intent.setClass(mContext, Web.class);
                    intent.putExtra("url", desc);
                    mContext.startActivity(intent);

                } else if (name.equals("6")) {
                    if (!Constant.APP_CONSTANT.isEnglish()) {
                        EventBus.getDefault().post(new MainMicroClassEvent("6"));
                    }
                }
            }
        });
    }

}
