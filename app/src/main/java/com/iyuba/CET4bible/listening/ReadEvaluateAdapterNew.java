package com.iyuba.CET4bible.listening;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.op.NewTypeTextAOp;
import com.iyuba.CET4bible.util.Share;
import com.iyuba.abilitytest.entity.SendEvaluateResponse;
import com.iyuba.abilitytest.network.PublishVoiceResponse;
import com.iyuba.CET4bible.util.SentenceSpanUtils;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.network.EvaluateApi;
import com.iyuba.abilitytest.utils.ToastUtil;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.RecordManager;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.trainingcamp.utils.ACache;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lid.lib.LabelTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.cet4.activity.fragment
 * @class describe
 * @time 2019/1/21 11:32
 * @change
 * @chang time
 * @class describe
 */
public class ReadEvaluateAdapterNew extends RecyclerView.Adapter<ReadEvaluateAdapterNew.ViewHolder> {

    private int mActivePosition = 0;
    private String playUrl;

    NewTypeTextAOp cetHelper;
    private String shareWebHeader = "http://voa.iyuba.cn/voa/play.jsp?";
    private String paraId;
    private RecordManager mRecordManager;
    private String examTime;
    private Subscription recordSubscription;
    private Subscription s;
    private Subscription s2;
    private boolean isRecording = false;
    private KProgressHUD publishingDialog;

    private Typeface typeface = Typeface.SERIF;

    private MediaPlayer mPlayerOrigin;
    private MediaPlayer mPlayerRecord;
    private Context mContext;
    private String userId;
    private Subscription recordingObservable;
    private Subscription recordingTimerSubscription;
    private boolean isPlaying = false;
    private int playingIndex = -1;

