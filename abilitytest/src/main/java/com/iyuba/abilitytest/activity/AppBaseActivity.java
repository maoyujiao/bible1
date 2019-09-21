package com.iyuba.abilitytest.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.TestRecord;
import com.iyuba.abilitytest.protocol.UploadTestRecordRequest;
import com.iyuba.abilitytest.sqlite.TestRecordHelper;
import com.iyuba.abilitytest.utils.JsonUtil;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.activity.BaseActivity;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;

import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * 基类
 *
 * @author liuzhenli
 * @version 1.0.0
 * @time 2016/9 16:59
 */
public abstract class AppBaseActivity extends BaseActivity {

   // private TestResultHelper mZDBHelper;
    private TestRecordHelper mTestResHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RuntimeManager.setApplication(getApplication());
       // mZDBHelper = TestResultHelper.getInstance(mContext);
        mTestResHelper = TestRecordHelper.getInstance(mContext);
    }

    public void initCommons() {
        ImageButton selector_btn_bg = findView(R.id.btn_nav_sub);
        if (selector_btn_bg != null) {
            selector_btn_bg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    /**
     * alertdialog 提示用户是继续还是退出测试     *
     * @param cur   当前进度
     * @param total 试题总数
     * @param mode  1--测评 2 练习
     */
   /* public void showAlertDialog(final int cur, int total, final String type, final ArrayList<AbilityQues> mQuesList, final int mode) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示:");
        dialog.setMessage("测试进度:" + cur + "/" + total + ",是否放弃测试?");
        dialog.setPositiveButton("离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pidition) {
                ToastUtil.showToast(mContext, type + "测试未完成");
                //已经保存的数据库记录需要标记一下,不上传服务器
                if (mode == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < cur - 1; i++) {
                                //boolean b = zdbHelper.setTestRecordIsUpload(mQuesList.get(i).TestId);
                                boolean b = mTestResHelper.setTestRecordIsUpload(mQuesList.get(i).TestId);
                                LogUtils.e("数据是否可以上传: " + b);
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadTestRecordToNet(getUid(), 0, mode);
                        }
                    }).start();
                }
                finish();
            }
        });
        dialog.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }*/

    /***
     * 上传测试结果到大数据 该方法用于上传失败数据的上传
     *
     * @param uid      用户的id
     * @param ability  测试类型id 0写作 1单词  2语法  3听力 4.口语 5阅读
     * @param testMode 测试类型id X写作 W单词  G语法  L听力 S口语 R阅读
     * @param mode     1测评 2练习
     */
    public void uploadTestRecordToNet(String uid, int ability, String testMode, int mode) {
        if (mTestResHelper == null)
            mTestResHelper = TestRecordHelper.getInstance(mContext);
        ArrayList<TestRecord> mTestRecordList = mTestResHelper.getWillUploadTestRecord(testMode, getUid(), mode);//每一个题目测试记录  根据mode区分是练习还是测评
        ArrayList<AbilityResult> mAbilityResultLists = new ArrayList<>();
        if (mode == 1) {//练习不上传该记录
            mAbilityResultLists = mTestResHelper.getAbilityTestRecord(ability, uid, true);//每一项能力结果
        }
        if (mTestRecordList.size() > 0 || mAbilityResultLists.size() > 0) {//有可传数据再上传
            try {
                jsonForTestRecord = JsonUtil.buildJsonForTestRecordDouble(mTestRecordList, mAbilityResultLists, uid, mode);
                LogUtils.e("appBaseActivity.java", "buildJsonForTestRecord" + jsonForTestRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.e("appBaseActivity.java：", "获取将要上传的做题记录！！！！！！！");
            String url = Constant.url_updateExamRecord;
            UploadTestRecordRequest up = new UploadTestRecordRequest(jsonForTestRecord, url);//上传

            String result = up.getResultByName("result");
            String jifen = up.getResultByName("jiFen");
            LogUtils.e("积分:" + jifen + "结果   " + result);
            if (Integer.parseInt(jifen) > 0) {
                ToastUtil.showToast(mContext, "测评数据成功同步到云端 +" + jifen + "积分");
            }
            TestRecord testRecords;
            AbilityResult aResult;
            if (!result.equals("-1") && !result.equals("-2")) {// 成功
                int size = mTestRecordList.size();
                for (int i = 0; i < size; i++) {
                    testRecords = (TestRecord) mTestRecordList.toArray()[i];
                    mTestResHelper.setTestRecordIsUpload(testRecords.TestNumber);
                }
                for (int i = 0; i < mAbilityResultLists.size(); i++) {
                    aResult = (AbilityResult) mAbilityResultLists.toArray()[i];
                    boolean b = mTestResHelper.setAbilityResultIsUpload(aResult.TestId);
                    LogUtils.e("上传之后数据库是否更新:  " + b + "  aResult.TestId: " + aResult.TestId);
                }
            }
        } else {
            LogUtils.e("没有数据上传服务器");
        }
    }

    /***
     * 获取用户的ID
     *
     * @return 用户ID
     */
    protected String getUid() {
        return AccountManager.Instace(getApplicationContext()).userId == null ? "" : AccountManager.Instace(getApplicationContext()).userId;
    }

    /**
     * 判断用户是不是VIP
     */
    public boolean isVip(){
        try {
            return isUserLogin()&& (!AccountManager.Instace(getApplicationContext()).userInfo.vipStatus.equals("0") );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * alertdialog 提示用户是继续还是退出测试
     *
     * @param total 试题总数
     * @param mode  1--测评 2 练习
     */
    public void showAlertDialogLocal(int total, int index, final String type, final ArrayList<TestRecord> testRecordList, final AbilityResult abilityResult, final int mode) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示:");
        dialog.setMessage("测试进度:" + index + "/" + total + ",是否放弃测试?");
        dialog.setPositiveButton("离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pidition) {
                ToastUtil.showToast(mContext, type + "测试未完成");
                //已经保存的数据库记录需要标记一下,不上传服务器
                if (mode == 1) {
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadTestRecordToNetLocal(testRecordList, abilityResult, mode);
                        }
                    }).start();

                }
                finish();
            }
        });
        dialog.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private String jsonForTestRecord;

    /***
     * 上传测试结果到大数据  答完题目之后直接上传
     *
     * @param mode 1测评 2练习
     */
    public void uploadTestRecordToNetLocal(ArrayList<TestRecord> mTestRecordList, AbilityResult mAbilityResultLists, int mode) {
        if (mode==1){
            boolean bb = mTestResHelper.seveTestRecord(mAbilityResultLists);//分析结果 保存到数据库
        }
        if (mTestRecordList.size() > 0) {//有可传数据再上传
            try {
                jsonForTestRecord = JsonUtil.buildJsonForTestRecordDouble2(mTestRecordList, mAbilityResultLists, getUid(), mode);
                LogUtils.e("appbaseactivity.java", "buildJsonForTestRecord:  " + jsonForTestRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.e("执行到的地方测试：", "获取将要上传的做题记录！！！！！！！");
            String url = Constant.url_updateExamRecord;
            UploadTestRecordRequest up = new UploadTestRecordRequest(jsonForTestRecord, url);
            String result = up.getResultByName("result");
            if (!result.equals("-1") && !result.equals("-2")) {// 成功
                mTestResHelper.setAbilityResultIsUpload(mAbilityResultLists.TestId);
               final String jifen = up.getResultByName("jiFen");
                LogUtils.e("积分:" + jifen + "结果   " + result);
                if (Integer.parseInt(jifen) > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mContext, "测评数据成功同步到云端 +" + jifen + "积分");
                        }
                    });
                }
            } else {//上传失败了,保存数据
                boolean boo = mTestResHelper.saveTestRecords(mTestRecordList);//测试记录 每一道题目保存到数据库
                LogUtils.e("appBaseActivity.java", "测试结果上传失败了  Testrecord保存:" + boo);
            }
        } else {
            LogUtils.e("没有数据上传服务器");
        }
    }
}
