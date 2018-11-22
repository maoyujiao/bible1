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

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.SectionA;
import com.iyuba.CET4bible.manager.DownloadManager;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.CET4bible.sqlite.op.NewTypeExplainOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionAAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionBAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionCAnswerOp;
import com.iyuba.CET4bible.thread.DownloadTask;
import com.iyuba.base.util.T;
import com.iyuba.configation.Constant;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.File;
import java.util.HashMap;

public class NewTypeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mList;
    private int clickPosition = 0;
    private int[] iv_years = new int[]{
            R.drawable.ic_2017121601,
            R.drawable.ic_2017061702, R.drawable.ic_2017061701,
            R.drawable.ic_2016121702, R.drawable.ic_2016121701,
            R.drawable.ic_2016062, R.drawable.ic_2016061,
            R.drawable.ic_2015123, R.drawable.ic_2015122, R.drawable.ic_2015121,
            R.drawable.ic_2015063, R.drawable.ic_2015062, R.drawable.ic_2015061};
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
                    T.showLong(mContext, R.string.download);
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
                        new File(Constant.videoAddr + msg.obj + ".cet4").createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };
    private StringBuffer sb;

    public NewTypeAdapter(Context context) {
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
        final int ic_year = iv_years[position];
        final String test;
        final ViewHolder curViewHolder;
        if (convertView == null) {
            curViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_testlist_newtype, null);
            curViewHolder.title = convertView.findViewById(R.id.artical_title);
            curViewHolder.functionView = convertView.findViewById(R.id.testlist_sub);
            curViewHolder.functionImage = convertView.findViewById(R.id.go);
            curViewHolder.ll_touch = convertView.findViewById(R.id.ll_touch);
            curViewHolder.iv_year = convertView.findViewById(R.id.iv_year);
            curViewHolder.touch = convertView.findViewById(R.id.touch);
            curViewHolder.section_a = convertView.findViewById(R.id.section_a);
            curViewHolder.section_b = convertView.findViewById(R.id.section_b);
            curViewHolder.section_c = convertView.findViewById(R.id.section_c);
            curViewHolder.progressBar = convertView.findViewById(R.id.roundProgressBar);
            curViewHolder.image = convertView.findViewById(R.id.image_downed);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(ic_year).into(curViewHolder.iv_year);
        String[] years = curYear.split("_");
        sb = new StringBuffer();
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
                sb.append("(").append(years[2].substring(years[2].length() - 1, years[2].length())).append(")");
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

        curViewHolder.ll_touch.setOnClickListener(new OnClickListener() {

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
        /*curViewHolder.touch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("curViewHolder.touch","curPosition>>>"+curPosition);
				if (curViewHolder.functionView.isShown()) {
					clickPosition = -1;
				} else {
					clickPosition = curPosition;
				}
				handler.sendEmptyMessage(-1);
			}
		});*/
        curViewHolder.section_a.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.e("TestListAdapter", "test>>>>>" + test);
                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    getData(0, test);
                    intent.putExtra("section", "A");
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
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
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
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
                    Intent intent = new Intent(mContext, SectionA.class);
                    intent.putExtra("section", "C");
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
                    getData(2, test);
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
                ListenDataManager.Instance().answerList = new NewTypeSectionAAnswerOp(
                        mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext)
                        .selectData(year);
                break;
            case 1:
                ListenDataManager.Instance().answerList = new NewTypeSectionBAnswerOp(
                        mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext)
                        .selectData(year);
                break;
            case 2:
                ListenDataManager.Instance().answerList = new NewTypeSectionCAnswerOp(
                        mContext).selectData(year);
            /*ListenDataManager.Instance().textList = new SectionCTextOp(mContext)
                    .selectData(year);*/
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext)
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
            if (file1.list().length <= 0) {
                file1.delete();
                return 0;
            }
            return 1;
            // complete
        } else if (file2.exists()) {
            return 2;
            // downloading
        } else {
            return 0;
            // no down
        }
    }

    private void initDownload(String year) {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.id = year;
        downloadFile.downloadState = "start";
        downloadFile.fileAppend = ".zip";
        downloadFile.fileName = year;
        if (SettingConfig.Instance().isHighSpeed()) {
            downloadFile.downLoadAddress = "http://cetsoundsvip.iyuba.com/" + Constant.APP_CONSTANT.TYPE() + "/"
                    + year + ".zip";
        } else {
            downloadFile.downLoadAddress = "http://cetsounds.iyuba.com/" + Constant.APP_CONSTANT.TYPE() + "/"
                    + year + ".zip";
        }
        Log.e("TestListAdapter", "downloadAddress>>>>" + downloadFile.downLoadAddress);
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
        private ImageView functionImage, iv_year;
        private TextView title;
        private View section_a, section_b, section_c, touch, ll_touch;
        private View functionView;
        private RoundProgressBar progressBar;
        private ImageView image;
    }
}
