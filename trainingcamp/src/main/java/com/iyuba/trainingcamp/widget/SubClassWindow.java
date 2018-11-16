package com.iyuba.trainingcamp.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.LessonTitleAdapter;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LessonIdBean;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.widget
 * @class describe
 * @time 2018/11/5 15:43
 * @change
 * @chang time
 * @class describe
 */
public class SubClassWindow {

    private RecyclerView recyclerView ;
    private LessonTitleAdapter mAdapter ;
    static PopupWindow window ;
    Context context ;


    public void createSubClassWindow(final Context context , List<LessonIdBean.LessonListBean> list){
        this.context = context;
        // 用于PopupWindow的View
        View contentView= LayoutInflater.from(context).inflate(R.layout.trainingcamp_subclass, null);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点

        recyclerView = contentView.findViewById(R.id.recyclerView);
        mAdapter = new LessonTitleAdapter(list , window);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
         window=new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight(context)*586/732, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f,context);
            }
        });
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(anchor, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);

    }



    public  void show(View view){
        window.setAnimationStyle(R.style.anim_photo_select);
        setBackgroundAlpha(0.5f,context);
//        int windowPos[] = calculatePopWindowPos(view, ((Activity)context).getWindow().getDecorView());
        window.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.TOP | Gravity.START, 0, getScreenHeight(context));
//        window.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.);
    }

    public void dismiss(){
        window.dismiss();
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
