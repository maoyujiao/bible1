package com.iyuba.base.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iyuba.base.R;

/**
 * SimpleLine
 */
public class SimpleLineDividerDecoration extends RecyclerView.ItemDecoration {

    private int mDefaultColor = R.color.base_divider_color;
    private int dividerHeight = 2;
    private Paint dividerPaint;
    private Context context ;

    public SimpleLineDividerDecoration(Context context) {
        dividerPaint = new Paint();
        this.context = context ;
        dividerPaint.setColor(context.getResources().getColor(mDefaultColor));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft()+DisplayUtil.dip2px(context, 30);
        int right = parent.getWidth() - DisplayUtil.dip2px(context, 30);

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    public RecyclerView.ItemDecoration setColor(int color) {
        mDefaultColor = color;
        return this;
    }
}