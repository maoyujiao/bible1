package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.BBCInfoBean;
import com.iyuba.trainingcamp.bean.LessonIdBean;
import com.iyuba.trainingcamp.bean.VoaInfoBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.widget.GlideRoundTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 */
public class LessonAdapter extends PagerAdapter {

    private Context mContext;
    private List<LessonIdBean.LessonListBean> mLessonListBeans ;
    private DailyWordDBHelper mHelper;

    public LessonAdapter(Context context,List<LessonIdBean.LessonListBean> beanList){
        mLessonListBeans = beanList ;
        mContext = context;
        mHelper = new DailyWordDBHelper(mContext);
    }
    @Override
    public int getCount() {
        return mLessonListBeans.size()/3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trainingcamp_learn_card,container,false);
        final RelativeLayout relativeLayout = view.findViewById(R.id.rr);
        final TextView lesson_number  = view.findViewById(R.id.lesson_number);

        final TextView title  = view.findViewById(R.id.lesson_title);
        final TextView number  = view.findViewById(R.id.numbers);
        TextView study_sign = view.findViewById(R.id.study_sign);
        lesson_number.setText(String.valueOf(position+1));
        if (GoldApp.getApp(mContext).LessonType.contains("cet")){
            Glide.with(mContext).load(R.drawable.trainingcamp_pager_placeholder)
                    .transform(new GlideRoundTransform(mContext, 6))
                    .into(new ViewTarget<View, GlideDrawable>(relativeLayout) {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                            this.view.setBackground(resource.getCurrent());
                        }
                    });

            number.setVisibility(View.GONE);
            SpannableStringBuilder style = new SpannableStringBuilder(number.getText().toString());
            String s =number.getText().toString();
            style.setSpan(new RelativeSizeSpan(2.5f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            number.setText(style);

        }else {
            if (!GoldApp.getApp(mContext).LessonType.contains("bbc")){
                retrofit2.Call<VoaInfoBean> call =
                        VipRequestFactory.getVoaInfoApi().getVoaInfo(String.valueOf(mLessonListBeans.get(position*3).getLessonid()));
                call.enqueue(new Callback<VoaInfoBean>() {
                    @Override
                    public void onResponse(Call<VoaInfoBean> call, Response<VoaInfoBean> response) {
                        VoaInfoBean bean = response.body();
                        if (bean == null){
                            return;
                        }
                        number.setVisibility(View.VISIBLE);

                        title.setText(bean.getData().get(0).getTitle_cn());
                        number.setText(bean.getData().get(0).getReadCount()+"次阅览");
                        Glide.with(mContext).load(bean.getData().get(0).getPic())
                                .placeholder(R.drawable.trainingcamp_pager_placeholder)
                                .transform(new GlideRoundTransform(mContext, 6))
                                .into(new ViewTarget<View, GlideDrawable>(relativeLayout) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource,
                                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        this.view.setBackground(resource.getCurrent());
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<VoaInfoBean> call, Throwable t) {

                    }
                });

            }else {
                retrofit2.Call<BBCInfoBean> call =
                        VipRequestFactory.getVoaInfoApi().getBBCInfo(String.valueOf(mLessonListBeans.get(position*3).getLessonid()));
                call.enqueue(new Callback<BBCInfoBean>() {
                    @Override
                    public void onResponse(Call<BBCInfoBean> call, Response<BBCInfoBean> response) {
                        BBCInfoBean bean = response.body();
                        if (bean == null){
                            return;
                        }
                        number.setVisibility(View.VISIBLE);

                        title.setText(bean.getData().get(0).getTitle_cn());
                        number.setText(bean.getData().get(0).getReadCount()+"次阅览");
                        Glide.with(mContext).load(bean.getData().get(0).getPic())
                                .placeholder(R.drawable.trainingcamp_pager_placeholder)
                                .transform(new GlideRoundTransform(mContext, 6))
                                .into(new ViewTarget<View, GlideDrawable>(relativeLayout) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource,
                                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        this.view.setBackground(resource.getCurrent());
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<BBCInfoBean> call, Throwable t) {

                    }
                });
                Glide.with(mContext).load("http://static.iyuba.com/images/voa/"+mLessonListBeans.get(position*3).getLessonid()+".jpg")
                        .transform(new GlideRoundTransform(mContext, 10))
                        .into(new ViewTarget<View, GlideDrawable>(relativeLayout) {
                            @Override
                            public void onResourceReady(GlideDrawable resource,
                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                                this.view.setBackground(resource.getCurrent());
                            }
                        });
            }

        }
        GoldDateRecord record = mHelper.selectDataById(GoldApp.getApp(mContext).userId,mLessonListBeans.get(position*3).getLessonid());
        if (record.getStep().equals("3")){
            study_sign.setBackgroundResource(R.drawable.trainingcamp_icon_finished_new);
            study_sign.setText("已学习");
            study_sign.setTextColor(mContext.getResources().getColor(R.color.trainingcamp_white));
        }else if (record.getStep().equals("1")){
            study_sign.setBackgroundResource(R.drawable.trainingcamp_lock_bg);
            study_sign.setText("未学习");
            study_sign.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }else {
            study_sign.setTextColor(mContext.getResources().getColor(R.color.trainingcamp_white));
            study_sign.setBackgroundResource(R.drawable.trainingcamp_icon_finished_new);
            study_sign.setText("学习中");
        }

        container.addView(view);
        return view;
    }

    private void getTitleUrl(){

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
