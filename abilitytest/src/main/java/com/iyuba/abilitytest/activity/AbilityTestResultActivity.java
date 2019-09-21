package com.iyuba.abilitytest.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.adapter.AbilityTestResultAdapter;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.AddCreditModule;
import com.iyuba.abilitytest.event.StartMicroClassEvent;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.utils.TimeFormatUtil;
import com.iyuba.abilitytest.widget.ListViewInScrollView;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.LogUtils;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 用户答完题目之后显示的界面  分数  题目概况  能力概况
 *
 * @author Administrator
 * @version 1.0.0
 * @time 2016/9 16:57
 */
public class AbilityTestResultActivity extends AppBaseActivity {
    /**
     * 保存每一个类型的答题结果
     */
    private int[] results;
    private Context mContext;
    private final int FINISHSELF = 10000;
    /**
     * 答对题目的个数
     */
    private int rightNum;
    /**
     * 没有回答的题目数
     */
    private int mUndoNum;
    private String[] abilityType;
    private int testType;
    /**
     * 总的题目数量, 可以根据测试项目类型赋值
     */
    private int mTotalTestNum;
    private AbilityResult mAbilityResult;
    private String userTimeToshow;
    private int mTotalScore;
    private String mTypeName;
    private int mAbilityTypeCount;//能力数目
    private String[] mTestTypeArr;
    private ListViewInScrollView lv_ability_result;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_abilitytestresult;
    }

    @Override
    protected void initVariables() {
        initCommons();
        mContext = this;
        String lastTestTime = "未知";//上次测试时间
        testType = getIntent().getIntExtra("testType", -1);
        mAbilityResult = (AbilityResult) getIntent().getSerializableExtra("abilityResults");
        mTestTypeArr = getIntent().getStringArrayExtra("testTypeArr");
        results = new int[mTestTypeArr.length];
        rightNum = 0;
        //根据每个题目的类型转化成百分制成绩进行展示
        if (mAbilityResult != null) {
            String begintime = mAbilityResult.beginTime;
            String endtime = mAbilityResult.endTime;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date begin = formatter.parse(begintime);
                Date end = formatter.parse(endtime);
                long usedTime = end.getTime() / 1000 - begin.getTime() / 1000;//单位 秒
                userTimeToshow = usedTime / 60 + "分" + usedTime % 60 + "秒";
            } catch (Exception e) {
                e.printStackTrace();
            }
            rightNum = mAbilityResult.DoRight;
            lastTestTime = mAbilityResult.endTime;
            mUndoNum = mAbilityResult.UndoNum;
            mTotalTestNum = mAbilityResult.Total;
        }

        int typecount = 0;
        switch (testType) {
            case Constant.ABILITY_TETYPE_WRITE://写作
                mTypeName = "写作";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
            case Constant.ABILITY_TETYPE_WORD://单词
                mTypeName = "单词";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
            case Constant.ABILITY_TETYPE_GRAMMAR://语法
                mTypeName = "语法";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
            case Constant.ABILITY_TETYPE_LISTEN://听力
                mTypeName = "听力";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
            case Constant.ABILITY_TETYPE_SPEAK://口语
                mTypeName = "口语";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
            case Constant.ABILITY_TETYPE_READ://阅读
                mTypeName = "阅读";
                typecount = mTestTypeArr.length;
                abilityType = new String[typecount];
                getResult(mAbilityResult, typecount);
                break;
        }

        mTotalScore = mTotalTestNum == 0 ? 0 : mAbilityResult.DoRight * 100 / mTotalTestNum;
    }

    private void getResult(AbilityResult mAbilityResult, int typecount) {

        for (int i = 0; i < typecount; i++) {
            results[0] = mAbilityResult.Score1 == null ? 0 : getResult(mAbilityResult.Score1, 0);
            if (typecount > 1) {
                results[1] = mAbilityResult.Score2 == null ? 0 : getResult(mAbilityResult.Score2, 1);
            }
            if (typecount > 2) {
                results[2] = mAbilityResult.Score3 == null ? 0 : getResult(mAbilityResult.Score3, 2);
            }
            if (typecount > 3)
                results[3] = mAbilityResult.Score4 == null ? 0 : getResult(mAbilityResult.Score4, 3);
            if (typecount > 4)
                results[4] = mAbilityResult.Score5 == null ? 0 : getResult(mAbilityResult.Score5, 4);
            if (typecount > 5)
                results[5] = mAbilityResult.Score6 == null ? 0 : getResult(mAbilityResult.Score6, 5);
            if (typecount > 6)
                results[6] = mAbilityResult.Score7 == null ? 0 : getResult(mAbilityResult.Score7, 6);
        }
    }

    /**
     * 根据数据库中的数据保存过情况,计算用户的各项
     *
     * @param scoreInfo 分数信息
     * @param i
     * @return
     */
    private int getResult(String scoreInfo, int i) {
        int totalNum = 0;
        int userScore = 0;
        if (!scoreInfo.equals("-1")) {
            totalNum = Integer.parseInt(scoreInfo.split("\\+\\+")[0]);//试题总数
            userScore = Integer.parseInt(scoreInfo.split("\\+\\+")[1]);//用户答对的题数
            abilityType[i] = scoreInfo.split("\\+\\+")[2];//应用能力名称
        }
        return totalNum == 0 ? 0 : userScore * 100 / totalNum;//转化为百分制
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        TextView tv_user_score = findView(R.id.tv_user_score);//用户测试成绩
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/EDOSZ.TTF");
        tv_user_score.setTypeface(typeface);
        Button btn_goto_test = findView(R.id.btn_goto_test);
        TextView tv_title = findView(R.id.tv_titlebar_sub);
        String titleName = "我的能力(" + mTypeName + ")";
        tv_title.setText(titleName);
        tv_user_score.setText(mTotalScore + "");

        btn_goto_test.setOnClickListener(gotoTargetActivityClickListener());

        lv_ability_result = findView(R.id.lv_ability_result);
        //题目分析控件
        TextView tv_titleNum = findView(R.id.tv_ability_result_titleNum);
        TextView tv_rightNum = findView(R.id.tv_ability_result_rightnum);
        TextView tv_wrongNum = findView(R.id.tv_ability_result_wrongnum);
        TextView tv_undoNum = findView(R.id.tv_ability_result_undonum);
        TextView tv_usetime = findView(R.id.tv_ability_result_usetime);


        ImageButton ibtn_test_share = findView(R.id.ibtn_test_share);
        ibtn_test_share.setVisibility(View.VISIBLE);
        ibtn_test_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        int score1 = Integer.parseInt(mAbilityResult.Score1.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score1.split("\\+\\+")[1]);
        int score2 = Integer.parseInt(mAbilityResult.Score2.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score2.split("\\+\\+")[1]);
        int score3 = Integer.parseInt(mAbilityResult.Score3.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score3.split("\\+\\+")[1]);
        int score4 = Integer.parseInt(mAbilityResult.Score4.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score4.split("\\+\\+")[1]);
        int score5 = Integer.parseInt(mAbilityResult.Score5.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score5.split("\\+\\+")[1]);
        int score6 = Integer.parseInt(mAbilityResult.Score6.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score6.split("\\+\\+")[1]);
        int score7 = Integer.parseInt(mAbilityResult.Score7.split("\\+\\+")[0]) == -1 ? 0 : Integer.parseInt(mAbilityResult.Score7.split("\\+\\+")[1]);

        int totalscore = score1 + score2 + score3 + score4 + score5 + score6 + score7;

        totalscore = totalscore > mTotalTestNum ? mTotalTestNum : totalscore;//答对的题目数 千万不可以大于试题总数 这种情况应该不会发生
        int undo = mAbilityResult.UndoNum > mTotalTestNum - rightNum ? mTotalTestNum - rightNum : mAbilityResult.UndoNum;//这种情况一般也不会发生,会不会欺骗消费者?
        int worongNum = mTotalTestNum - rightNum - undo < 0 ? 0 : mTotalTestNum - rightNum - undo;//会出现负数吗?

        //赋值
        tv_titleNum.setText(mTotalTestNum + "");//试题总数
        tv_rightNum.setText(totalscore + "");//答对的个数
        tv_wrongNum.setText(worongNum + "");//打错的个数
        tv_undoNum.setText(undo + "");//未答的个数
        tv_usetime.setText(userTimeToshow);//时长

        Button btn_goto_detail = findView(R.id.btn_goto_detail);
        btn_goto_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbilityTestResultActivity.this, ShowAnalysisActivity.class);
                try {
                    intent.putExtra("testCategory", getIntent().getStringExtra("testCategory"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    private void showShare() {
        MobSDK.init(getApplicationContext(), Constant.APP_CONSTANT.MOB_APPKEY(), Constant.APP_CONSTANT.MOB_APP_SECRET());

        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setSite(Constant.APPName);
        //oks.setSite("托福听力");
        oks.setSite(Constant.APPName);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://app.iyuba.cn/android/androidDetail.jsp?id=" + Constant.APPID);
        // 图片的网络路径，新浪微博、人人、QQ空间和Linked-in
        oks.setTitle(Constant.APPName);
        oks.setTitleUrl("http://app.iyuba.cn/android/androidDetail.jsp?id=" + Constant.APPID);
        oks.setFilePath("http://app.iyuba.cn/androidApp.jsp?id=" + Constant.APPID + "&appId=" + Constant.APPID_DOWNLOAD);
        oks.setUrl("http://app.iyuba.cn/androidApp.jsp?id=" + Constant.APPID + "&appId=" + Constant.APPID_DOWNLOAD);
        oks.setText("来挑战我的" + mTypeName + "水平吧~ 我在" + Constant.APPName + "测评中得了" + mTotalScore + " 分。地址:http://app.iyuba.cn/android/androidDetail.jsp?id=" + Constant.APPID);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = platform.getName();//分享的平台的名称
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
            }
        });
        // 启动分享GUI
        oks.show(this);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int srid;
            String platformName = "";
            if (msg.what == 3) {
                platformName = (String) msg.obj;
                Log.e("liuzhenli", "本次的分享平台:" + platformName);
            }
            if (platformName.equals("QZone") || platformName.equals("WechatMoments")) {
                srid = 19;
            } else {
                srid = 49;
            }
            switch (msg.what) {
                case 1:
                    Toast.makeText(mContext, "分享成功加" + msg.arg2 + "分!您当前总积分:" + msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(mContext, "分享成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    String userId = AccountManager.Instace(mContext).userId;
                    LogUtils.e("aaa", "userId" + userId);
                    String flag = "1234567890" + TimeFormatUtil.getTime();

                    Call<AddCreditModule> call = AbilityTestRequestFactory.addCredit().addCredit(userId, Constant.APPID, srid + "", flag);
                    call.enqueue(new retrofit2.Callback<AddCreditModule>() {
                        @Override
                        public void onResponse(Call<AddCreditModule> call, Response<AddCreditModule> response) {

                            if (response.body().result == 200) {
                                Message msg = Message.obtain();
                                msg.obj = response.body();
                                msg.what = 1;
                                msg.arg1 = response.body().totalcredit;
                                msg.arg2 = response.body().addcredit;
                                handler.sendMessage(msg);
                            } else if (response.body().result == 201) {
                                handler.sendEmptyMessage(2);
                            }
                        }

                        @Override
                        public void onFailure(Call<AddCreditModule> call, Throwable t) {

                        }
                    });
                    break;
            }
        }
    };

    /**
     * 进入对应微课界面
     */
    private View.OnClickListener gotoTargetActivityClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImoocManager.appId = Constant.APPID;
//                Intent intent = MobClassActivity.buildIntent(mContext, Integer.parseInt(Constant.APP_CONSTANT.courseTypeId()),true);
//                startActivity(intent);
                EventBus.getDefault().post(new StartMicroClassEvent());

                hander.sendEmptyMessage(FINISHSELF);
            }
        };
    }

    @Override
    protected void loadData() {
        AbilityTestResultAdapter adapter = new AbilityTestResultAdapter(mContext, mAbilityResult, mTestTypeArr.length);
        lv_ability_result.setAdapter(adapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINISHSELF:
                    finish();
                    break;
            }
        }
    };


}
