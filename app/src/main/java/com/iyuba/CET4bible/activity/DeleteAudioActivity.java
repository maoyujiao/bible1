package com.iyuba.CET4bible.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author：Howard on 2016/8/3 14:23
 * Email：Howard9891@163.com
 */

public class DeleteAudioActivity extends BaseActivity {
    List<String> fileNames;
    private ListView lv_audio_delete;
    private DelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_audio);
        lv_audio_delete = findViewById(R.id.lv_audio_delete);
        findViewById(R.id.buttonTitleBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fileNames = new ArrayList<>();
        File file = null;
        try {
            file = new File(Constant.videoAddr);
            if (!file.exists()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.getName().startsWith("ability")) {
                continue;
            }
            fileNames.add(f.getName());
        }

        Collections.sort(fileNames);


        mAdapter = new DelAdapter();
        mAdapter.setData(fileNames);
        lv_audio_delete.setAdapter(mAdapter);
        lv_audio_delete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showAlertDialog(final int position) {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("确认删除吗?").setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(Constant.videoAddr + "/" + fileNames.get(position));
                        if (file.isDirectory()) {
                            for (File f : file.listFiles()) {
                                f.delete();
                            }
                        }
                        file.delete();
                        fileNames.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(DeleteAudioActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        dialog.show();
    }


    class DelAdapter extends BaseAdapter {
        private List<String> fileNames;

        public void setData(List<String> fileNames) {
            this.fileNames = fileNames;
        }

        @Override
        public int getCount() {
            return fileNames.size();
        }

        @Override
        public Object getItem(int position) {
            return fileNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_delete_listen, null);
                viewHolder.tv_delete_listen = convertView.findViewById(R.id.tv_delete_listen);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String str = fileNames.get(position);
            if (str.contains(".cet")) {
                viewHolder.tv_delete_listen.setText(str.replace(".cet4", "") + "音频压缩包");
            } else {
                if (str.length() > 6) {
                    viewHolder.tv_delete_listen.setText(str.substring(0, 6) + "(" + str.substring(str.length() - 1, str.length()) + ")");
                } else {
                    viewHolder.tv_delete_listen.setText(str.substring(0, 6) + "(1)");
                }
            }
            return convertView;
        }
    }


    class ViewHolder {
        TextView tv_delete_listen;
    }
}
