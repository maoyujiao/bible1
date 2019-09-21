package com.iyuba.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.iyuba.base.util.DisplayUtil;
import com.iyuba.biblelib.R;

/**
 * 下载圆形进度条
 *
 * @author 陈彤
 */
public class RoundProgressBar extends View {

    public static final int STROKE = 0;
    public static final int FILL = 1;
    private Paint paint;
    private int roundColor;
    private int roundProgressColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    private int max;
    private int progress;
    private boolean textIsDisplayable;
    private int style;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.circleProgressBar);
        roundColor = mTypedArray.getColor(
                R.styleable.circleProgressBar_circleColor, getResources().getColor(R.color.darkgray));
        roundProgressColor = mTypedArray.getColor(
                R.styleable.circleProgressBar_circleProgressColor,getResources().getColor(R.color.colorPrimary));
        textColor = mTypedArray.getColor(
                R.styleable.circleProgressBar_textColor, getResources().getColor(R.color.colorPrimary));
        textSize = mTypedArray.getDimension(
                R.styleable.circleProgressBar_textSize, DisplayUtil.dip2px(context, 10));
        roundWidth = mTypedArray.getDimension(
                R.styleable.circleProgressBar_circleWidth, 2);
        max = mTypedArray.getInteger(R.styleable.circleProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(
                R.styleable.circleProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.circleProgressBar_style, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = (int) (centre - roundWidth / 2 - 3);
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DisplayUtil.dip2px(getContext() , roundWidth));
        paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, radius, paint);
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        int percent = (int) (((float) progress / (float) max) * 100);
        float textWidth = paint.measureText(String.valueOf(percent));
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(String.valueOf(percent), centre - textWidth / 2,
                    centre + textSize / 2, paint);
        }
        paint.setStrokeWidth(DisplayUtil.dip2px(getContext() , roundWidth));
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);
        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, (float) (360.0 / max * progress), false,
                        paint);
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, (float) (360.0 / max * progress), true,
                            paint);
                break;
            }
        }
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
