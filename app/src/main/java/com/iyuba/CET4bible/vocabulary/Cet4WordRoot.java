package com.iyuba.CET4bible.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.Cet4Word;
import com.iyuba.CET4bible.sqlite.op.Cet4WordOp;
import com.iyuba.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by yq on 2017/2/24.
 */

public class Cet4WordRoot extends BaseActivity {

    private ArrayList<Cet4Word> rootWords;
    private Cet4WordOp wordOp;
    private ListView listView;
    private Cet4RootListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularyroot);
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        Intent intent = getIntent();
        int groupflg = intent.getIntExtra("rootWord", -1);
        wordOp = new Cet4WordOp(mContext);
        rootWords = wordOp.findWordByRoot(groupflg);
        listView = findViewById(R.id.list);
        adapter = new Cet4RootListAdapter();
        adapter.setList(rootWords);
        listView.setAdapter(adapter);

    }

    class Cet4RootListAdapter extends BaseAdapter {


        private ArrayList<Cet4Word> list;

        public void setList(ArrayList<Cet4Word> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder curViewHolder;
            final Cet4Word word = list.get(i);
            if (convertView == null) {
                curViewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_cet_word, null);
                curViewHolder.word = convertView.findViewById(R.id.word);
                curViewHolder.pron = convertView.findViewById(R.id.pron);
                curViewHolder.desc = convertView.findViewById(R.id.desc);
                convertView.setTag(curViewHolder);
            } else {
                curViewHolder = (ViewHolder) convertView.getTag();
            }
            curViewHolder.word.setText(word.word);
            curViewHolder.pron.setText(word.pron);
            curViewHolder.desc.setText(word.def);
            return convertView;
        }

    }

    public class ViewHolder {
        TextView word, pron;
        TextView desc;
    }

}
