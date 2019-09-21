package com.iyuba.core.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.iyuba.biblelib.R;
import com.iyuba.core.listener.ResultIntCallBack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 上下文菜单 可复用 传入操作字符串数组 以及 rusultintcallback监听
 *
 * @author 陈彤
 */
@SuppressLint("Instantiatable")
public class ContextMenu extends LinearLayout {
    private View cancle, back, root, content;
    private ListView oper;
    private String[] operText;
    private Context mContext;
    private ResultIntCallBack mListener;

    public ContextMenu(Context context) {
        super(context);
        mContext = context;
        LayoutInflater vi = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = vi.inflate(R.layout.context_menu, this);

    }

    public ContextMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater vi = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = vi.inflate(R.layout.context_menu, this);
    }

    public void setText(String[] array) {
        operText = array;
    }

    public void setCallback(ResultIntCallBack menuResultListener) {
        mListener = menuResultListener;
        init();
    }

    private void init() {
        cancle = findViewById(R.id.cancle_layout);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dismiss();
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dismiss();
            }
        });
        content = findViewById(R.id.content);
        oper = findViewById(R.id.list);
        initList();
    }

    private void initList() {
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < operText.length; i++) {
            map = new HashMap<>();
            map.put("ItemText", operText[i]);// 按序号做ItemText
            lstImageItem.add(map);
        }
        SimpleAdapter saImageItems = new SimpleAdapter(mContext, lstImageItem,
                R.layout.item_context_menu, new String[]{"ItemText"},
                new int[]{R.id.text});
        oper.setAdapter(saImageItems);
        oper.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                mListener.setResult(arg2);
                dismiss();
            }
        });
    }

    public void show() {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in);
        root.startAnimation(animation);
        root.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_in);
        content.startAnimation(animation);
    }

    public void dismiss() {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.fade_out);
        root.startAnimation(animation);
        root.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_out);
        content.startAnimation(animation);
    }

}
