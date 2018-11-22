package com.iyuba.CET4bible.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

//import com.nineoldandroids.view.ViewHelper;

/**
 * 自定义拖拽控件
 *
 * @author poplar
 */
public class CustomDragLayout extends FrameLayout {
    private static final String TAG = "TAG";
    private ViewDragHelper mDragHelper;
    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mWidth;
    // 拖拽范围
    private int mDragRange;
    private int mHeight;
    private Status mStatus = Status.Close;
    private OnDragStateChangeListener mOnDragStateChangeListener;
    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        // 1. 决定了是否要拖拽当前的child
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d(TAG, "tryCaptureView: ");
            return child == mMainContent || child == mLeftContent;
        }

        // 2. 设置拖拽的横向范围（用来决定横向动画执行时间）
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        }

        // 3. 决定了View将要放置的位置（在这里进行位置的修正） *
        // left 只是建议移动到的位置
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // oldLeft + dx = left;
//			int oldLeft = child.getLeft();
//			Log.d("TAG", "clampViewPositionHorizontal: oldLeft: " + oldLeft +" dx: " + dx + " left: " + left );

            if (child == mMainContent) {
                left = fixLeft(left);
            }

            return left;
        }

        // 4. 决定了当View位置被改变时，要做的其他事情（伴随动画）
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            Log.d(TAG, "onViewPositionChanged: " + " dx: " + dx + " left: " + left);
            int mMainleft = mMainContent.getLeft();
            if (changedView != mMainContent) {
                // 如果不是主面板移动，将变化量交给mMainleft
                mMainleft = mMainleft + dx;
            }
            // 修正新的left位置
            mMainleft = fixLeft(mMainleft);

            if (changedView == mLeftContent) {
                mLeftContent.layout(0, 0, mWidth, mHeight);
                mMainContent.layout(mMainleft, 0, mMainleft + mWidth, 0 + mHeight);
            }
            dispatchDragEvent(mMainleft);
            invalidate();
        }

        // 5. 决定了松手之后要处理的事情（View被释放，做动画）
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // releasedChild被释放的孩子
            Log.d(TAG, "onViewReleased: " + " xvel: " + xvel);
            if (xvel > 0) {
                open();
            } else if (xvel == 0 && releasedChild.getLeft() > mDragRange * 0.5f) {
                open();
            } else {
                close();
            }
        }

        private int fixLeft(int left) {
            if (left < 0) {
                return 0;
            } else if (left > mDragRange) {
                return mDragRange;
            }
            return left;
        }
    };

    public CustomDragLayout(Context context) {
        this(context, null);
    }

    public CustomDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 通过静态方法得到一个ViewDragHelper辅助类
        mDragHelper = ViewDragHelper.create(this, 0.2f, mCallback);
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public OnDragStateChangeListener getOnDragStateChangeListener() {
        return mOnDragStateChangeListener;
    }

    public void setOnDragStateChangeListener(
            OnDragStateChangeListener mOnDragStateChangeListener) {
        this.mOnDragStateChangeListener = mOnDragStateChangeListener;
    }

    /**
     * 分发拖拽事件， 执行伴随动画
     *
     * @param mMainleft
     */
    protected void dispatchDragEvent(int mMainleft) {
        float percent = mMainleft * 1.0f / mDragRange;
        Log.d(TAG, "percent: " + percent);
        // 执行动画
        //animViews(percent);
        if (mOnDragStateChangeListener != null) {
            mOnDragStateChangeListener.onDraging(percent);
        }
        // 更新状态
        Status lastStatus = mStatus;
        mStatus = updateStatus(percent);
        if (mStatus != lastStatus && mOnDragStateChangeListener != null) {
            if (mStatus == Status.Close) {
                mOnDragStateChangeListener.onClose();
            } else if (mStatus == Status.Open) {
                mOnDragStateChangeListener.onOpen();
            }
        }
    }

    private Status updateStatus(float percent) {
        if (percent == 0) {
            mStatus = Status.Close;
            Log.e("My mStatus", "close");
        } else if (percent == 1) {
            mStatus = Status.Open;
            Log.e("My mStatus", "open");
        } else {
            Log.e("My mStatus", "draging");
            mStatus = Status.Draging;
        }
        return mStatus;
    }

//    private void animViews(float percent) {
//        //		> 1. 主面板： 缩放动画
//        // 0.0 -> 1.0 >>> 1.0 -> 0.8 >>> (0.2 -> 0.0) + 0.8
//        ViewHelper.setScaleX(mMainContent, (1 - percent) * 0.2f + 0.8f);
//        ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f)); // 另一种计算方式
//
//        //		> 2. 左面板： 缩放动画，平移动画，透明度变化
//        // 0.5 -> 1.0
//        ViewHelper.setScaleX(mLeftContent, percent * 0.5f + 0.5f);
//        ViewHelper.setScaleY(mLeftContent, evaluate(percent, 0.5f, 1.0f));
//        // - mWidth / 2.0f -> 0
//        ViewHelper.setTranslationX(mLeftContent, evaluate(percent, -mWidth / 2.0f, 0f));
//        // 0 -> 1.0
//        ViewHelper.setAlpha(mLeftContent, evaluate(percent, 0.0f, 1.0f));
//
//        //		> 3. 背景： 亮度变化
//        getBackground().setColorFilter(evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
//    }

    /**
     * 颜色从startValue过度到endValue，fraction为分度值（百分比）
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return 中间的过度色值
     */
    private int evaluateColor(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }

    // FloatEvaluator 估值器
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    public void close() {
        close(true);
    }

    public void open() {
        open(true);
    }

    /**
     * 关闭动画
     * isSmooth true为平滑动画
     */
    protected void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            // 触发一个动画，平滑地移动到指定位置
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // 如果是true， 当前还没到指定位置
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
        mStatus = Status.Close;
    }

    /**
     * 开启动画
     */
    protected void open(boolean isSmooth) {
        int finalLeft = mDragRange;
        if (isSmooth) {
            // 触发一个动画，平滑地移动到指定位置
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // 如果是true， 当前还没到指定位置
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
        mStatus = Status.Open;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 高频率的调用,自动化重复调用，使动画继续
        Log.d(TAG, "computeScroll");
        if (mDragHelper.continueSettling(true)) {
            // 如果是true， 当前还没到指定位置
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    // 由ViewDragHelper判断是否需要拦截事件
    @Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 由ViewDragHelper决定如何处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 健壮性检查
        int childCount = getChildCount();
        if (childCount < 2) {
            throw new IllegalStateException("你需要至少两个子View！You need 2 children at least!");
        }
        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("你的孩子必须是ViewGroup的子类！Your children must be instance of ViewGroup");
        }
        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMainContent.getMeasuredWidth();
        mHeight = mMainContent.getMeasuredHeight();
        mDragRange = (int) (mWidth * 0.6f);
    }

    public enum Status {
        Open, Close, Draging
    }

    public interface OnDragStateChangeListener {
        /**
         * 开启监听
         */
        void onOpen();

        /**
         * 关闭监听
         */
        void onClose();

        /**
         * 拖拽监听
         *
         * @param percent 当前动画执行的百分比
         */
        void onDraging(float percent);
    }

}
