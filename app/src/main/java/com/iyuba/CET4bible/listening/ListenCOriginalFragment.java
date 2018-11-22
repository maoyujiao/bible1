package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSum;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSynView;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.mode.test.CetText;
import com.iyuba.core.widget.BackPlayer;
import com.iyuba.core.widget.WordCard;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListenCOriginalFragment extends Fragment {
    private Context mContext;
    private View root;
    private SubtitleSynView original;
    private WordCard card;
    private int currParagraph;
    private BackPlayer mPlayer;
    private SubtitleSum subtitleSum;
    private int time;
    private View unvip;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (currParagraph != 0) {
                        original.snyParagraph(currParagraph);
                    }
                    if (!mPlayer.isPlaying()) {
                        original.setSyncho(false);
                    } else {
                        original.setSyncho(SettingConfig.Instance().isSyncho());
                    }
                    break;
                case 1:
                    currParagraph = subtitleSum.getParagraph(mPlayer
                            .getCurrentPosition() / 1000.0);
                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessageDelayed(1, 1500);
                    break;
            }
        }
    };
    private TextPageSelectTextCallBack tp = new TextPageSelectTextCallBack() {
        @Override
        public void selectTextEvent(String selectText) {

            if (selectText.matches("^[a-zA-Z'-]*.{1}")) {
                String regEx = "[^a-zA-Z'-]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(selectText);
                selectText = m.replaceAll("").trim();
                card.setVisibility(View.VISIBLE);
                card.searchWord(selectText);
            } else {
                card.setVisibility(View.GONE);
                CustomToast.showToast(mContext,
                        R.string.play_please_take_the_word);
            }
        }

        @Override
        public void selectParagraph(int paragraph) {
            mPlayer.seekTo(Integer.parseInt(ListenDataManager.Instance().textList
                    .get(paragraph).time) * 1000);
            currParagraph = paragraph;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.listen_original, container, false);
        mContext = RuntimeManager.getContext();
        mPlayer = BackgroundManager.Instace().bindService.getPlayer();
        init();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        time = args.getInt("time");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    public void init() {
        card = root.findViewById(R.id.word);
        card.setVisibility(View.GONE);
        original = root.findViewById(R.id.original);
        unvip = root.findViewById(R.id.un_vip);
        int isvip = ConfigManager.Instance().loadInt("isvip");
        unvip.setVisibility(View.GONE);
        setSubtitleSum();
        setDetail();
        original.setSubtitleSum(subtitleSum);
        original.setTpstcb(tp);
        original.setSyncho(SettingConfig.Instance().isSyncho());
        handler.sendEmptyMessage(1);
        /*if (isvip == 1) {
			unvip.setVisibility(View.GONE);
			setSubtitleSum();
			setDetail();
			original.setSubtitleSum(subtitleSum);
			original.setTpstcb(tp);
			original.setSyncho(SettingConfig.Instance().isSyncho());
			handler.sendEmptyMessage(1);
		} else {
			unvip.setVisibility(View.VISIBLE);
			original.setVisibility(View.GONE);
			Button buy = (Button) root.findViewById(R.id.buy);
			buy.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					mContext.startActivity(new Intent(mContext, BuyVip.class));
				}
			});
		}*/

    }

    private void setSubtitleSum() {
        subtitleSum = new SubtitleSum();
        if (subtitleSum.subtitles == null) {
            subtitleSum.subtitles = new ArrayList<Subtitle>();
            subtitleSum.subtitles.clear();
        }
    }

    private void setDetail() {
        ArrayList<CetText> textList = ListenDataManager.Instance().textList;
        int size = textList.size();
        Subtitle st;
        StringBuffer sb;
        for (int i = 0; i < size; i++) {
            st = new Subtitle();
            sb = new StringBuffer();
            sb.append("\t");
            sb.append(textList.get(i).sentence);
            st.content = sb.toString();
            if (time == 1 || time == 0) {
                st.pointInTime = Integer.parseInt(textList.get(i).time);
            } else if (time == 2) {
                st.pointInTime = Integer.parseInt(textList.get(i).time2);
            } else {
                st.pointInTime = Integer.parseInt(textList.get(i).time3);
            }
            subtitleSum.subtitles.add(st);
        }
    }
}
