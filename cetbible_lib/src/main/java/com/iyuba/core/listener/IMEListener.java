/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class IMEListener implements OnTouchListener {

    boolean isShow;
    InputMethodManager input;

    public IMEListener(InputMethodManager _input, boolean _isShow) {
        this.input = _input;
        this.isShow = _isShow;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isShow) {
                    // 显示输入法窗口，这个分支不要使用，当时想多了，系统会自动显示输入法窗口
                    // v.requestFocus();
                    input.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    // 隐藏输入法窗口
                    input.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;

            default:
                break;
        }
        // 如果不拦截其他的touch事件，设为false好
        return false;
    }

}
