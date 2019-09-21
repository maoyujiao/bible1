//package com.iyuba.abilitytest.manager;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//
//import com.iyuba.abilitytest.utils.FileUtil;
//import com.iyuba.abilitytest.utils.ResultParse;
//import com.iyuba.abilitytest.utils.WavWriter;
//import com.iyuba.configation.Constant;
//import com.iyuba.core.util.LogUtils;
//import com.iyuba.core.util.ToastUtil;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
////进行语音对比打分
//public class IseManager {
//    private static final String TAG = IseManager.class.getSimpleName();
//
//    private static IseManager instance;
//    private static Handler handler;
//
////    private SpeechEvaluator mSpeechEvaluator;
//
//    private String LANGUAGE = "en_us";
//    private String ISE_CATEGORY = "read_sentence";
//    // private String ISE_CATEGORY = "read_word";
//    private String RESULT_LEVEL = "complete";
//    private String VAD_BOS = "5000";
//    private String VAD_EOS = "1800";
//    private String KEY_SPEECH_TIMEOUT = "-1";
//
//    private String resultStr;
////    private Result resultEva; //
//
//    private String pcmFileName;
//
//    private int time;// 录音时间
//    private int senIndex;
//    private String sentence;
//    private String endTime;
//    private Context mContext;
//
//    private IseManager(Context context) {
//        this.mContext = context;
//        // 创建评测对象
////        mSpeechEvaluator = SpeechEvaluator.createEvaluator(context, null);
//    }
//
//    public static IseManager getInstance(Context context, Handler h) {
//        if (instance == null) {
//            instance = new IseManager(context);
//        }
//        handler = h;
//        return instance;
//    }
//
//    private void setParams() {
////        pcmFileName = Environment.getExternalStorageDirectory()
////                .getAbsolutePath()
////                + "/msc/"
////                + LANGUAGE
////                + "_"
////                + ISE_CATEGORY
////                + "_" + System.currentTimeMillis() / 1000 + ".pcm";
//
//        pcmFileName = Environment.getExternalStorageDirectory()
//                .getAbsolutePath()
//                + "/iyuba/"
//                + "sound.pcm";
//        LogUtils.e("pcmFileName    " + pcmFileName);
//        // 仅作测试
//        // pcmFileName
//        // =Environment.getExternalStorageDirectory().getAbsolutePath()
//        // +"/msc "+"/test.pcm";
//        // 测试部分
//
//        // 设置评测语种
//        mSpeechEvaluator.setParameter(SpeechConstant.LANGUAGE, LANGUAGE);
//        // 设置评测题型
//        mSpeechEvaluator
//                .setParameter(SpeechConstant.ISE_CATEGORY, ISE_CATEGORY);
//        // 设置试题编码类型
//        mSpeechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
//        mSpeechEvaluator.setParameter(SpeechConstant.SAMPLE_RATE, 16000 + "");
//        // 设置前、后端点超时
//        mSpeechEvaluator.setParameter(SpeechConstant.VAD_BOS, VAD_BOS);
//        mSpeechEvaluator.setParameter(SpeechConstant.VAD_EOS, VAD_EOS);
//        // 设置录音超时，设置成-1 则无超时限制
//        mSpeechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,
//                KEY_SPEECH_TIMEOUT);
//        // 设置结果等级，不同等级对应不同的详细程度
//        mSpeechEvaluator
//                .setParameter(SpeechConstant.RESULT_LEVEL, RESULT_LEVEL);
//        mSpeechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, pcmFileName);
//    }
//
//    // 评测监听接口
//    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {
//
//        @Override
//        public void onResult(EvaluatorResult result, boolean isLast) {
//            LogUtils.e(TAG, result.toString());
//            handler.sendEmptyMessage(7); // 不加这个音频会自动启动，别问我为什么
//            // LogUtils.e("评测结果:  " + result.getResultString() + isLast);
//            if (isLast) {
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//                endTime = df.format(new Date());// new Date()为获取当前系统时间
//                StringBuilder builder = new StringBuilder();
//                builder.append(result.getResultString());
//                resultStr = builder.toString();
//                resultParse();
//
//                Message msg = handler.obtainMessage();
//                msg.what = 6;
//                msg.arg1 = (int) (resultEva.total_score * 20);
//                LogUtils.e("真实的得分  " + resultEva.total_score);
//                msg.arg2 = senIndex;
//                msg.obj = resultEva.is_rejected;
//
//                handler.sendMessage(msg);
//                transformPcmToAmr();
//            }
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            if (error != null) {
//                LogUtils.e("错误  " + error.getErrorDescription() + error.getErrorCode());
////                 showTip("error:" + error.getErrorCode() + ","
////                + error.getErrorDescription());
//                ToastUtil.showToast(mContext, error.getErrorDescription() + error.getErrorCode());
//            } else {
//                LogUtils.e(TAG, "evaluator over");
//            }
//            Message msg = handler.obtainMessage();
//            msg.what = 1000;
//            msg.obj = error.getErrorDescription() + error.getErrorCode();
//            handler.sendMessage(msg);
//        }
//
//        @Override
//        public void onBeginOfSpeech() {
//            LogUtils.e(TAG, "开始评测");
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            handler.sendEmptyMessage(3);
//            // handlerRead.removeMessages(1);
//            LogUtils.e(TAG, "evaluator stoped");
//        }
//
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//        }
//
//        @Override
//        public void onVolumeChanged(int arg0, byte[] arg1) {
//
//            LogUtils.e("音量大小有变化  " + arg0);
//            noticeVolume(arg0);
//        }
//
//    };
//
//    public void noticeVolume(int volume) {
//        Message msg = handler.obtainMessage();
//        msg.what = 2;
//        msg.arg1 = volume;
//        handler.sendMessage(msg);
//    }
//
//    public void startEvaluate(String sen, int senIndex) {
//        if (mSpeechEvaluator != null) {
//            setParams();
//            LogUtils.e("语音评测开始  " + sen);
//            mSpeechEvaluator.startEvaluating(sen, null, mEvaluatorListener);
//            this.senIndex = senIndex;
//            this.sentence = sen;
//            time = 0;
//            // handlerRead.sendEmptyMessage(1);
//        }
//    }
//
//    public boolean isEvaluating() {
//        return mSpeechEvaluator.isEvaluating();
//    }
//
//    public void stopEvaluating() {
//        if (mSpeechEvaluator.isEvaluating()) {
//            mSpeechEvaluator.stopEvaluating();
//            // handlerRead.removeMessages(1);
//        }
//    }
//
//    public void cancelEvaluate(boolean cancel) {
//        mSpeechEvaluator.cancel();
//    }
//
//    public void resultParse() {
//        if (!TextUtils.isEmpty(resultStr)) {
//            XmlResultParser resultParser = new XmlResultParser();
//            resultEva = resultParser.parse(resultStr.toString());
//        }
//    }
//
//    public String getResult() {
//        return resultStr;
//    }
//
//    public Result getResultEva() {
//        return resultEva;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//    public void transformPcmToAmr() {
//        File amrFile = new File(Constant.getsimRecordAddr() + senIndex
//                + Constant.getrecordTag());
//        File pcmFile = new File(pcmFileName);
//        FileUtil.fileChannelCopy(pcmFile, amrFile);
//        LogUtils.e(amrFile.getAbsolutePath());
//        WavWriter myWavWriter = null;
//        try {
//            myWavWriter = new WavWriter(amrFile, 16000);
//            myWavWriter.writeHeader();
//            myWavWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        pcmFile.delete();
//    }
//
//    public SpannableStringBuilder getSenStyle() {
//        LogUtils.e(TAG, String.valueOf(resultEva.is_rejected));
//        LogUtils.e(TAG, resultEva.toString());
//        return ResultParse.getSenResult(resultEva, sentence);
//    }
//}
