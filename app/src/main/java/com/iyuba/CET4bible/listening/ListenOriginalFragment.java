package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSum;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSynView;
import com.iyuba.base.util.L;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.mode.test.CetText;
import com.iyuba.core.widget.WordCard;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;
import com.iyuba.play.ExtendedPlayer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListenOriginalFragment extends Fragment {
    private Context mContext;
    private View root;
    private SubtitleSynView original;
    private WordCard card;
    private int currParagraph;
    private ExtendedPlayer mPlayer;
    private View unvip;
    private SubtitleSum subtitleSum;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (getPlayer() != null) {
                        if (currParagraph != 0) {
                            original.snyParagraph(currParagraph);
                        }
                        if (!mPlayer.isPlaying()) {
                            original.setSyncho(false);
                        } else {
                            original.setSyncho(SettingConfig.Instance().isSyncho());
                        }
                    }
                    break;
                case 1:
                    if (getPlayer() != null) {
                        try{
                            currParagraph = subtitleSum.getParagraph(mPlayer
                                    .getCurrentPosition() / 1000.0);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessageDelayed(1, 1500);
                    }
                    break;
                default:
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
            if (getPlayer() != null) {
                mPlayer.seekTo(Integer.parseInt(ListenDataManager.Instance().textList
                        .get(paragraph).time) * 1000);
                currParagraph = paragraph;
            }
        }
    };

    private ExtendedPlayer getPlayer() {
        try {
            if (getActivity() == null) {
                return null;
            }
            return ((SectionA) getActivity()).getPlayer();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.listen_original, container, false);
        mContext = RuntimeManager.getContext();
        init();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlayer = getPlayer();
        if (mPlayer == null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPlayer = getPlayer();
                    handler.sendEmptyMessage(1);
                }
            }, 1500);
        } else {
            handler.sendEmptyMessage(1);
        }
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
        original.setSubtitleSum(subtitleSum);
        original.setTpstcb(tp);
        original.setSyncho(SettingConfig.Instance().isSyncho());
        /*if (isvip == 1) {
            unvip.setVisibility(View.GONE);
			setSubtitleSum();
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

    public void setSubtitleSum() {
        subtitleSum = new SubtitleSum();
        if (subtitleSum.subtitles == null) {
            subtitleSum.subtitles = new ArrayList<>();
            subtitleSum.subtitles.clear();
        }
        setDetail();
    }

    private void setDetail() {
        ArrayList<CetText> textList = ListenDataManager.Instance().textList;
        if (textList != null) {
            int size = textList.size();
            Subtitle st;
            StringBuffer sb;
            for (int i = 0; i < size; i++) {
                st = new Subtitle();
                sb = new StringBuffer();
                if (!TextUtils.isEmpty(textList.get(i).sex)) {
                    sb.append("\t");
                    sb.append(textList.get(i).sex);
                    sb.append(": ");
                    sb.append(textList.get(i).sentence);
                    sb.append("\n");
                } else {
                    sb.append("\t");
                    sb.append(textList.get(i).sentence);
                    sb.append("\n");
                }
                L.e("---- ====  - -- -  -  " + sb.toString());
                st.content = sb.toString();
                st.testTime = textList.get(i).testTime ;
                if (textList.get(i).time != null) {
                    st.pointInTime = Integer.parseInt(textList.get(i).time);
                }
                subtitleSum.subtitles.add(st);
            }
        }
    }
}
