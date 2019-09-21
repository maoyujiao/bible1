package com.iyuba.abilitytest.widget;
/**
 * @author mingyu
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.iyuba.abilitytest.R;

public class CustomBgView extends View {
    private Bitmap mImage;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private String mText;
    private String mSubText;
    private int paddingTop;
    private int paddingLeft;
    private Context mContext;

    public CustomBgView(Context context) {
        this(context, null);
    }

    public CustomBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomBgView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        mContext = context;
        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.winter);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mContext.getResources().getDimension(R.dimen.textsize));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

//        mPaint.setColor(Color.parseColor("#E8ECEF"));
        mPaint.setColor(Color.WHITE);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        mPaint.setShadowLayer(5, 3, 3, Color.GRAY);
    }

    public void setBg(int imageId) {
        mImage = BitmapFactory.decodeResource(getResources(), imageId);
        invalidate();
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setSubText(String text) {
        mSubText = text;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Rect rect = new Rect(paddingLeft, paddingTop, mWidth, mHeight);
        canvas.drawBitmap(mImage, null, rect, mPaint);
        canvas.drawText(mText, mContext.getResources().getDimension(R.dimen.marginX_title), mContext.getResources().getDimension(R.dimen.marginY_title), mPaint);
        canvas.drawText(mSubText, mContext.getResources().getDimension(R.dimen.marginX_subTitle), mContext.getResources().getDimension(R.dimen.marginY_subTitle), mPaint);

    }


}
