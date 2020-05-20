/*
 * 文件名
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.SectionA;
import com.iyuba.CET4bible.listening.SectionC;
import com.iyuba.CET4bible.manager.DownloadManager;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.CET4bible.sqlite.op.ExplainOp;
import com.iyuba.CET4bible.sqlite.op.SectionAAnswerOp;
import com.iyuba.CET4bible.sqlite.op.SectionBAnswerOp;
import com.iyuba.CET4bible.sqlite.op.SectionCAnswerOp;
import com.iyuba.CET4bible.sqlite.op.SectionCTextOp;
import com.iyuba.CET4bible.thread.DownloadTask;
import com.iyuba.configation.Constant;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 类名
 *
 * @author 作者 <br/>
 * 实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class TestListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mList;
    private int clickPosition = 0;
    private HashMap<String, RoundProgressBar> progresses = new HashMap<String, RoundProgressBar>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DownloadFile file;
            RoundProgressBar tempBar;
            switch (msg.what) {
                case -1:
                    notifyDataSetChanged();
                    break;
                case 0:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 1:
                    CustomToast.showToast(mContext, R.string.download_finish);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.download);
                    break;
                case 3:
                    file = (DownloadFile) msg.obj;
                    Message message = new Message();
                    if (file.downloadState.equals("start")) {
                        tempBar = progresses.get(String.valueOf(file.id));
                        tempBar.setCricleProgressColor(mContext.getResources()
                                .getColor(R.color.app_color));
                        if (file.fileSize != 0 && file.downloadSize != 0) {
                            tempBar.setMax(file.fileSize);
                            tempBar.setProgress(file.downloadSize);
                        } else {
                            tempBar.setMax(1000);
                            tempBar.setProgress(1);
                        }
                        message.what = 3;
                        message.obj = file;
                        handler.sendMessageDelayed(message, 1500);
                    } else if (file.downloadState.equals("finish")) {
                        message.what = 4;
                        message.obj = file;
                        handler.sendMessage(message);
                    }
                    break;
                case 4:
                    file = (DownloadFile) msg.obj;
                    tempBar = progresses.get(file.id);
                    tempBar.setVisibility(View.GONE);
                    DownloadManager.Instance().fileList.remove(file);
                    handler.sendEmptyMessage(-1);
                    handler.sendEmptyMessage(1);
                    break;
                case 5:
                    try {
                        new File(Constant.videoAddr + msg.obj + ".cet4")
                                .createNewFile();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    public TestListAdapter(Context context) {
        mContext = context;
    }

    public void setClickPos(int pos) {
        clickPosition = pos;
    }

    public void setList(String[] list) {
        this.mList = list;
    }

    @Override
    public int getCount() {

        return mList.length;
    }

    @Override
    public String getItem(int position) {

        return mList[position];
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int curPosition = position;
        final String curYear = mList[position];
        final String test;
        final ViewHolder curViewHolder;
        if (convertView == null) {
            curViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_testlist, null);
            curViewHolder.title = convertView
                    .findViewById(R.id.artical_title);
            curViewHolder.functionView = convertView
                    .findViewById(R.id.testlist_sub);
            curViewHolder.functionImage = convertView
                    .findViewById(R.id.go);
            curViewHolder.touch = convertView.findViewById(R.id.touch);
            curViewHolder.section_a = convertView.findViewById(R.id.section_a);
            curViewHolder.section_b = convertView.findViewById(R.id.section_b);
            curViewHolder.section_c = convertView.findViewById(R.id.section_c);
            curViewHolder.progressBar = convertView
                    .findViewById(R.id.roundProgressBar);
            curViewHolder.image = convertView
                    .findViewById(R.id.image_downed);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }
        String[] years = curYear.split("_");
        StringBuffer sb = new StringBuffer();
        sb.append(years[0]).append(mContext.getString(R.string.year));
        sb.append(years[1]).append(mContext.getString(R.string.month));
        if (years.length == 2) {
            test = years[0] + years[1];
            curViewHolder.title.setText(sb.toString());
        } else {
            test = years[0] + years[1] + years[2];
            if (years[2].equals("0")) {
                sb.append("(").append("预测").append(")");
            } else {
                sb.append("(").append(years[2]).append(")");
            }
            curViewHolder.title.setText(sb.toString());
        }
        if (curPosition != clickPosition) {
            curViewHolder.title.setTextColor(mContext.getResources().getColor(
                    R.color.black));
            curViewHolder.functionView.setVisibility(View.GONE);
            curViewHolder.functionImage.setBackgroundResource(R.drawable.go);
        } else {
            curViewHolder.title.setTextColor(mContext.getResources().getColor(
                    R.color.app_color));
            curViewHolder.functionView.setVisibility(View.VISIBLE);
            curViewHolder.functionImage
                    .setBackgroundResource(R.drawable.go_press);
        }
        if (prepareDownload(test) == 1) {
            curViewHolder.image.setVisibility(View.VISIBLE);
            curViewHolder.progressBar.setVisibility(View.GONE);
        } else if (prepareDownload(test) == 0) {
            curViewHolder.image.setVisibility(View.GONE);
            curViewHolder.progressBar.setVisibility(View.GONE);
        } else {
            curViewHolder.image.setVisibility(View.GONE);
            curViewHolder.progressBar.setVisibility(View.VISIBLE);
            int size = DownloadManager.Instance().fileList.size();
            DownloadFile file;
            for (int i = 0; i < size; i++) {
                file = DownloadManager.Instance().fileList.get(i);
                Log.d("bible", file.downLoadAddress);

                if (file.id.equals(test)) {
                    progresses.put(file.id, curViewHolder.progressBar);
                    Message message = new Message();
                    message.what = 3;
                    message.obj = file;
                    handler.sendMessage(message);
                    break;
                }
            }
        }
        curViewHolder.touch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (curViewHolder.functionView.isShown()) {
                    clickPosition = -1;
                } else {
                    clickPosition = curPosition;
                }
                handler.sendEmptyMessage(-1);
            }
        });
        curViewHolder.section_a.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    getData(0, test);
                    intent.putExtra("section", "A");
                    intent.putExtra("isNewType", false);
                    mContext.startActivity(intent);
                } else if (prepareDownload(test) == 2) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.obtainMessage(5, test).sendToTarget();
                    checkNetWork(test);
                    handler.sendEmptyMessageDelayed(-1, 1000);
                }
            }
        });
        curViewHolder.section_b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    getData(1, test);
                    intent.putExtra("section", "B");
                    intent.putExtra("isNewType", false);
                    mContext.startActivity(intent);
                } else if (prepareDownload(test) == 2) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.obtainMessage(5, test).sendToTarget();
                    checkNetWork(test);
                    handler.sendEmptyMessageDelayed(-1, 1000);
                }
            }
        });
        curViewHolder.section_c.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionC.class);
                    getData(2, test);
                    intent.putExtra("section", "C");
                    intent.putExtra("isNewType", false);
                    mContext.startActivity(intent);
                } else if (prepareDownload(test) == 2) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.obtainMessage(5, test).sendToTarget();
                    checkNetWork(test);
                    handler.sendEmptyMessageDelayed(-1, 1000);
                }
            }
        });
        return convertView;
    }

    private void getData(int type, String year) {
        ListenDataManager.Instance().year = year;
        switch (type) {
            case 0:
                ListenDataManager.Instance().answerList = new SectionAAnswerOp(
                        mContext).selectData(year);
                ListenDataManager.Instance().explainList = new ExplainOp(mContext)
                        .selectData(year);
                break;
            case 1:
                ListenDataManager.Instance().answerList = new SectionBAnswerOp(
                        mContext).selectData(year);
                ListenDataManager.Instance().explainList = new ExplainOp(mContext)
                        .selectData(year);
                break;
            case 2:
                ListenDataManager.Instance().blankList = new SectionCAnswerOp(
                        mContext).selectData(year);
                ListenDataManager.Instance().textList = new SectionCTextOp(mContext)
                        .selectData(year);
                ListenDataManager.Instance().explainList = new ExplainOp(mContext)
                        .selectData(year);
                break;
            default:
                break;
        }
    }

    private int prepareDownload(String year) {

        String fileNoAppend = Constant.videoAddr + year + ".cet4";
        String folder = Constant.videoAddr + year;
        File file1 = new File(folder);
        File file2 = new File(fileNoAppend);
        if (file1.exists()) {
            return 1;// complete
        } else if (file2.exists()) {
            return 2;// downloading
        } else {
            return 0;// no down
        }
    }

    private void initDownload(String year) {

        DownloadFile downloadFile = new DownloadFile();
        downloadFile.id = year;
        downloadFile.downloadState = "start";
        downloadFile.fileAppend = ".zip";
        downloadFile.fileName = year;
        downloadFile.downLoadAddress = "http://cetsounds.iyuba.cn/" + Constant.APP_CONSTANT.TYPE() + "/"
                + year + ".zip";
        DownloadManager.Instance().fileList.add(downloadFile);
        DownloadTask task = new DownloadTask(downloadFile);
        task.start();
    }

    private void checkNetWork(String year) {
        int isConnect = NetWorkState.getAPNType();
        if (isConnect == 0) {
            handler.sendEmptyMessage(0);
        } else {
            handler.sendEmptyMessage(2);
            initDownload(year);
        }
    }

    public class ViewHolder {
        private ImageView functionImage;
        private TextView title;
        private View section_a, section_b, section_c, touch;
        private View functionView;
        private RoundProgressBar progressBar;
        private ImageView image;
    }
}
