package com.iyuba.abilitytest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.iyuba.abilitytest.utils.CommonUtils;
import com.iyuba.core.util.LogUtils;

import java.util.ArrayList;

/**
 * 绘制圆形图谱的工具类
 *
 * @author liuzhenli
 * @version 1.0.0
 */

public class DrawView extends View {
    private static final String TAG = "DrawView";
    private static int[] result;
    private static String[] mAbilityType;
    private int centerX;//圆心x的坐标
    private int centerY;//圆心y的坐标
    private static int mCorners;
    private Context mContext;
    //用户分数几个顶点的坐标 r为半径
    private int r;
    /****
     * 实心小圆点的半径
     */
    private int r_small = 5;
    private float rate;

    private ArrayList<Coordinates> cdList;//坐标

    public DrawView(Context context) {
        super(context);
        mContext = context;
        cdList = new ArrayList<>();
        float dens = context.getResources().getDisplayMetrics().density;//屏幕的密度
        rate = dens / 2.0f;//测试手机是在密度2.0的手机上  其他手机上适配在此基础上等比例缩放
        r = (int) (200 * rate);
        centerX = CommonUtils.getScreenWidth(context) / 2;
        centerY = (int) (300 * rate);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置数据
     *
     * @param abilityType 包含的能力
     * @param res         答题记录
     */
    public void setData(String[] abilityType, int[] res) {
        result = res;
        mAbilityType = abilityType;
        mCorners = abilityType.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
         *  drawLine 绘制直线 drawPoin 绘制点
         */
        PathEffect effects = new DashPathEffect(new float[]{20, 1, 1, 1}, 2);
        // 创建画笔
        Paint p = new Paint();
        // p.setColor(Color.RED);// 设置红色
        p.setARGB(0xFF, 0x6C, 0xC5, 0xD9);//原始浅绿色
        //  p.setARGB(0xff, 0x34, 0x34, 0x34);//灰色
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(1);

        Paint p_purple = new Paint();
        p_purple.setARGB(0xFF, 0x91, 0x2b, 0xD5);
        p_purple.setStyle(Paint.Style.STROKE);
        p_purple.setPathEffect(effects);
        p_purple.setStrokeWidth(1);


        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除
        canvas.drawCircle(centerX, centerY, 20 * rate, p);// 大圆 10  float cx, float cy, float radius, Paint paint
        canvas.drawCircle(centerX, centerY, 40 * rate, p);// 大圆 20
        canvas.drawCircle(centerX, centerY, 60 * rate, p);// 大圆30
        canvas.drawCircle(centerX, centerY, 80 * rate, p);// 大圆 40
        canvas.drawCircle(centerX, centerY, 100 * rate, p);// 大圆 50
        canvas.drawCircle(centerX, centerY, 120 * rate, p_purple);// 大圆 60
        canvas.drawCircle(centerX, centerY, 140 * rate, p);// 大圆
        canvas.drawCircle(centerX, centerY, 160 * rate, p);// 大圆
        canvas.drawCircle(centerX, centerY, 180 * rate, p);// 大圆
        canvas.drawCircle(centerX, centerY, 200 * rate, p);// 大圆

        drawMap(canvas, p, mCorners);//自定义边数
    }

    /**
     * 绘制能力图谱
     *
     * @param cvs   画布
     * @param paint 画笔
     * @param cor   边的条数
     */
    private void drawMap(Canvas cvs, Paint paint, int cor) {
        int textSize = (int) (16 * mContext.getResources().getDisplayMetrics().scaledDensity);
        paint.reset();
        paint.setARGB(0xff, 0x6C, 0xC5, 0xD9);//测评模块的浅蓝色
        paint.setTextSize(textSize);

        Paint paintGray = new Paint();
        paintGray.setColor(Color.GRAY);
        paintGray.setTextSize(textSize * 1.1f);

        Paint paint_text = new Paint();
        paint_text.setARGB(0xff, 0x34, 0x34, 0x34);
        paint_text.setTextSize(textSize);

        float f = (float) Math.toRadians(360 / cor);//相邻两条线的夹角
        float tempf = (float) (Math.toRadians(90));//转化为弧度
        float[] x = new float[cor];//小圆点坐标
        float[] y = new float[cor];

        float[] xScore = new float[cor];//维度文字x坐标
        float[] yScore = new float[cor];//维度文字y坐标

        float[] xText = new float[cor];//画文字的左上角X坐标
        float[] yText = new float[cor];//画文字的左上角Y坐标

        if (cdList.size() > 0) cdList.clear();
        Path path = new Path();
        for (int i = 0; i < cor; i++) {
            Coordinates cd = new Coordinates();
            //从圆心向顶点画直线
            x[i] = (float) (centerX + r * Math.cos(f * i + tempf));
            y[i] = (float) (centerY - r * Math.sin(f * i + tempf));
            cvs.drawLine(centerX, centerY, x[i], y[i], paint);

            if (result[i] > 100) {
                result[i] = 100;
            }
            //写文字的时候根据文字位置需要重新定义起始位置  顶为0 逆时针
            String text2Draw = mAbilityType[i];//写的文字内容
            String score2Draw = "(" + result[i] + ")";//得分
            xText[i] = x[i];//分数文字x坐标
            yText[i] = y[i];//分数文字y坐标
            xScore[i] = x[i];
            yScore[i] = y[i];

            float textHeight = getTextHeight(text2Draw, textSize);//文字高度
            float scoreHeight = getTextHeight(score2Draw, textSize);//文字高度
            float textWidth = getTextWidth(text2Draw, textSize);//文字宽度
            float scoreWidth = getTextWidth(score2Draw, textSize);//文字宽度

            if (yText[i] > centerY) {//圆心下侧
                yText[i] = y[i] + textHeight / 2;
                yScore[i] = y[i] + 3 * scoreHeight / 2;
            } else if (yText[i] < centerY) {//圆心上侧
                yText[i] = y[i] - textHeight;
                yScore[i] = y[i];
            } else {//圆心正左/右方
                yText[i] = y[i] + textHeight / 2;
                yScore[i] = y[i] + 3 * scoreHeight / 2;
            }
            if (xText[i] > centerX) { //圆心右侧
                xText[i] = x[i];
                xScore[i] = x[i] + (textWidth - scoreWidth) / 2;
            } else if (xText[i] < centerX) {//圆心左侧
                xText[i] = x[i] - textWidth;
                xScore[i] = x[i] - (scoreWidth + textWidth) / 2;
            } else {//圆心正上/下侧
                xText[i] = x[i] - textWidth / 2;
                xScore[i] = x[i] - scoreWidth / 2;
                // 正上方/正下方
                yText[i] = yText[i] < centerY ? y[i] - 4 * textHeight / 3 : y[i] + textHeight;
                yScore[i] = yScore[i] < centerY ? y[i] - scoreHeight / 3 : y[i] + 2 * scoreHeight;
            }

            switch (cor) {
                case 4:
                    if (i == 1 || i == 3) {
                        yText[i] = y[i] + textHeight / 4;
                        yScore[i] = y[i] + 5 * scoreHeight / 4;
                    }
                    break;
            }
            if (curIndex == i) {
                cvs.drawText(text2Draw, xText[i], yText[i], paintGray);
                cvs.drawText(score2Draw, xScore[i], yScore[i], paintGray);
            } else {
                cvs.drawText(text2Draw, xText[i], yText[i], paint_text);
                cvs.drawText(score2Draw, xScore[i], yScore[i], paint_text);
            }

            cd.Xstart = xText[i] < xScore[i] ? xText[i] - 20 * rate : xScore[i] - 20 * rate;
            cd.Ystart = yText[i] - 20 * rate;
            cd.Xend = xText[i] > xScore[i] ? xText[i] + textWidth + 20 * rate : xScore[i] + scoreWidth + 20 * rate;
            cd.Yend = yScore[i] + scoreHeight + 20 * rate;
            cdList.add(cd);

            //与得分关联,1.画顶点小圆   2.画封闭面积
            if (result[i] > 100) result[i] = 100;
            x[i] = (float) (centerX + r * Math.cos(f * i + tempf) * result[i] / 100);
            y[i] = (float) (centerY - r * Math.sin(f * i + tempf) * result[i] / 100);
            paint.setARGB(0x4D, 0x6C, 0xC5, 0xD9);
            //paint.setColor(Color.RED);
            if (i == 0) {
                path.moveTo(x[i], y[i]);
            } else if (i < cor - 1) {
                path.lineTo(x[i], y[i]);
            } else {
                path.lineTo(x[i], y[i]);
                path.close();//封闭
                cvs.drawPath(path, paint);
            }
            paint.setARGB(0xff, 0x6C, 0xC5, 0xD9);
            cvs.drawCircle(x[i], y[i], r_small, paint);//顶点小圆
        }
    }

    /***
     * 获取文字的宽度
     */
    private float getTextWidth(String text, int texSize) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(texSize);
        return paint.measureText(text);
    }

    /**
     * 获取文字的高度
     */
    private float getTextHeight(String text, int textsize) {
        TextPaint p = new TextPaint();
        p.setTextSize(textsize);
        Paint.FontMetrics fm = p.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent) + 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int curIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curIndex = getArrayId(event.getX(), event.getY());
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                curIndex = -1;
                int id = getArrayId(event.getX(), event.getY());
                LogUtils.e(TAG, "idididid:  " + id);
                if (listener != null) {
                    listener.itemSelected(id);
                    invalidate();
                } else {
                    LogUtils.e(TAG, "listenner 空了");
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public int getArrayId(float rawX, float rawY) {
        int arrayId = -1;
        for (int i = 0; i < cdList.size(); i++) {
            if (rawX >= cdList.get(i).Xstart
                    && rawX <= cdList.get(i).Xend
                    && rawY >= cdList.get(i).Ystart
                    && rawY <= cdList.get(i).Yend) {
                arrayId = i;
            }
        }
        return arrayId;
    }

    private PositionClickListener listener;

    public void setOnPositinoClickListener(PositionClickListener lis) {
        this.listener = lis;
    }

    public interface PositionClickListener {
        void itemSelected(int itmeId);
    }
}
