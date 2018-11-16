package com.iyuba.trainingcamp.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;

import com.iflytek.ise.result.Result;
import com.iflytek.ise.result.entity.Word;
import com.iflytek.ise.result.xml.XmlResultParser;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.FileUtil;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.ResultParse;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.utils.WavWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//进行语音对比打分
public class IseManager {
    private static final String TAG = IseManager.class.getSimpleName();

    private static IseManager instance;
    private static Handler handler;
    private static SpeechEvaluator getSpeechEvaluator;

    private SpeechEvaluator mSpeechEvaluator;

    private String LANGUAGE = "en_us";
    private String ISE_CATEGORY = "read_sentence";
    private String RESULT_LEVEL = "complete";
    private String VAD_BOS = "5000";
    private String VAD_EOS = "1800";
    private String KEY_SPEECH_TIMEOUT = "-1";

    private String resultStr;
    private Result resultEva;

    private String pcmFileName;
    /** 录音时间**/
    private int time;
    private int senIndex;
    private String sentence;
    private String endTime;
    private Context mContext;

    private IseManager(Context context) {
        this.mContext = context;
        // 创建评测对象
        mSpeechEvaluator = SpeechEvaluator.createEvaluator(context, null);
    }

    public static IseManager getInstance(Context context, Handler h) {
        if (instance == null) {
            instance = new IseManager(context);
        }
        handler = h;
        LogUtils.d(handler.toString());
        return instance;
    }

    public static void destroy(){
        instance = null ;
        if (null != handler){
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
    }

    private void setParams() {
//        pcmFileName = Environment.getExternalStorageDirectory()
//                .getAbsolutePath()
//                + "/msc/"
//                + LANGUAGE
//                + "_"
//                + ISE_CATEGORY
//                + "_" + System.currentTimeMillis() / 1000 + ".pcm";

        pcmFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/iyuba/"
                + "sound.pcm";
        LogUtils.e("pcmFileName    " + pcmFileName);

        // 仅作测试
        // pcmFileName
        // =Environment.getExternalStorageDirectory().getAbsolutePath()
        // +"/msc "+"/test.pcm";
        // 测试部分

        // 设置评测语种
        mSpeechEvaluator.setParameter(SpeechConstant.LANGUAGE, LANGUAGE);
        // 设置评测题型
        mSpeechEvaluator
                .setParameter(SpeechConstant.ISE_CATEGORY, ISE_CATEGORY);
        // 设置试题编码类型
        mSpeechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mSpeechEvaluator.setParameter(SpeechConstant.SAMPLE_RATE, 16000 + "");
        // 设置前、后端点超时
        mSpeechEvaluator.setParameter(SpeechConstant.VAD_BOS, VAD_BOS);
        mSpeechEvaluator.setParameter(SpeechConstant.VAD_EOS, VAD_EOS);
        // 设置录音超时，设置成-1 则无超时限制
        mSpeechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,
                KEY_SPEECH_TIMEOUT);
        // 设置结果等级，不同等级对应不同的详细程度
        mSpeechEvaluator
                .setParameter(SpeechConstant.RESULT_LEVEL, RESULT_LEVEL);
        mSpeechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, pcmFileName);
    }

    // 评测监听接口
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            LogUtils.e(TAG, result.toString());
            handler.sendEmptyMessage(7); // 不加这个音频会自动启动，别问我为什么
            // LogUtils.e("评测结果:  " + result.getResultString() + isLast);
            if (isLast) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
                endTime = df.format(new Date());// new Date()为获取当前系统时间
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());

                resultStr = builder.toString();
                Log.d(TAG, "onResult: "+resultStr);
                resultParse();

                Message msg = handler.obtainMessage();
                msg.what = 6;
                msg.arg1 = (int) (resultEva.total_score * 20);
                List<Integer> indexList = new ArrayList();
                for (Word word : resultEva.sentences.get(0).words){
                    if (word.total_score<3){
                        indexList.add(word.index);
                    }
                }
//                msg.arg2 = resultEva.
                LogUtils.e("真实的得分  " + resultEva.total_score);

                msg.obj =indexList;

                handler.sendMessage(msg);
                transformPcmToAmr();
            }
        }

        @Override
        public void onError(SpeechError error) {
            if (error != null) {
                LogUtils.e("错误  " + error.getErrorDescription() + error.getErrorCode());
//                 showTip("error:" + error.getErrorCode() + ","
//                + error.getErrorDescription());
                ToastUtil.showToast(mContext, error.getErrorDescription() + error.getErrorCode());
            } else {
                LogUtils.e(TAG, "evaluator over");
            }
            Message msg = handler.obtainMessage();
            msg.what = 1000;
            msg.obj = error.getErrorDescription() + error.getErrorCode();
            handler.sendMessage(msg);
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtils.e(TAG, "开始评测");
        }

        @Override
        public void onEndOfSpeech() {
            handler.sendEmptyMessage(3);
            // handlerRead.removeMessages(1);
            LogUtils.e(TAG, "evaluator stoped");
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onVolumeChanged(int arg0, byte[] arg1) {

            LogUtils.e("音量大小有变化  " + arg0);
            noticeVolume(arg0);
        }

    };

    public void noticeVolume(int volume) {
        if (handler == null){
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = 2;
        msg.arg1 = volume;
        handler.sendMessage(msg);
    }

    public void startEvaluate(String sen, int senIndex) {
        if (mSpeechEvaluator != null) {
            setParams();
            LogUtils.e("语音评测开始  " + sen);
            mSpeechEvaluator.startEvaluating(sen, null, mEvaluatorListener);
            this.senIndex = senIndex;
            this.sentence = sen;
            time = 0;
            // handlerRead.sendEmptyMessage(1);
        }
    }

    public boolean isEvaluating() {
        return mSpeechEvaluator.isEvaluating();
    }

    public void stopEvaluating() {
        if (mSpeechEvaluator.isEvaluating()) {
            Log.d(TAG, "stopEvaluating: ");
            mSpeechEvaluator.stopEvaluating();
            // handlerRead.removeMessages(1);
        }
    }

    public void cancelEvaluate(boolean cancel) {
        if (null != mSpeechEvaluator){
            mSpeechEvaluator.cancel();
        }
    }

    public void resultParse() {
        if (!TextUtils.isEmpty(resultStr)) {
            XmlResultParser resultParser = new XmlResultParser();
            resultEva = resultParser.parse(resultStr.toString());
        }
    }

    public String getResult() {
        return resultStr;
    }

    public Result getResultEva() {
        return resultEva;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getTime() {
        return time;
    }

    public void transformPcmToAmr() {
        File amrdir = new File(FilePath.getRecordPath());
        if (!amrdir.exists()){
            amrdir.mkdir();

        }
        File amrFile = new File(FilePath.getRecordPath()+ senIndex
                + ".amr");

        if (!amrFile.exists()){
            try {
                amrFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        File amrFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"hahhahahhaa.mp3");
        File pcmFile = new File(pcmFileName);
        FileUtil.fileChannelCopy(pcmFile, amrFile);
        LogUtils.e(amrFile.getAbsolutePath());
        WavWriter myWavWriter = null;
        try {
            myWavWriter = new WavWriter(amrFile, 16000);
            myWavWriter.writeHeader();
            myWavWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pcmFile.delete();
    }

    public SpannableStringBuilder getSenStyle() {
        LogUtils.e(TAG, String.valueOf(resultEva.is_rejected));
        LogUtils.e(TAG, resultEva.toString());
        return ResultParse.getSenResult(resultEva, sentence);
    }

}
