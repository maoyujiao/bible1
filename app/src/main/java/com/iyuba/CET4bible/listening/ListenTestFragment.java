package com.iyuba.CET4bible.listening;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.service.Background;
import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.BackPlayer;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.dialog.CustomToast;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

public class ListenTestFragment extends Fragment implements
        OnPlayStateChangedListener, OnClickListener {
    private Context mContext;
    private Context activityContext;

    private View root;
    private TextView number, question, answer1, answer2, answer3, answer4;
    private Button qsound;
    private Player qPlayer;
    String mExamTime ;
    private ArrayList<CetAnswer> answerList = new ArrayList<CetAnswer>();
    private int curPos;
    private BackPlayer mPlayer;

    private ImageView cbFavorite;
    private boolean isFavoriteChecked = false;
    private FavoriteUtil favoriteUtil;
    private String favoritePrefix = "listening_";
    private CetAnswer answerrrrrr;
    private String year;
    private String section;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.listen_test, container, false);
        mContext = RuntimeManager.getContext();
        activityContext = container.getContext();
        section = getArguments().getString("section");
        mExamTime = getArguments().getString("examtime");
        favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.listening);
        cbFavorite = root.findViewById(R.id.cb_favorite);
        cbFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteChecked(!isFavoriteChecked);
                favoriteUtil.setFavorite(isFavoriteChecked, getKey());
            }
        });
        init();
        return root;
    }

    private String getKey() {
        return favoritePrefix  + year + "_" + section + "_" + answerrrrrr.sound;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComments();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        qPlayer.pause();
        qPlayer.reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (qPlayer!=null){
            qPlayer.stop();

        }
    }

    public void init() {
        number = root.findViewById(R.id.row);
        question = root.findViewById(R.id.question);
        answer1 = root.findViewById(R.id.answer1);
        answer2 = root.findViewById(R.id.answer2);
        answer3 = root.findViewById(R.id.answer3);
        answer4 = root.findViewById(R.id.answer4);
        qsound = root.findViewById(R.id.qsound);
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);
        qsound.setOnClickListener(this);
        qPlayer = new Player(mContext, this);
        if (ListenDataManager.Instance().rowString != null) {
            setContent();
        }
    }

    private void setContent() {
        if (null != BackgroundManager.Instace().bindService) {
            getActivity().bindService(new Intent(getActivity(),Background.class),BackgroundManager.Instace().conn, Service.BIND_AUTO_CREATE);
            if (BackgroundManager.Instace().bindService != null){
                mPlayer = BackgroundManager.Instace().bindService.getPlayer();
            }else {
                mPlayer = new BackPlayer(mContext);
            }
        }
        answerList = ListenDataManager.Instance().answerList;
        curPos = ListenDataManager.curPos;
        number.setText(ListenDataManager.Instance().rowString);
        question.setText("Question: " + answerList.get(curPos).id);
        answer1.setText("A: " + answerList.get(curPos).a1);
        answer2.setText("B: " + answerList.get(curPos).a2);
        answer3.setText("C: " + answerList.get(curPos).a3);
        answer4.setText("D: " + answerList.get(curPos).a4);
        String answer = answerList.get(curPos).yourAnswer;
        if (TextUtils.isEmpty(answer)) {
            setAnswer(0);
        } else if (answer.equals("A")) {
            setAnswer(1);
        } else if (answer.equals("B")) {
            setAnswer(2);
        } else if (answer.equals("C")) {
            setAnswer(3);
        } else if (answer.equals("D")) {
            setAnswer(4);
        }

        answerrrrrr = answerList.get(curPos);
        year = ListenDataManager.Instance().year;

        setFavoriteChecked(favoriteUtil.isFavorite(getKey()));
    }

    private void setFavoriteChecked(boolean isChecked) {
        isFavoriteChecked = isChecked;
        cbFavorite.setImageResource(isChecked ? R.drawable.ic_favorite_orange_24dp : R.drawable.ic_favorite_orange_border_24dp);
    }

    private void setAnswer(int position) {
        answer1.setTextColor(Color.BLACK);
        answer2.setTextColor(Color.BLACK);
        answer3.setTextColor(Color.BLACK);
        answer4.setTextColor(Color.BLACK);
        switch (position) {
            case 1:
                answer1.setTextColor(mContext.getResources().getColor(
                        R.color.app_color));
                answerList.get(curPos).yourAnswer = "A";
                break;
            case 2:
                answer2.setTextColor(mContext.getResources().getColor(
                        R.color.app_color));
                answerList.get(curPos).yourAnswer = "B";
                break;
            case 3:
                answer3.setTextColor(mContext.getResources().getColor(
                        R.color.app_color));
                answerList.get(curPos).yourAnswer = "C";
                break;
            case 4:
                answer4.setTextColor(mContext.getResources().getColor(
                        R.color.app_color));
                answerList.get(curPos).yourAnswer = "D";
                break;
            default:
                answerList.get(curPos).yourAnswer = "";
                break;
        }

    }

    @Override
    public void playCompletion() {
        qPlayer.reset();
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    @Override
    public void playFaild() {
        CustomToast.showToast(mContext, R.string.check_network);
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.answer1:
                setAnswer(1);
                break;
            case R.id.answer2:
                setAnswer(2);
                break;
            case R.id.answer3:
                setAnswer(3);
                break;
            case R.id.answer4:
                setAnswer(4);
                break;
            case R.id.qsound:
                mPlayer.pause();
                if (checkLocalFiles()){
                    qPlayer.playUrl(Constant.videoAddr
                            + ListenDataManager.Instance().year + File.separator
                            + answerList.get(curPos).qsound);

                    Log.e("qsound", Constant.videoAddr
                            + ListenDataManager.Instance().year + File.separator
                            + answerList.get(curPos).qsound);
                }else {
                    String type = Constant.APP_CONSTANT.TYPE();

                    qPlayer.playUrl("http://cetsounds.iyuba.cn/" + type
                    + "/" + mExamTime + "/" + answerList.get(curPos).qsound);
                }

                if (((SectionA)activityContext).getPlayer()!=null&&((SectionA)activityContext).getPlayer().isPlaying()){
                    ((SectionA)activityContext).getPlayer().pause();
                }

                break;
            default:
                break;
        }
    }

    private boolean checkLocalFiles() {
        String year = mExamTime;
        String fileNoAppend = Constant.videoAddr + year + ".cet4";
        String folder = Constant.videoAddr + year;
        File file1 = new File(folder);
        File file2 = new File(fileNoAppend);
        if (file1.exists()) {
            if (file1.list().length <= 0) {
                file1.delete();
                return false;
            }
            // complete
            return true;
        } else if (file2.exists()) {
            // downloading
            return false;
        } else {
            // no down
            return false;
        }
    }

    @NonNull
    private String getSoundPath() {
        if (checkLocalFiles()) {
            return Constant.videoAddr
                    + mExamTime + File.separator
                    + answerList.get(curPos).sound;
        } else {
            String type = Constant.APP_CONSTANT.TYPE();
            String path = "http://cetsounds.iyuba.cn/" + type
                    + "/" + mExamTime + "/" + answerList.get(curPos).sound;
            return path;
        }
    }

    public void initComments() {
        final int isvip = ConfigManager.Instance().loadInt("isvip");
        final View unvip = root.findViewById(R.id.un_vip);
        unvip.setVisibility(View.GONE);
        final View seeComments = root.findViewById(R.id.see_comments);
        seeComments.setVisibility(View.VISIBLE);
        seeComments.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                seeComments.setVisibility(View.GONE);
                if (isvip > 0) {
                    unvip.setVisibility(View.GONE);
                    TextView text = root.findViewById(R.id.comment);
                    int curPos = ListenDataManager.Instance().curPos + Integer.parseInt(ListenDataManager.Instance().answerList.get(0).id) - 1;
                    StringBuffer sb = new StringBuffer();
                    try {
                        CetExplain explain = ListenDataManager.Instance().explainList
                                .get(curPos);
                        sb.append("&nbsp;&nbsp;&nbsp;<font color='")
                                .append(getResources().getColor(R.color.app_color))
                                .append("'>");
                        sb.append(mContext.getString(R.string.keys)).append("</font>");
                        sb.append(explain.keys);
                        sb.append("<br/><br/>&emsp;<font color='")
                                .append(getResources().getColor(R.color.app_color))
                                .append("'>");
                        sb.append(mContext.getString(R.string.explains)).append("</font>");
                        sb.append(explain.explain);
                        sb.append("<br/><br/>&emsp;<font color='")
                                .append(getResources().getColor(R.color.app_color))
                                .append("'>");
                        sb.append(mContext.getString(R.string.knowledges))
                                .append("</font>");
                        sb.append(explain.knowledge);
                        text.setText(Html.fromHtml(TextAttr.ToDBC(sb.toString())));
                        text.setMovementMethod(ScrollingMovementMethod.getInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    unvip.setVisibility(View.VISIBLE);
                    Button buy = root.findViewById(R.id.buy);
                    buy.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            mContext.startActivity(new Intent(mContext, VipCenter.class));
                        }
                    });
                }
            }
        });

    }
}
