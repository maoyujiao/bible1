package com.iyuba.core.teacher.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;

public class SelectQuestionType extends BaseActivity {

//    private static final String[] question_app_type_arr =
//            {"全部", "VOA", "BBC", "听歌", "CET4", "CET6",
//                    "托福", "N1", "N2", "微课", "雅思", "初中",
//                    "高中", "考研", "新概念", "走遍美国"};
    private static final String[] question_app_type_arr =
            {"全部", "VOA", "BBC", "听歌", "CET4", "CET6",
                    "托福", "N1", "N2", "N3", "微课", "雅思", "初中",
                    "高中", "考研", "新概念", "走遍美国", "英语头条"};
    private static final String[] question_ability_type_arr =
            {"全部", "口语", "听力", "阅读", "写作", "翻译",
                    "单词", "语法", "其他"};
    OnClickListener onTVCloseClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            finish();
        }
    };
    OnClickListener onBtnOkClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, CommunityActivity.class));
            finish();
        }
    };
    private TextView tvSelectClose;
    private Button btnClearSelect;
    private Button btnOkSelect;
    private GridView gvAppType;
    private GridView gvAbilityType;
    OnClickListener onBtnClearClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            ConfigManager.Instance().putInt("quesAppType", 0);
            ConfigManager.Instance().putInt("quesAbilityType", 0);

            gvAppType.setAdapter(new AppTypeTextViewAdapter(mContext));

            gvAbilityType.setAdapter(new AbilityTypeTextViewAdapter(mContext));


        }
    };
    private int quesAbilityType;
    private int quesAppType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_question_type);

        findViewsById();

        setViewsListener();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void getQuesTypeData() {
        quesAbilityType = ConfigManager.Instance().loadInt("quesAbilityType");

        if (ConfigManager.Instance().loadInt("quesAppType") != 0) {
            quesAppType = ConfigManager.Instance().loadInt("quesAppType") - 100;
        } else {
            quesAppType = ConfigManager.Instance().loadInt("quesAppType");
        }

    }

//	OnItemClickListener onGVAppTypeItemClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view,  
//                int position, long id) { 
//
////			Toast.makeText(mContext,question_app_type_arr[position], 1000).show();
//		}
//	};
//	
//	OnItemClickListener onGVAbilityTypeItemClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view,  
//                int position, long id) { 
//
////			Toast.makeText(mContext,question_ability_type_arr[position], 1000).show();
////			ConfigManager.Instance().putInt("quesAbilityType", position);
//		}
//	};

    private void findViewsById() {
        tvSelectClose = findViewById(R.id.tv_select_ques_type_close);
        btnClearSelect = findViewById(R.id.btn_clear_select_type);
        btnOkSelect = findViewById(R.id.btn_ok_select_type);
        gvAppType = findViewById(R.id.gv_ques_app_type);
        gvAbilityType = findViewById(R.id.gv_ques_ability_type);
    }

    private void setViewsListener() {
//		tvSelectClose = (TextView) findViewById(R.id.tv_select_ques_type_close);
//		btnClearSelect = (Button) findViewById(R.id.btn_clear_select_type);
//		btnOkSelect = (Button) findViewById(R.id.btn_ok_select_type);
//		gvAppType = (GridView) findViewById(R.id.gv_ques_app_type);
//		gvAbilityType = (GridView) findViewById(R.id.gv_ques_ability_type);

        tvSelectClose.setOnClickListener(onTVCloseClickListener);
        btnOkSelect.setOnClickListener(onBtnOkClickListener);
        btnClearSelect.setOnClickListener(onBtnClearClickListener);

        gvAppType.setAdapter(new AppTypeTextViewAdapter(mContext));
//		gvAppType.setOnItemClickListener(onGVAppTypeItemClickListener);

        gvAbilityType.setAdapter(new AbilityTypeTextViewAdapter(mContext));
//		gvAbilityType.setOnItemClickListener(onGVAbilityTypeItemClickListener);
    }

    private class AppTypeTextViewAdapter extends BaseAdapter {
        private Context mContext;

        public AppTypeTextViewAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return question_app_type_arr.length;
        }

        @Override
        public Object getItem(int position) {
            return question_app_type_arr[position];
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(
                        R.layout.item_activity_select_ques_type, null);
                viewHolder = new ViewHolder();
                viewHolder.rlAppQuesType = convertView
                        .findViewById(R.id.rl_select_ques_type_item);
                viewHolder.tvAppQuesType = convertView
                        .findViewById(R.id.tv_select_ques_type_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            getQuesTypeData();

            if (quesAppType == position) {
                viewHolder.rlAppQuesType.setBackgroundColor(Color.parseColor("#0077d5"));
                viewHolder.tvAppQuesType.setTextColor(Color.WHITE);
            } else {
                viewHolder.rlAppQuesType.setBackgroundColor(Color.parseColor("#dce3e3"));
                viewHolder.tvAppQuesType.setTextColor(Color.BLACK);
            }

            viewHolder.tvAppQuesType.setText(question_app_type_arr[position]);

            viewHolder.rlAppQuesType.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

//    				Toast.makeText(mContext,question_app_type_arr[position], 1000).show();
                    if (position == 0) {
                        ConfigManager.Instance().putInt("quesAppType", 0);
                    } else {
                        ConfigManager.Instance().putInt("quesAppType", position + 100);
                    }
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public class ViewHolder {
            RelativeLayout rlAppQuesType;
            TextView tvAppQuesType;

        }
    }

    private class AbilityTypeTextViewAdapter extends BaseAdapter {
        private Context mContext;

        public AbilityTypeTextViewAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return question_ability_type_arr.length;
        }

        @Override
        public Object getItem(int position) {
            return question_ability_type_arr[position];
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(
                        R.layout.item_activity_select_ques_type, null);
                viewHolder = new ViewHolder();
                viewHolder.rlAbilityQuesType = convertView
                        .findViewById(R.id.rl_select_ques_type_item);
                viewHolder.tvAbilityQuesType = convertView
                        .findViewById(R.id.tv_select_ques_type_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            getQuesTypeData();

            if (quesAbilityType == position) {
                viewHolder.rlAbilityQuesType.setBackgroundColor(Color.parseColor("#0077d5"));
                viewHolder.tvAbilityQuesType.setTextColor(Color.WHITE);
            } else {
                viewHolder.rlAbilityQuesType.setBackgroundColor(Color.parseColor("#dce3e3"));
                viewHolder.tvAbilityQuesType.setTextColor(Color.BLACK);
            }

            viewHolder.tvAbilityQuesType.setText(question_ability_type_arr[position]);

            viewHolder.rlAbilityQuesType.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

//				Toast.makeText(mContext,question_ability_type_arr[position], 1000).show();
                    ConfigManager.Instance().putInt("quesAbilityType", position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public class ViewHolder {
            RelativeLayout rlAbilityQuesType;
            TextView tvAbilityQuesType;
        }
    }
}
