package com.iyuba.CET4bible.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iyuba.CET4bible.R;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.util.ReadBitmap;

public class HelpFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private int mContent;

    public static HelpFragment newInstance(int content) {
        HelpFragment fragment = new HelpFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getInt(KEY_CONTENT);
        }
        View root = inflater.inflate(R.layout.help_fragment, container, false);
        ImageView iv = root.findViewById(R.id.iv);
        iv.setImageBitmap(ReadBitmap.readBitmap(RuntimeManager.getContext(),
                mContent));
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, mContent);
    }
}
