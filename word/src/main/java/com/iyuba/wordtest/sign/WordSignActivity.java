package com.iyuba.wordtest.sign;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.iyuba.wordtest.R;
import com.iyuba.wordtest.databinding.WordSignBinding;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.viewmodel.UserSignViewModel;


public class WordSignActivity extends BaseSignActivity implements SignMVPView {


    SignPresenter presenter ;

    UserSignViewModel model ;
    public static Intent buildIntent(Context context, UserSignViewModel model) {
        Intent intent;
        intent = new Intent(context, WordSignActivity.class);
        intent.putExtra("model",  model);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignPresenter();

        WordSignBinding wordSignBinding = DataBindingUtil.setContentView(this, R.layout.word_sign);
         model = (UserSignViewModel) getIntent().getSerializableExtra("model");
        wordSignBinding.setUserinfo(model);
        presenter.attachView(this);
//        signPresenter.attachView(this);
    }

    @Override
    protected void initView() {
        String userIconUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + WordManager.get().userid + "&size=middle";
        ImageView view = findViewById(R.id.userimage);
        Glide.with(this).load(userIconUrl).into(view);
        TextView share = findViewById(R.id.share_wechat);
        TextView cancel = findViewById(R.id.cancel);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.writeBitmapToFile(findViewById(R.id.root));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.word_sign;
    }

    @Override
    public void showSucess(String sucess) {
        ToastUtils.showShort(sucess);
        finish();
    }

    @Override
    public void showFail(String fail) {
        ToastUtils.showShort(fail);
    }

    @Override
    public void saveImageFinished(String imagePath  ) {
        String userid = WordManager.get().userid;
        finish();
        presenter.showShareOnMoment(this,userid, WordManager.get().appid,imagePath, model.getStage());
    }


}