    public ReadEvaluateAdapterNew(List<Subtitle> items, String ExamTime, String url, Context context) {
        mItems = items;
        examTime = ExamTime;   // 当做lessonid ;
        playUrl = url;
        mContext = context;
        initVariables();

        for (Subtitle text : mItems) {
            text = cetHelper.getRecordingResult(text, ((SectionA) mContext).section);
        }
        try {
            mPlayerOrigin.setDataSource(playUrl);
            mPlayerOrigin.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        mPlayerOrigin = new MediaPlayer();
        mPlayerRecord = new MediaPlayer();
        cetHelper = new NewTypeTextAOp(mContext);
        publishingDialog = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等")
                .setDetailsLabel("正在上传...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        userId = com.iyuba.configation.ConfigManager.Instance().loadString("userId");
        if (TextUtils.isEmpty(com.iyuba.configation.ConfigManager.Instance().loadString("userId"))) {
            userId = "0";
        }

        ((SectionA)mContext).setOriginFragmentPlayer(mPlayerOrigin);
        ((SectionA)mContext).setRecordFragmentPlayer(mPlayerRecord);
    }


    public List<Subtitle> getmItems() {
        return mItems;
    }

    private List<Subtitle> mItems;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_read_evaluate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setActive((position == mActivePosition));
        if (position < mItems.size() - 1) {
            holder.setItem(mItems.get(position), mItems.get(position + 1));
        } else {
            holder.setItem(mItems.get(position), mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sen_en)
        LabelTextView enTextView;
        @BindView(R.id.sep_line)
        View separateLine;
        @BindView(R.id.bottom_view)
        LinearLayout buttonsLayout;
        @BindView(R.id.sen_play)
        RoundProgressBar playProgressBar;
        @BindView(R.id.sen_i_read)
        RoundProgressBar readProgressBar;
        @BindView(R.id.sen_read_button)
        FrameLayout readPlayLayout;
        @BindView(R.id.sen_read_play)
        ImageView readPlayImageView;
        @BindView(R.id.sen_read_playing)
        RoundProgressBar recordPlayProgressBar;
        @BindView(R.id.sen_read_send)
        ImageView sendImageView;
        @BindView(R.id.sen_read_collect)
        ImageView shareImageView;
        @BindView(R.id.sen_read_result)
        TextView readResultTextView;

        Subtitle item;
        Subtitle itemNext;

        boolean isActive = false;
        private int inteval;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            enTextView.setTypeface(typeface);
        }


        public void setActive(boolean active) {
            isActive = active;
        }

        private void setReadScoreViewContent(TextView view, int score) {
            if (score < 50) {
                view.setText("");
                view.setBackgroundResource(R.drawable.sen_score_lower60);
            } else if (score > 80) {
                view.setText(String.valueOf(score));
                view.setBackgroundResource(R.drawable.sen_score_higher_80);
            } else {
                view.setText(String.valueOf(score));
                view.setBackgroundResource(R.drawable.sen_score_60_80);
            }
        }

        public void setItem(Subtitle item, Subtitle itemNext) {
            this.item = item;
            this.itemNext = itemNext;

            enTextView.setLabelText(item.NumberIndex + "");
            enTextView.setText(item.content);
            shareImageView.setVisibility(View.INVISIBLE);
            if (isActive) {
                separateLine.setVisibility(View.VISIBLE);
                buttonsLayout.setVisibility(View.VISIBLE);
                if (item.isRead) {
                    readResultTextView.setVisibility(View.VISIBLE);
                    setReadScoreViewContent(readResultTextView, (int) item.readScore);
                    setViewVisibilityAndClickability(readPlayLayout, true);
                    setViewVisibilityAndClickability(sendImageView, true);
                } else {
                    setViewVisibilityAndClickability(readPlayLayout, false);
                    setViewVisibilityAndClickability(sendImageView, false);
                }
            } else {
                separateLine.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.GONE);
            }
            if (item.goodList != null || item.badList != null) {
                setWordsBean(item);
            }

        }

        MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                recordPlayProgressBar.setVisibility(View.GONE);
                readPlayImageView.setVisibility(View.VISIBLE);
            }
        };



        MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                inteval = 0;
                mp.start();
                isPlaying = true;
                recordPlayProgressBar.setMax(mp.getDuration());
                readPlayImageView.setVisibility(View.INVISIBLE);
                recordPlayProgressBar.setVisibility(View.VISIBLE);
                recordSubscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                recordPlayProgressBar.setProgress(inteval * 100);
                                inteval++;
                            }
                        });
            }
        };

        private void setViewVisibilityAndClickability(View view, boolean isAble) {
            if (isAble) {
                view.setVisibility(View.VISIBLE);
                view.setClickable(true);
            } else {
                view.setVisibility(View.INVISIBLE);
                view.setClickable(false);
            }
        }

        @OnClick(R.id.linear_text_container)
        public void clickTextPart() {
            if (mActivePosition != getAdapterPosition()) {
                isPlaying = false;
                stopRunningJob();
                mActivePosition = getAdapterPosition();
                notifyDataSetChanged();
            }
        }

        @OnClick(R.id.sen_read_playing)
        public void stop_play() {
            if (isPlaying) {
                recordPlayProgressBar.setProgress(0);
                recordPlayProgressBar.setVisibility(View.GONE);
                readPlayImageView.setVisibility(View.VISIBLE);
                mPlayerRecord.pause();
                isPlaying = false;
                Timber.tag("diao").d("1234");
                return;
            }
        }


        @OnClick(R.id.sen_read_send)
        public void sendVoice() {
            if (!AccountManager.Instace(mContext).checkUserLogin()) {
                ToastUtils.showShort("请登录后发布");
                return;
            }
            publishingDialog.setDetailsLabel("正在发布").setLabel("请稍等").show();

            Map<String, String> map = new HashMap<>();
            map.put("topic", (com.iyuba.configation.Constant.mListen));
            map.put("protocol", ("60002"));
            map.put("platform", ("android"));
            map.put("shuoshuotype", ("2"));
            map.put("format", ("json"));
            map.put("voaid", (String.valueOf(examTime)));
//            map.put("paraId",(String.valueOf(item.Number+")));
            if (((SectionA) mContext).section.equals("A")) {
                map.put("paraid", ("1"));
            } else if (((SectionA) mContext).section.equals("B")) {
                map.put("paraid", ("2"));
            } else if (((SectionA) mContext).section.equals("C")) {
                map.put("paraid", ("3"));
            }
            map.put("idIndex", (item.Number + "000" + item.NumberIndex));
            map.put("score", (String.valueOf(item.readScore)));
            map.put("userid", (userId));// TODO this is not good
//            map.put("username", AbilityTestRequestFactory.fromString(userName));
            map.put("content", (item.record_url));

            Call<PublishVoiceResponse> call = AbilityTestRequestFactory.getPublishApi().publishVoice(map);

            call.enqueue(new Callback<PublishVoiceResponse>() {
                @Override
                public void onResponse(Call<PublishVoiceResponse> call, Response<PublishVoiceResponse> response) {
                    publishingDialog.dismiss();
                    setViewVisibilityAndClickability(sendImageView, false);
                    item.shuoshuoid = response.body().getShuoShuoId();
                    shareImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<PublishVoiceResponse> call, Throwable t) {
                    publishingDialog.dismiss();
                }
            });
        }

        @OnClick(R.id.sen_read_play)
        public void onClickrecordPlayProgressBar() {
            stopRunningJob();
            Timber.tag("diao").d(isPlaying + "");


            File file = new File(Environment.getExternalStorageDirectory() + "/iyuba/" + com.iyuba.configation.Constant.mListen + "/"
                    + "audio/"
                    + examTime + "-" + paraId + "-"
                    + item.NumberIndex + ".amr");
            Timber.tag("diao").d(file.getAbsolutePath());
            if (file.exists()) {
                try {
                    mPlayerRecord.reset();
                    mPlayerRecord.setDataSource(file.getAbsolutePath());
                    mPlayerRecord.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            isPlaying = true;
            playingIndex = mActivePosition;
            mPlayerRecord.setOnPreparedListener(onPreparedListener);
            mPlayerRecord.setOnCompletionListener(completionListener);

        }

        @OnClick(R.id.sen_play)
        public void clickPlayOriginal() {
            if (isRecording) {
                return;
            }
            stopRunningJob();

            if (isPlaying && playingIndex == mActivePosition) {
                playProgressBar.setBackgroundResource(R.drawable.sen_play);
                playProgressBar.setProgress(0);
                isPlaying = false;
                return;
            }

            Timber.e("playOri");
            Timber.tag("diao").e(String.valueOf(item.pointInTime));
            playProgressBar.setMax(100);
            if (s2 != null) {
                s2.unsubscribe();
            }
            inteval = 0;
            isPlaying = true;
            playingIndex = mActivePosition;
            mPlayerOrigin.seekTo(item.pointInTime * 1000);
            Log.d("diao", item.pointInTime * 1000 + "");
            mPlayerOrigin.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mPlayerOrigin.start();
                    playProgressBar.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sen_stop));
                    if (mActivePosition == playingIndex) {
                        s = Observable.interval(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Timber.tag("diao").d(mPlayerOrigin.getCurrentPosition() + ":" + item.pointInTime * 1000 + ":" + item.endTiming * 1000);
                                if (mActivePosition == mItems.size() - 1) {
                                    playProgressBar.setProgress((inteval) * 10 / 20);
                                } else {
                                    playProgressBar.setProgress((inteval) * 10 / (itemNext.pointInTime - item.pointInTime));
                                }
                                inteval++;
                            }
                        });
                        if (mActivePosition == mItems.size() - 1) {
                            s2 = Observable.timer(20, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    mPlayerOrigin.pause();
                                    playProgressBar.setBackgroundResource(R.drawable.sen_play);
                                    playProgressBar.setProgress(0);
                                    isPlaying = false;
                                    s.unsubscribe();
                                }
                            });
                        } else {
                            s2 = Observable.timer((long) (itemNext.pointInTime - item.pointInTime + 0.75), TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    mPlayerOrigin.pause();
                                    playProgressBar.setBackgroundResource(R.drawable.sen_play);
                                    playProgressBar.setProgress(0);
                                    isPlaying = false;
                                    s.unsubscribe();
                                }
                            });
                        }
                    }

                }
            });
        }

        @OnClick(R.id.sen_i_read)
        public void clickEvaluate() {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
                //验证是否许可权限
                for (String str : permissions) {
                    if (mContext.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        ((SectionA) mContext).requestPermissions(permissions, SectionA.REQUEST_CODE_CONTACT);
                        return;
                    }
                }
                startEvaluateJobs();
            } else {
                startEvaluateJobs();
            }

        }


        private void startEvaluateJobs() {
            stopRunningJob();
            if (!NetworkUtils.isConnected()) {
                ToastUtil.showToast(itemView.getContext(), "请检查网络连接");
                return;
            }
            final File file = new File(Environment.getExternalStorageDirectory() + "/iyuba/" + com.iyuba.configation.Constant.mListen + "/"
                    + "audio/"
                    + examTime + "-" + paraId + "-"
                    + item.NumberIndex + ".amr");
            if (!isRecording) {
                if (!file.exists()) {
                    try {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mRecordManager = new RecordManager(file);
                mRecordManager.startRecord();
                isRecording = true;
                readProgressBar.setMax(100);
                recordingTimerSubscription = Observable.interval(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        readProgressBar.setProgress((int) mRecordManager.getVolume());
                    }
                });
                recordingObservable = Observable.timer(item.content.length() * 120 + 200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        if (isRecording && isActive) {
                                            isRecording = false;
                                            mRecordManager.stopRecord();
                                            startEvaluate(item, file);
                                            recordingTimerSubscription.unsubscribe();
                                        }
                                    }
                                }
                        );
            } else {
                isRecording = false;
                mRecordManager.stopRecord();
                recordingTimerSubscription.unsubscribe();
                recordingObservable.unsubscribe();
                startEvaluate(item, file);
            }
        }

        @OnClick(R.id.sen_read_collect)
        public void onCLickshare() {
            ((SectionA) mContext).showShare("我在AI英语语音评测中得了" + item.readScore + "分", item.content,
                    shareWebHeader + "id=" + item.shuoshuoid + "&appid=" + Constant.APPID + "&apptype=" + com.iyuba.configation.Constant.mListen + "&addr=" + item.record_url
                            + "&from=singemessage");
        }

        private void startEvaluate(Subtitle Subtitle, File file) {
            ToastUtils.showLong("正在测评,请稍等");
            readProgressBar.setProgress(0);
            Map<String, RequestBody> map = new HashMap<>(6);
            map.put(EvaluateApi.GetVoa.Param.Key.SENTENCE, AbilityTestRequestFactory.fromString(Subtitle.content));
            map.put(EvaluateApi.GetVoa.Param.Key.IDINDEX, AbilityTestRequestFactory.fromString(item.Number + "-" + item.NumberIndex));
            map.put(EvaluateApi.GetVoa.Param.Key.NEWSID, AbilityTestRequestFactory.fromString(examTime));
            map.put(EvaluateApi.GetVoa.Param.Key.PARAID, AbilityTestRequestFactory.fromString(Subtitle.Number + ""));
            map.put(EvaluateApi.GetVoa.Param.Key.TYPE, AbilityTestRequestFactory.fromString(com.iyuba.configation.Constant.mListen + ""));
            map.put(EvaluateApi.GetVoa.Param.Key.USERID, AbilityTestRequestFactory.fromString(userId));
            Call<SendEvaluateResponse> call = AbilityTestRequestFactory.getEvaluateApi().sendVoice(map, AbilityTestRequestFactory.fromFile(file));
            call.enqueue(new Callback<SendEvaluateResponse>() {
                @Override
                public void onResponse(Call<SendEvaluateResponse> call, final Response<SendEvaluateResponse> response) {
                    itemView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body() != null) {
                                item.isRead = true;
                                if (response.body().getResult().equals("0")) {
                                    ToastUtils.showShort("评测失败");
                                    return;
                                }
                                if (response.body().getData() != null) {
                                    setReadScoreViewContent(readResultTextView, (int) (Double.parseDouble(response.body().getData().getTotal_score()) * 20));
                                    item.readScore = (int)(Double.parseDouble(response.body().getData().getTotal_score()) * 20);
                                    item.mDataBean = response.body().getData().getWords();
                                    item.record_url = response.body().getData().getURL();
                                    List<Integer> goodlist = new ArrayList<>();
                                    List<Integer> badList = new ArrayList<>();

                                    for (int i = 0; i < response.body().getData().getWords().size(); i++) {
                                        SendEvaluateResponse.DataBean.WordsBean bean = response.body().getData().getWords().get(i);
                                        if (bean.getScore() < 2.5) {
                                            badList.add(i);
                                        } else if (bean.getScore() > 4) {
                                            goodlist.add(i);
                                        }
                                    }
                                    item.badList = badList;
                                    item.goodList = goodlist;
                                    writeNewDataToDB(item);
                                    notifyDataSetChanged();
                                } else {
                                    ToastUtil.showToast(mContext, "失败");
                                }
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<SendEvaluateResponse> call, Throwable t) {
                    ToastUtils.showShort("评测失败");
                }
            });

        }

        public void setWordsBean(Subtitle item) {
            if (item.goodList == null || item.badList == null) {
                return;
            }
            SpannableStringBuilder builder = SentenceSpanUtils.getSpanned(mContext, item.content,
                    item.goodList, item.badList);
            enTextView.setText(builder);
        }

        private void stopRunningJob() {
            if (recordSubscription != null) recordSubscription.unsubscribe();
            if (s != null) s.unsubscribe();
            if (s2 != null) s2.unsubscribe();
            if (recordingTimerSubscription != null) recordingTimerSubscription.unsubscribe();
            if (recordingObservable != null) recordingObservable.unsubscribe();
            if (mPlayerRecord.isPlaying()) mPlayerRecord.pause();
            if (mPlayerOrigin != null && mPlayerOrigin.isPlaying()) mPlayerOrigin.pause();
            readPlayImageView.setVisibility(View.VISIBLE);
            recordPlayProgressBar.setVisibility(View.GONE);
        }
    }

    private void writeNewDataToDB(Subtitle item) {
        cetHelper.writeRecordingData(item, ((SectionA) mContext).section);
    }

    public void stopRunningJob() {
        if (recordSubscription != null) recordSubscription.unsubscribe();
        if (s != null) s.unsubscribe();
        if (s2 != null) s2.unsubscribe();
        if (recordingTimerSubscription != null) recordingTimerSubscription.unsubscribe();
        if (recordingObservable != null) recordingObservable.unsubscribe();
        if (mPlayerRecord != null) mPlayerRecord.reset();
        if (mPlayerOrigin != null) mPlayerOrigin.pause();
    }

    public void stopRunningJobTotally() {
        if (recordSubscription != null) recordSubscription.unsubscribe();
        if (s != null) s.unsubscribe();
        if (s2 != null) s2.unsubscribe();
        if (recordingTimerSubscription != null) recordingTimerSubscription.unsubscribe();
        if (recordingObservable != null) recordingObservable.unsubscribe();
        if (mPlayerRecord != null) mPlayerRecord.reset();
        if (mPlayerOrigin != null) {
            mPlayerOrigin.stop();
            mPlayerOrigin.release();
        }
    }



}
