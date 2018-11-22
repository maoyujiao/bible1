package com.iyuba.CET4bible.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;

import com.iyuba.CET4bible.R;
import com.iyuba.base.util.ClickableImageSpan;
import com.iyuba.base.util.DisplayUtil;
import com.iyuba.core.activity.CrashApplication;

/**
 * StringUtil
 *
 * @author wayne
 * @date 2017/12/19
 */
public class StringUtil {
    public static int COLOR_ORANGE = Color.parseColor("#FF8A65");
    public static int COLOR_GREEN = Color.parseColor("#96CA27");

    public static CharSequence getSpannableString(Context context, String content,
                                                  SparseArray<String> wordArray,
                                                  SparseArray<Boolean> answerArray,
                                                  View.OnClickListener onClickListener,
                                                  View.OnClickListener explainListener) {
        String[] parts = content.split("___");


        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);

            if (i == parts.length - 1) {
                continue;
            }

            String word = wordArray.get(i, null);
            Boolean answer = answerArray.get(i, null);

            builder.append(" ");
            // 序号
            SpannableString indexString = new SpannableString(i + 1 + ". ");
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(isAnswer(answer) ? COLOR_GREEN : COLOR_ORANGE);
            indexString.setSpan(foregroundColorSpan, 0, indexString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            builder.append(indexString);

            // 单词
            SpannableString wordString;
            if (TextUtils.isEmpty(word)) {
                wordString = new SpannableString("________");
                wordString.setSpan(getClickImageSpan(context, i, onClickListener), 0, wordString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                String aaa = null;
                try {
                    String[] words = word.split("[.]");
                    aaa = words[1].trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    aaa = word;
                }
                wordString = new SpannableString("  " + aaa + "  ");
                wordString.setSpan(new CustomSpan(i, isAnswer(answer), !TextUtils.isEmpty(word), onClickListener), 0, wordString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            builder.append(wordString);

            builder.append(" ");
            // 解析
            if (answer != null) {
                SpannableString explainString = new SpannableString("  ？ ");
                explainString.setSpan(new CustomSpan22(i, answer, explainListener), 0, explainString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                builder.append(explainString);
            }
        }

        return builder;
    }

    public static Object getClickImageSpan(Context context, final int pos, final View.OnClickListener listener) {
        return new ClickableImageSpan(context, R.drawable.finger_line) {
            @Override
            public void onClick(View view) {
                view.setTag(pos);
                listener.onClick(view);
            }
        };
    }

    /**
     * null 为正确的颜色
     */
    private static boolean isAnswer(Boolean answer) {
        return answer == null ? true : answer;
    }

    private static CharSequence getString(int pos, String select) {
        return TextUtils.isEmpty(select) ? "________" : "  " + select + "  ";
    }

    public static CharSequence getSpannableString(Context context, String content,
                                                  View.OnClickListener onClickListener,
                                                  View.OnClickListener explainListener) {
        return getSpannableString(context, content, new SparseArray<String>(), new SparseArray<Boolean>(), onClickListener, explainListener);
    }

    public static CharSequence getSpannableString(Activity mContext, String content,
                                                  SparseArray<String> selectKeys,
                                                  View.OnClickListener onClickListener,
                                                  View.OnClickListener explainListener) {
        return getSpannableString(mContext, content, selectKeys, new SparseArray<Boolean>(), onClickListener, explainListener);
    }

    static class CustomSpan extends ClickableSpan {
        View.OnClickListener listener;
        int pos;
        boolean underline = true;
        boolean answer = true;

        public CustomSpan(int pos, boolean answer, boolean underline, View.OnClickListener listener) {
            this.pos = pos;
            this.listener = listener;
            this.underline = underline;
            this.answer = answer;
        }

        @Override
        public void onClick(View widget) {
            widget.setTag(pos);
            listener.onClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(answer ? COLOR_GREEN : COLOR_ORANGE);
            ds.setUnderlineText(underline);
            ds.setTextSize(DisplayUtil.sp2px(CrashApplication.getInstance(), 17));
            ds.setFakeBoldText(true);
        }
    }

    static class CustomSpan22 extends ClickableSpan {
        View.OnClickListener listener;
        int pos;
        boolean answer;

        public CustomSpan22(int pos, boolean answer, View.OnClickListener listener) {
            this.pos = pos;
            this.listener = listener;
            this.answer = answer;
        }

        @Override
        public void onClick(View widget) {
            widget.setTag(pos);
            listener.onClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setStyle(Paint.Style.FILL_AND_STROKE);
            ds.setColor(answer ? COLOR_GREEN : COLOR_ORANGE);
            ds.setTextSize(DisplayUtil.sp2px(CrashApplication.getInstance(), 16));
            ds.setFakeBoldText(true);
        }
    }
}
