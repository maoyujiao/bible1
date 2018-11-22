package com.iyuba.CET4bible.write;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.util.ReadBitmap;
import com.iyuba.core.util.TextAttr;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WriteQuestionFragment extends Fragment {
    private Context mContext;
    private View root;
    private ImageView questionImg;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.write_question, container, false);
        mContext = RuntimeManager.getContext();
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
        questionImg = root.findViewById(R.id.question_pic);
        text = root.findViewById(R.id.question);
        try {
            File pic = new File(Constant.picSrcAddr
                    + WriteDataManager.Instance().write.image + ".jpg");
            if (pic.exists()) {
                questionImg.setImageBitmap(ReadBitmap.readBitmap(mContext,
                        new FileInputStream(pic)));
            }
        } catch (FileNotFoundException e) {
        }
        String content = WriteDataManager.Instance().write.question;
        text.setText(TextAttr.ToDBC(content));
    }
}
