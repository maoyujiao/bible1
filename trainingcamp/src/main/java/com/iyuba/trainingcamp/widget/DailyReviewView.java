package com.iyuba.trainingcamp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.DailyItem;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.TimeUtils;

import java.util.List;

/**
 * @author yq QQ:1032006226
 */
public class DailyReviewView extends LinearLayout {
    List<DailyItem> list;
    Context context;


    private String today;
    ImageView imLearned;
    ImageView imPointer;
    TextView day;
    LinearLayout ll ;
    private DailyReviewInterface mInterface;

    public DailyReviewView(Context context) {

        super(context);
        setWillNotDraw(false);
    }

    public DailyReviewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.context = context;
    }

    public void setDailyList(final List<DailyItem> list, final String time) {

        today = TimeUtils.getCurTime();
        this.list = list;

        removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.trainingcamp_daily, this,false);
            day = view.findViewById(R.id.date_day);
            imLearned = view.findViewById(R.id.date_learned);
            imPointer = view.findViewById(R.id.pointer);
            ll = view.findViewById(R.id.ll);
            addView(view);

            if (!list.get(i).isShow) {
                view.setVisibility(INVISIBLE);
            }

            if (list.get(i).date.length() > 2) {
                day.setText(Integer.parseInt(list.get(i).date.substring(list.get(i).date.length() - 2))+""); //取最后的两位
            }
            if (list.get(i).studySign) {
                imLearned.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_finished_study));
            } else {
                imLearned.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_study_not_finishe));
            }

            if (list.get(i).date.equals(TimeUtils.getCurTime())) {
                imPointer.setVisibility(VISIBLE);
                view.setBackgroundResource(R.drawable.trainingcamp_rect_date);

            } else {
                imPointer.setVisibility(INVISIBLE);
                view.setBackgroundResource(0);
            }

            LogUtils.d("diao", "setDailyList: " + list.get(i).date);

            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currenttime = list.get(finalI).date;
                    for (int iii = 0 ;iii < list.size() ; iii++){
                        getChildAt(iii).findViewById(R.id.pointer).setVisibility(INVISIBLE);
                        getChildAt(iii).findViewById(R.id.ll).setBackgroundResource(0);
                    }
                    v.findViewById(R.id.pointer).setVisibility(VISIBLE);
                    v.findViewById(R.id.ll).setBackgroundResource(R.drawable.trainingcamp_rect_date);

                    invalidate();
                    mInterface.setSelect(list, currenttime);
                }
            });
        }
    }

    public DailyReviewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    public void setDailyReviewInterface(DailyReviewInterface iDaily) {
        this.mInterface = iDaily;
    }

    public interface DailyReviewInterface {
        void setSelect(List<DailyItem> list, String s);
    }
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
//                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
//    }
}
