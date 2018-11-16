package com.iyuba.trainingcamp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.iyuba.trainingcamp.R;

import java.util.List;


/**
 * @author yq QQ:1032006226
 */
public class ScoreCircleView extends View {

    private Context mContext;
    private List<Integer> scores;
    int[][] arr ;
    String []  items= {"词义辨析","口语力" , "听力","听力"};
    Paint paint;
    Path path;
    TypedArray ta ;
    int color ;

    public void setScores(List<Integer> scores) {
        this.scores = scores;
        paint = new Paint();
        path = new Path();


        invalidate();
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public ScoreCircleView(Context context) {
        super(context);
    }

    public ScoreCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int[] attrs1;
        ta = context.obtainStyledAttributes(attrs,R.styleable.TrainingTheme);
        color = ta.getColor(0,context.getResources().getColor(R.color.trainingcamp_theme_color));
        ta.recycle();
    }

    public ScoreCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setAntiAlias(true);
//        给画笔设置颜色
        paint.setColor(getResources().getColor(R.color.trainingcamp_white_inside));
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStrokeWidth(8);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
        paint.setColor(getResources().getColor(R.color.trainingcamp_white_inside2));
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStrokeWidth(8);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() * 5 / 12, paint);
        paint.setColor(getResources().getColor(R.color.trainingcamp_white_inside3));
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStrokeWidth(8);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 3, paint);
        paint.setColor(getResources().getColor(R.color.trainingcamp_white_inside4));
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setStrokeWidth(8);//设置画笔粗细
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 4, paint);
        arr = new int[2][scores.size()];

        arr = generatePoints(scores);
        paint.setColor(color);

        path.moveTo(arr[0][0], arr[1][0]);
        for (int i = 0 ;i < scores.size() ; i++ ){
            if (i == scores.size()-1){
                path.lineTo(arr[0][0], arr[1][0]);
            }else {
                path.lineTo(arr[0][i+1], arr[1][i+1]);
            }
        }

        path.close();
        path.setFillType(Path.FillType.WINDING);
        canvas.drawPath(path, paint);
        paint.setColor(getResources().getColor(R.color.trainingcamp_white_pure));
        paint.setTextSize(SizeUtils.dp2px(11));

        for (int i = 0 ; i < arr[0].length ; i ++){
            if (arr[0][i]<getWidth()/2){
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(items[i], arr[0][i]-20, arr[1][i], paint);
            }else {
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(items[i], arr[0][i]+20, arr[1][i], paint);
            }
        }
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(85);
        int total = 0;
        for (int i : scores) {
            total += i;
        }

//        canvas.drawText(total / 4 + "", getWidth() / 2, getHeight() / 2 + 25, paint);
        super.onDraw(canvas);
    }

    private int[][] generatePoints(List<Integer> scores) {
        int size = scores.size();
        for (int i = 0; i < scores.size(); i++) {
            arr[0][i] = (int) ((Math.cos(Math.toRadians(360 / size * i ))) *
                    (getHeight() / 4 * scores.get(i) / 100 + getHeight() / 4) + getWidth() / 2);   //x轴的坐标
            arr[1][i] = (int) ((Math.sin(Math.toRadians(360 / size * i ))) *
                    (getHeight() / 4 * scores.get(i) / 100 + getHeight() / 4) + getHeight() / 2);    //y轴的坐标

        }
        return arr;
    }
}
