package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.util.TextAttr;
import com.umeng.analytics.MobclickAgent;

public class ListenCommentFragment extends Fragment {
    private Context mContext;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.write_comment, container, false);
        mContext = getContext();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    public void init() {
        int isvip = ConfigManager.Instance().loadInt("isvip");
        View unvip = root.findViewById(R.id.un_vip);
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
}
