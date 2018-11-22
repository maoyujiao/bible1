package com.iyuba.CET4bible.write;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.util.TextAttr;
import com.umeng.analytics.MobclickAgent;

public class WriteCommentFragment extends Fragment {
    private Context mContext;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.write_comment, container, false);
        mContext = getContext();
        init();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private void init() {
        int isvip = ConfigManager.Instance().loadInt("isvip");
        View unvip = root.findViewById(R.id.un_vip);
        if (isvip != 0) {
            unvip.setVisibility(View.GONE);
            TextView text = root.findViewById(R.id.comment);
            if (WriteDataManager.Instance().write.comment != null) {
                String content = WriteDataManager.Instance().write.comment;
                content = content.replaceAll("\\+\\+", "\n");
                text.setText(TextAttr.ToDBC(content));
            } else {
                text.setText("暂无");
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
