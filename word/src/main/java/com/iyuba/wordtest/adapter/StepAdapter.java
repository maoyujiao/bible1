package com.iyuba.wordtest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.iyuba.wordtest.R;
import com.iyuba.wordtest.R2;
import com.iyuba.wordtest.WordListActivity;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.manager.WordManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StepAdapter extends BaseAdapter {


    private int stepPerLine;
    private int step;
    private int size;

    int convertPosition = 0;

    Context context;

    public StepAdapter(int stepPerLine, int step, int size) {
        this.stepPerLine = stepPerLine;
        this.step = step;
        this.size = size;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.wordtest_step_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(convertView);
        TextView tv = holder.tv;
        CircleImageView iv = holder.img;
        ImageView iv_avtor = holder.img_avator;
        View vView = holder.lineVt;
        iv.setVisibility(View.GONE);
        iv_avtor.setVisibility(View.GONE);
        tv.setText(String.format("第%d关", position + 1));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position>1&&WordManager.get().vip == 0){
                    ToastUtils.showShort("请开通会员学习后面的内容");
                    return;
                }
                if (position >= WordConfigManager.Instance(context).loadInt("stage", 1)) {
                    ToastUtils.showShort("尚未开启此关卡,请先学习前面的内容");
                } else {
                    WordListActivity.startIntnent(context, position + 1 , false);
                }

            }
        });

        if (step <= position) {
            tv.setBackground(context.getResources().getDrawable(R.drawable.word_step_bg_lock));
//            vView.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
            tv.setText("未解锁");
        }else {
            tv.getBackground().setTint(context.getResources().getColor(R.color.colorPrimary));
        }
        vView.setVisibility(View.INVISIBLE);

        if (step == position + 1) {
            iv.setVisibility(View.VISIBLE);
            loadUserIcon("..", holder.img);
            iv_avtor.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void loadUserIcon(String url, ImageView imageView) {
        String userIconUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + WordManager.get().userid + "&size=middle";
        Glide.with(context)
                .load(userIconUrl)
                .asBitmap()
                .signature(new StringSignature(System.currentTimeMillis() + ""))
                .placeholder(R.drawable.noavatar_small)
                .into(imageView);
    }

    static class ViewHolder {
        @BindView(R2.id.tv)
        TextView tv;
        @BindView(R2.id.line_vt)
        View lineVt;
        @BindView(R2.id.img)
        CircleImageView img;
        @BindView(R2.id.img_indicator)
        ImageView img_avator;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
