package com.iyuba.core.me.sqlite.mode;

import com.iyuba.biblelib.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情操作
 *
 * @author 陈彤
 */
public class Emotion {
    public static final String[] express = {"(发呆)", "(微笑)", "(大笑)", "(坏笑)",
            "(偷笑)", "(生气)", "(不)", "(难过)", "(哭)", "(偷瞄)", "(晕)", "(汗)", "(困)",
            "(害羞)", "(惊讶)", "(开心)", "(色)", "(得意)", "(骷髅)", "(囧)", "(睡觉)",
            "(撇嘴)", "(亲)", "(疑问)", "(闭嘴)", "(失望)", "(茫然)", "(努力)", "(鄙视)",
            "(猪头)"};
    public final static HashMap<String, String> map = new HashMap<String, String>();
    private static int[] imageIds = new int[30];

    static {
        map.put("(发呆)", "image1");
        map.put("(微笑)", "image2");
        map.put("(大笑)", "image3");
        map.put("(坏笑)", "image4");
        map.put("(偷笑)", "image5");
        map.put("(生气)", "image6");
        map.put("(不)", "image7");
        map.put("(难过)", "image8");
        map.put("(哭)", "image9");
        map.put("(偷瞄)", "image10");
        map.put("(晕)", "image11");
        map.put("(汗)", "image12");
        map.put("(困)", "image13");
        map.put("(害羞)", "image14");
        map.put("(惊讶)", "image15");
        map.put("(开心)", "image16");
        map.put("(色)", "image17");
        map.put("(得意)", "image18");
        map.put("(骷髅)", "image19");
        map.put("(囧)", "image20");
        map.put("(睡觉)", "image21");
        map.put("(撇嘴)", "image22");
        map.put("(亲)", "image23");
        map.put("(疑问)", "image24");
        map.put("(闭嘴)", "image25");
        map.put("(失望)", "image26");
        map.put("(茫然)", "image27");
        map.put("(努力)", "image28");
        map.put("(鄙视)", "image29");
        map.put("(猪头)", "image30");
    }

    public int[] emotion = {R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8,
            R.drawable.image9, R.drawable.image10, R.drawable.image11,
            R.drawable.image12, R.drawable.image13, R.drawable.image14,
            R.drawable.image15, R.drawable.image16, R.drawable.image17,
            R.drawable.image18, R.drawable.image19, R.drawable.image20,
            R.drawable.image21, R.drawable.image22, R.drawable.image23,
            R.drawable.image24, R.drawable.image25, R.drawable.image26,
            R.drawable.image27, R.drawable.image28, R.drawable.image29,
            R.drawable.image30};

    public Emotion() {
        super();
    }

    /**
     * @param position
     */
    public Emotion(int position) {
        super();
    }

    public static List<HashMap<String, Object>> initEmotion() {
        List<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
        // 生成107个表情的id，封装
        Field field;
        HashMap<String, Object> listItem;
        for (int i = 0; i < 30; i++) {
            try {
                field = R.drawable.class.getDeclaredField("image" + (i + 1));
                int resourceId = Integer.parseInt(field.get(null).toString());
                imageIds[i] = resourceId;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            listItem = new HashMap<String, Object>();
            listItem.put("image", imageIds[i]);
            listItems.add(listItem);
        }
        return listItems;
    }

    public Boolean isExpress(String string) {
        String check = "\\([\\u4e00-\\u9fa5]{1,2}\\)";
        boolean result = Pattern.matches(check, string);
        return result;
    }

    public String replace(String string) {
        String check = "\\([\\u4e00-\\u9fa5]{1,2}\\)";
        if (isExpress(string)) {
            Pattern pattern = Pattern.compile(check);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                String valueString = Emotion.map.get(matcher.group(0));
                string = string.replace(matcher.group(0), valueString);
            }
        }
        return string;
    }
}
