package com.iyuba.core.adapter.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;

/**
 * 简版开考试子页面列表适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class SimpleTestAdapter extends BaseAdapter {
    private Context mContext;
    private int type;
    private String[] testContent;

    public SimpleTestAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        initContent();
    }

    /**
     *
     */
    private void initContent() {

        switch (type) {
            case 0:
//			testContent = new String[] { "2006_06", "2006_12", "2007_06",
//					"2007_12", "2008_06", "2008_12" };
                testContent = new String[]{"2009_06", "2009_12", "2010_06",
                        "2010_12", "2011_06", "2011_12", "2012_06", "2012_12", "2013_06",
                        "2013_12_1", "2013_12_2", "2014_06_1", "2014_06_2", "2014_12_1", "2014_12_2",
                        "2014_12_3", "2015_06_1", "2015_06_2", "2015_12_1", "2015_12_2"};
                break;
            case 1:
//			testContent = new String[] { "2006_06", "2006_12", "2007_06",
//					"2007_12", "2008_06", "2008_12" };
                testContent = new String[]{"2009_06", "2009_12", "2010_06",
                        "2010_12", "2011_06", "2011_12", "2012_06", "2012_12", "2013_06",
                        "2013_12_1", "2013_12_2", "2014_06_1", "2014_06_2", "2014_12_1", "2014_12_2",
                        "2014_12_3", "2015_06_1", "2015_06_2", "2015_12_1", "2015_12_2"};
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;

            default:
                break;
        }
    }

    @Override
    public int getCount() {

        return testContent.length;
    }

    @Override
    public String getItem(int arg0) {

        return testContent[arg0];
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String content = testContent[position];
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_common_test, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView
                    .findViewById(R.id.test_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(content);
        return convertView;
    }

    public class ViewHolder {
        TextView title;
    }
}
