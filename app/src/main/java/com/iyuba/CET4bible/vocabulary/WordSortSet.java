/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.vocabulary;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.InitialListAdapter;
import com.iyuba.CET4bible.manager.WordDataManager;
import com.iyuba.CET4bible.widget.ListViewForScrollView;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WordSortSet extends BasisActivity implements OnClickListener,
        OnItemClickListener {
    private ScrollView view;
    private Context mContext;
    private RadioButton basic, random, root, zeroStar, oneStar, twoStar, threeStar;
    private View basicView, randomView, rootView, zeroStarView, oneStarView, twoStarView,
            threeStarView;
    private ListViewForScrollView list;
    private InitialListAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private String old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_set);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        view = findViewById(R.id.scrollview);
        view.smoothScrollTo(0, 0);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        old = String.valueOf(WordDataManager.Instance().cate);
        init();
    }

    private void init() {

        basicView = findViewById(R.id.basic);
        basic = findViewById(R.id.default_radio);
        randomView = findViewById(R.id.random);
        random = findViewById(R.id.random_radio);
        rootView = findViewById(R.id.root);
        root = findViewById(R.id.root_radio);
        TextView txt_letter = findViewById(R.id.txt_letter);
        if (!BuildConfig.isEnglish) {
            rootView.setVisibility(View.GONE);
            findViewById(R.id.line_root).setVisibility(View.GONE);
            txt_letter.setText("按照假名筛选");
        }

//		oneStarView = findViewById(R.id.one_star);
//		oneStar = (RadioButton) findViewById(R.id.one_star_radio);
//		twoStarView = findViewById(R.id.two_star);
//		twoStar = (RadioButton) findViewById(R.id.two_star_radio);
//		threeStarView = findViewById(R.id.three_star);
//		threeStar = (RadioButton) findViewById(R.id.three_star_radio);
//		zeroStarView = findViewById(R.id.zero_star);
//		zeroStar = (RadioButton) findViewById(R.id.zero_star_radio);
        list = findViewById(R.id.initial);
        basicView.setOnClickListener(this);
        randomView.setOnClickListener(this);
        rootView.setOnClickListener(this);
//		oneStarView.setOnClickListener(this);
//		twoStarView.setOnClickListener(this);
//		threeStarView.setOnClickListener(this);
//		zeroStarView.setOnClickListener(this);
        basic.setOnClickListener(this);
        random.setOnClickListener(this);
        root.setOnClickListener(this);
//		oneStar.setOnClickListener(this);
//		twoStar.setOnClickListener(this);
//		threeStar.setOnClickListener(this);
//		zeroStar.setOnClickListener(this);
        adapter = new InitialListAdapter(mContext);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setSelected();
    }

    private void setSelected() {
        if (WordDataManager.Instance().cate.equals("0")) {
            basic.setChecked(true);
        } else if (WordDataManager.Instance().cate.equals("1")) {
            random.setChecked(true);
        } else if (WordDataManager.Instance().cate.equals("2")) {
            root.setChecked(true);
        }
//		else if(WordDataManager.Instance().cate.equals("10")){
//			zeroStar.setChecked(true);
//		}else if(WordDataManager.Instance().cate.equals("11")){
//			oneStar.setChecked(true);
//		}else if(WordDataManager.Instance().cate.equals("12")){
//			twoStar.setChecked(true);
//		}else if(WordDataManager.Instance().cate.equals("13")){
//			threeStar.setChecked(true);
//		}
        else {
            adapter.setSelected(String.valueOf(WordDataManager.Instance().cate));
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (old.equals(WordDataManager.Instance().cate)) {
        } else {
            WordDataManager.Instance().number = 0;
        }
    }

    private void resetRadioButton() {
        basic.setChecked(false);
        random.setChecked(false);
        root.setChecked(false);
//		zeroStar.setChecked(false);
//		oneStar.setChecked(false);
//		twoStar.setChecked(false);
//		threeStar.setChecked(false);
    }

    @Override
    public void onClick(View arg0) {

        resetRadioButton();
        switch (arg0.getId()) {
            case R.id.basic:
            case R.id.default_radio:
                basic.setChecked(true);
                WordDataManager.Instance().cate = "0";
                finish();
                break;
            case R.id.random:
            case R.id.random_radio:
                random.setChecked(true);
                WordDataManager.Instance().cate = "1";
                finish();
                break;
            case R.id.root:
            case R.id.root_radio:
                root.setChecked(true);
                WordDataManager.Instance().cate = "2";
                finish();
                break;
//		case R.id.zero_star:
//		case R.id.zero_star_radio:
//			zeroStar.setChecked(true);
//			WordDataManager.Instance().cate = "10";
//			finish();
//			break;
//		case R.id.one_star:
//		case R.id.one_star_radio:
//			oneStar.setChecked(true);
//			WordDataManager.Instance().cate = "11";
//			finish();
//			break;
//		case R.id.two_star:
//		case R.id.two_star_radio:
//			WordDataManager.Instance().cate = "12";
//			twoStar.setChecked(true);
//			finish();
//			break;
//		case R.id.three_star:
//		case R.id.three_star_radio:
//			WordDataManager.Instance().cate = "13";
//			threeStar.setChecked(true);
//			finish();
//			break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        resetRadioButton();
        WordDataManager.Instance().cate = (String) adapter.getItem(arg2);
        adapter.setSelected(WordDataManager.Instance().cate);
        finish();
    }
}
