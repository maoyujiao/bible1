package com.iyuba.abilitytest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.util.LogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Liuzhenli on 2016/9/19.
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    /**
     * 获取屏幕的宽
     *
     * @param context 上下文
     * @return 屏幕的高度
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的密度
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 让某几个文字显示颜色
     *
     * @param str 字符串
     */
    public static void showTextWithColor(TextView v, String str) {
        if (str.contains("[[") && str.contains("]]")) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xff000000);//前景色
            String wordsor = str.replace("[[", "").replace("]]", "");
            SpannableStringBuilder words = new SpannableStringBuilder(wordsor);
            words.setSpan(colorSpan, str.indexOf("[["), str.indexOf("]]") - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            v.setText(words);
        } else {
            v.setText(str);
        }
    }

    public static void showTextWithUnderLine(TextView textView, String string) {
        if (string.contains("[[") && string.contains("]]")) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xff000000);
            UnderlineSpan underlineSpan = new UnderlineSpan();
            String wordsor = string.replace("[[", "").replace("]]", "");
            SpannableStringBuilder spanString = new SpannableStringBuilder(wordsor);
            //变颜色
            spanString.setSpan(colorSpan, string.indexOf("[["), string.indexOf("]]") - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //下划线
            spanString.setSpan(underlineSpan, string.indexOf("[["), string.indexOf("]]") - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spanString);
        } else {
            textView.setText(string);
        }
    }

    public static Bitmap getImageBitmap(Context context, String imagePathString) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        LogUtils.e("density:   " + density);
        // int screenWidth = (int)
        // (mContext.getWindowManager().getDefaultDisplay().getWidth() * 0.95);
        // int screenHeight = (int) (getWindowManager().getDefaultDisplay()
        // .getHeight());
        //Bitmap bitmap = ImageUtil.getBitmap(mContext, imagePathString);
        try {
            FileInputStream fis = new FileInputStream(imagePathString);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断距离上次测试时是否够一个星期
     *
     * @param time 上次测试的时间
     * @param days 时间间隔
     * @return true--大于规定的时间  false--小于规定的时间
     */
    public static boolean timeOver(String time, int days) {
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
        String last = time.substring(0, 10);//当前测试时间精确到日
        try {
            Date testtime = formatter3.parse(last);
            long nexttime = testtime.getTime() + days * 24 * 60 * 60 * 1000;//一个星期之后的测试时间  秒
            long today = System.currentTimeMillis();//获取当前的时间
            return today >= nexttime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean alreadyFinshTodayTest(String testType) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String today = format.format(now);
        String lastTime = ConfigManager.Instance().loadString("abilityTest-" + testType);
        LogUtils.e(TAG, "今天有没有做: " + (!lastTime.equals(today)));
        return (!lastTime.equals(today) ||ignoreCount(Constant.mListen));
    }


    public static boolean ignoreCount(String couseName){
        return couseName.equals("GZ1")||couseName.equals("GZ2");
    }

    /**
     * 根据数据表里存放的编号,获取lessonType
     *
     * @param type 数据存储时的编号   0写作  1单词  语法....
     * @return
     */
    public static String getLessonType(int type) {
        String lessontype = "";
        switch (type) {
            case Constant.ABILITY_TETYPE_WRITE:
                lessontype = Constant.ABILITY_WRITE;
                break;
            case Constant.ABILITY_TETYPE_WORD:
                lessontype = Constant.ABILITY_WORD;
                break;
            case Constant.ABILITY_TETYPE_GRAMMAR:
                lessontype = Constant.ABILITY_GRAMMAR;
                break;
            case Constant.ABILITY_TETYPE_LISTEN:
                lessontype = Constant.ABILITY_LISTEN;
                break;
            case Constant.ABILITY_TETYPE_SPEAK:
                lessontype = Constant.ABILITY_SPEAK;
                break;
            case Constant.ABILITY_TETYPE_READ:
                lessontype = Constant.ABILITY_READ;
                break;
        }
        LogUtils.e("这里的lessonType    " + lessontype);
        return lessontype;
    }

    /**
     * 根据数据表里存放的编号,获取lessonType
     *
     * @param categoty L X W R...
     * @return 数据存储时的编号   0写作  1单词  语法....
     */
    public static int getAbilityCategoryId(String categoty) {
        int catetoryId = 0;
        switch (categoty) {
            case Constant.ABILITY_WRITE:
                catetoryId = Constant.ABILITY_TETYPE_WRITE;
                break;
            case Constant.ABILITY_WORD:
                catetoryId = Constant.ABILITY_TETYPE_WORD;
                break;
            case Constant.ABILITY_GRAMMAR:
                catetoryId = Constant.ABILITY_TETYPE_GRAMMAR;
                break;
            case Constant.ABILITY_LISTEN:
                catetoryId = Constant.ABILITY_TETYPE_LISTEN;
                break;
            case Constant.ABILITY_SPEAK:
                catetoryId = Constant.ABILITY_TETYPE_SPEAK;
                break;
            case Constant.ABILITY_READ:
                catetoryId = Constant.ABILITY_TETYPE_READ;
                break;
        }
        LogUtils.e("这里的categoryId    " + catetoryId);
        return catetoryId;
    }

    public static String getAbilityTitleName(String categoty) {
        String catetoryId = "测试";
        switch (categoty) {
            case Constant.ABILITY_WRITE:
                catetoryId = "写作";
                break;
            case Constant.ABILITY_WORD:
                catetoryId = "单词";
                break;
            case Constant.ABILITY_GRAMMAR:
                catetoryId = "语法";
                break;
            case Constant.ABILITY_LISTEN:
                catetoryId = "听力";
                break;
            case Constant.ABILITY_SPEAK:
                catetoryId = "口语";
                break;
            case Constant.ABILITY_READ:
                catetoryId = "阅读";
                break;
        }
        LogUtils.e("titleName   " + catetoryId);
        return catetoryId;
    }
}
