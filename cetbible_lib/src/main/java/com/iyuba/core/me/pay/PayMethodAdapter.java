package com.iyuba.core.me.pay;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;

public class PayMethodAdapter extends BaseAdapter {
    private static final String TAG = PayMethodAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private String[] methods, hints;
    private int[] imageIds;
    private boolean[] selections;

    public PayMethodAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        TypedArray ta = context.getResources().obtainTypedArray(R.array.pay_methods_images);
        int length = ta.length();
        imageIds = new int[length];
        selections = new boolean[length];
        for (int i = 0; i < length; i++) {
            imageIds[i] = ta.getResourceId(i, 0);
            selections[i] = (i == 0);
        }
        ta.recycle();
        methods = context.getResources().getStringArray(R.array.pay_methods);
        hints = context.getResources().getStringArray(R.array.pay_method_hints);
    }

    public void changeSelectPosition(int position) {
        resetSelection();
        selections[position] = true;
    }

    private void resetSelection() {
        for (int i = 0; i < selections.length; i++)
            selections[i] = false;
    }

    public int getSelectedPayway() {
        int selectedPosition = 0;
        for (int i = 0; i < selections.length; i++) {
            if (selections[i] == true) {
                selectedPosition = i;
                break;
            }
        }
        return PayMethodHelper.mapPositionToMethod(selectedPosition);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return methods[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pay_method, null);
        }
        ImageView methodIcon = ViewHolder.get(convertView, R.id.order_pay_method_icon);
        TextView methodName = ViewHolder.get(convertView, R.id.pay_by_method_text);
        TextView methodHint = ViewHolder.get(convertView, R.id.pay_by_method_text_hint);
        CheckBox methodBox = ViewHolder.get(convertView, R.id.pay_by_method_checkbox);
        methodIcon.setImageResource(imageIds[position]);
        methodName.setText(methods[position]);
        methodHint.setText(hints[position]);
        methodBox.setChecked(selections[position]);
        return convertView;
    }

    public interface PayMethod {
        int ALIPAY = 0;
        int WEIXIN = 1;
        int BANKCARD = 2;
    }
}
