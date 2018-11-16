package com.iyuba.trainingcamp.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.LearningContent;

import java.util.ArrayList;
import java.util.List;


public class WheelView extends ScrollView {
    private int LLHi;
    public static final int TYPE_WORD = 1;
    public static final int TYPE_SENTENCE = 2;
    private int type;
    public static final String TAG = WheelView.class.getSimpleName();

    View stuffView;

    public void setLLHi(int LLHi) {
        this.LLHi = LLHi;
    }

    public static class OnWheelViewListener {
        public void onSelected(int selectedIndex, LearningContent item) {
        }
    }

    private OnScrollListener listener;
    private Context context;
    private LinearLayout views;

    private List<LearningContent> mOriginList;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setType(int type) {
        this.type = type;
    }


    List<LearningContent> items;

    private List<LearningContent> getItems() {
        return items;
    }

    public void setItems(List<LearningContent> list) {
        if (null == items) {
            items = new ArrayList<>();
        }
        mOriginList = list;
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            LearningContent content = new LearningContent();
            content.en = "开启学习之旅";
            content.cn = "Start a journey of Learning";
            items.add(0, content);
            LearningContent contentEnd = new LearningContent();
            contentEnd.en = "";
            contentEnd.cn = "";
            items.add(contentEnd);
            LearningContent contentEndEnd = new LearningContent();
            contentEndEnd.en = "";
            contentEndEnd.cn = "";
            items.add(contentEndEnd);
        }
        if (type == WheelView.TYPE_SENTENCE && getHeight() > itemHeight * (list.size() - 1)) {
            items.remove(items.get(items.size() - 1));
        }
        initData();
    }


    public interface OnScrollListener {

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }

    public void setOnScrollLisener(OnScrollListener lisener) {
        this.listener = lisener;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

    }


    public static final int OFF_SET_DEFAULT = 1;

    public static int getOffSetDefault() {
        return OFF_SET_DEFAULT;
    }


    int offset = 1; // 偏移量（需要在最前面和最后面补全）

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    int displayItemCount; // 每页显示的数量

    int selectedIndex = 1;


    public void init(int type, int height) {
        this.type = type;
        this.LLHi = height;
    }


    private void init(Context context) {
        this.context = context;
        Log.d(TAG, "parent: " + this.getParent());
        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {


                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;

                    if (remainder == 0) {
                        selectedIndex = divided + offset;
                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    if (selectedIndex <= items.size()) {
                                        onSeletedCallBack();
                                    }
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    if (selectedIndex <= items.size()) {
                                        onSeletedCallBack();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    int initialY;
    Runnable scrollerTask;
    int newCheck = 0;

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 3;

        for (int i = 0; i < items.size(); i++) {
            if (type == TYPE_WORD) {
                if (i <= mOriginList.size() + 2) {
                    views.addView(createLinearView(items.get(i), i));
                }
            } else if (type == TYPE_SENTENCE) {
                Log.d(TAG, "initData: " + type);
                views.addView(createLinearView(items.get(i), i));
            }
        }

        refreshItemView(0);
    }


    public int itemHeight = 0;

    public int getItemHeight() {
        return itemHeight;
    }

    private LinearLayout createLinearView(LearningContent item, int i) {
        if (type == TYPE_WORD) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.trainingcamp_gold_word_item, null);
            TextView cn = linearLayout.findViewById(R.id.cn);
            TextView en = linearLayout.findViewById(R.id.en);
            if (i == 1) {
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LLHi));
                en.setText("\n\n");
                cn.setText("\n\n");
            } else {
                en.append(items.get(1).en + "\n");
                cn.append(items.get(1).cn + "\n");
            }

            if (0 == itemHeight) {
                itemHeight = getViewMeasuredHeight(linearLayout);
                Log.d(TAG, "itemHeight: " + itemHeight);
            }
            return linearLayout;
        } else if (type == TYPE_SENTENCE) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.trainingcamp_gold_sentence_item, null);
            TextView cn = linearLayout.findViewById(R.id.cn);
            TextView en = linearLayout.findViewById(R.id.en);
            if (i == 1) {

                Log.d("diao", "run: " + LLHi);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LLHi));
                en.setText("\n\n");
                cn.setText("\n\n");
            } else {
                en.append("\n");
                cn.append("\n");
            }


            if (0 == itemHeight) {
                itemHeight = getViewMeasuredHeight(linearLayout);
                Log.d(TAG, "itemHeight: " + itemHeight);
            }
            return linearLayout;
        }

        return null;
    }


    private TextView createView(LearningContent item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setText(item.en);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(15);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LLHi - itemHeight);
            stuffView = new View(context);
            Log.d(TAG, "itemHeight: " + itemHeight);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.d(TAG, "onScrollChanged: " + t);
        refreshItemView(t);

        if (t > oldt) {
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;
        }
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            LinearLayout linearLayout = (LinearLayout) views.getChildAt(i);
            if (null == linearLayout) {
                return;
            }
            if (position == i) {
                switch (type) {
                    case TYPE_WORD:
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LLHi));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LLHi - itemHeight);
                        linearLayout.findViewById(R.id.stuff).setLayoutParams(lp);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.cn)).setVisibility(View.VISIBLE);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.en)).setVisibility(View.VISIBLE);
                        ((TextView) linearLayout.findViewById(R.id.en)).setTextColor(Color.WHITE);
                        ((TextView) linearLayout.findViewById(R.id.cn)).setTextColor(Color.WHITE);
                        break;
                    case TYPE_SENTENCE:

                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LLHi));
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.cn)).setVisibility(View.VISIBLE);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.en)).setVisibility(View.VISIBLE);
                        ((TextView) linearLayout.findViewById(R.id.en)).setTextColor(Color.WHITE);
                        ((TextView) linearLayout.findViewById(R.id.cn)).setTextColor(Color.WHITE);
                        Log.d(TAG, "refreshItemView: " + "position" + position);
                        break;
                    default:
                        break;
                }

                Log.d(TAG, "onCallBack: " + selectedIndex);


            } else {
                switch (type) {
                    case TYPE_WORD:
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
                        linearLayout.findViewById(R.id.stuff).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.cn)).setVisibility(View.VISIBLE);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.en)).setVisibility(View.VISIBLE);
                        ((TextView) linearLayout.findViewById(R.id.en)).setTextColor(getResources().getColor(R.color.trainingcamp_gray_333));
                        ((TextView) linearLayout.findViewById(R.id.cn)).setTextColor(getResources().getColor(R.color.trainingcamp_gray_999));
                        ((TextView) linearLayout.findViewById(R.id.cn)).setText("");
                        ((TextView) linearLayout.findViewById(R.id.en)).setText("");
                        if (items.size() <= i) {
                            return;
                        }
                        ((TextView) linearLayout.findViewById(R.id.en)).setText(items.get(i).en);
                        ((TextView) linearLayout.findViewById(R.id.cn)).setText(items.get(i).cn);
                        Log.d(TAG, "onononSeletedCallBack: " + selectedIndex);
                        break;
                    case TYPE_SENTENCE:
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.cn)).setVisibility(View.VISIBLE);
                        ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.en)).setVisibility(View.VISIBLE);
                        ((TextView) linearLayout.findViewById(R.id.en)).setTextColor(getResources().getColor(R.color.trainingcamp_gray_333));
                        ((TextView) linearLayout.findViewById(R.id.cn)).setTextColor(getResources().getColor(R.color.trainingcamp_gray_999));
                        ((TextView) linearLayout.findViewById(R.id.cn)).setText("");
                        ((TextView) linearLayout.findViewById(R.id.en)).setText("");
                        ((TextView) linearLayout.findViewById(R.id.en)).setText(items.get(i).en);
                        ((TextView) linearLayout.findViewById(R.id.cn)).setText(items.get(i).cn);
                        Log.d(TAG, "onononSeletedCallBack: " + selectedIndex);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;

    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public int getOpacity() {
                return 0;
            }
        };

        super.setBackgroundDrawable(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.cn)).setText("");
            ((TextView) views.getChildAt(selectedIndex).findViewById(R.id.en)).setText("");
            Log.d(TAG, "onSeletedCallBack: " + selectedIndex);
            if (selectedIndex >= items.size()) {
                return;
            }
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public LearningContent getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (listener != null) {
                listener.onScroll(getScrollY());
            }
        }
        return super.onTouchEvent(ev);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

}
