/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.TestListAdapter;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class TestListActivity extends BasisActivity {
    private Context mContext;
    private View backBtn;
    private ListView listView;
    private TestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        backBtn = findViewById(R.id.button_back);

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        adapter = new TestListAdapter(mContext);
        adapter.setList(ListenDataManager.getTest());
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                adapter.setClickPos(arg2);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
