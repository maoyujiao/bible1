package com.iyuba.CET4bible.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.SectionA;
import com.iyuba.CET4bible.listening.TestListActivity;
import com.iyuba.CET4bible.manager.DownloadManager;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.CET4bible.sqlite.op.NewTypeExplainOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionAAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionBAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionCAnswerOp;
import com.iyuba.CET4bible.thread.DownloadTask;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.base.BaseRecyclerViewAdapter;
import com.iyuba.base.util.L;
import com.iyuba.base.util.T;
import com.iyuba.configation.Constant;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.nativeads.ListVideoAdRenderer;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.video.VideoStrategy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ListeningTestListAdapter
 *
 * @author wayne
 * @date 2018/1/3
 */
public class ListeningTestListAdapter extends BaseRecyclerViewAdapter {

    private final ListVideoAdRenderer youdaoAdRenders;
    private List mList;
    private int clickPosition = 0;
    private Map<String, Integer> yearImageMap;
    private NewTypeSectionAAnswerOp op ;
    private Map<String, String> imageUrls;
    private CustomDialog waitingDialog;
    private final static int[] drawables = new int[]{
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3,
            R.drawable.icon4,
            R.drawable.icon5,
            R.drawable.icon6};
    private Boolean isHome ; // 是否是首页 , 首页只加载3个

    private HashMap<String, RoundProgressBar> progresses = new HashMap<>();
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

    public void setList(List list) {
        this.mList = list;
    }

    public ListeningTestListAdapter(Context context , Boolean isHome) {
        super(context);
        op= new NewTypeSectionAAnswerOp(context);
        this.isHome  = isHome ;
        this.waitingDialog = WaittingDialog.showDialog(mContext);

        final VideoStrategy videoStrategy = YouDaoAd.getYouDaoOptions()
                .getDefaultVideoStrategy();
        videoStrategy.setPlayVoice(true);
        videoStrategy.setVisiblePlay(true);
        //根据需要设置视频策略
        YouDaoAd.getYouDaoOptions().setVideoStrategy(videoStrategy);
        youdaoAdRenders = new ListVideoAdRenderer(
                new ViewBinder.Builder(R.layout.youdao_video_ad_item_small)
                        .videoId(R.id.mediaView).titleId(R.id.native_title)
                        .textId(R.id.native_content)
                        .build());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.listen_foot, parent, false));
        } else if (viewType == 2) {
            return new VideoHolder(youdaoAdRenders.createAdView(mContext, parent));
        } else {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.year_item_new1, parent, false));
        }
    }



    @Override
    public int getItemCount() {
        if (isHome){
            return 3 ;
        }
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (position == mList.size()) {
                return 1;
            } else if (mList.get(position) instanceof NativeResponse) {
                NativeResponse nativeResponse = (NativeResponse) mList.get(position);
                if (nativeResponse.getVideoAd() != null) {
                    return 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        if (position == mList.size()) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, TestListActivity.class);
                    mContext.startActivity(intent);
                }
            });
            return;
        }

        if (getItemViewType(position) == 2) {
            final NativeResponse response = (NativeResponse) mList.get(position);
            response.recordImpression(viewHolder.itemView);
            youdaoAdRenders.renderAdView(viewHolder.itemView, response);
            VideoHolder h = (VideoHolder) viewHolder;
            h.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(v);
                }
            });
            return;
        }
        final Holder curViewHolder = (Holder) viewHolder;
        if (mList.get(position) instanceof NativeResponse) {
            final NativeResponse response = (NativeResponse) mList.get(position);
            response.recordImpression(curViewHolder.itemView);
            curViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(curViewHolder.itemView);
                }
            });
            curViewHolder.rate.setVisibility(View.GONE);
            curViewHolder.image.setVisibility(View.GONE);
            curViewHolder.progressBar.setVisibility(View.GONE);

            curViewHolder.ad_title.setText(response.getTitle());
            curViewHolder.ad_title.setVisibility(View.VISIBLE);
            curViewHolder.title.setVisibility(View.GONE);
            Glide.with(mContext).load(response.getMainImageUrl())
                    .error(R.drawable.nearby_no_icon2)
                    .placeholder(R.drawable.nearby_no_icon2)
                    .into(curViewHolder.adimage);
            curViewHolder.rr.setVisibility(View.GONE);
            curViewHolder.ll_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(curViewHolder.itemView);
                }
            });
            curViewHolder.adimage.setVisibility(View.VISIBLE);
            curViewHolder.download.setVisibility(View.GONE);
            curViewHolder.ad_note.setVisibility(View.VISIBLE);

            return;
        }else {
            curViewHolder.ad_title.setVisibility(View.GONE);
            curViewHolder.title.setVisibility(View.VISIBLE);
            curViewHolder.adimage.setVisibility(View.GONE);
            curViewHolder.image.setVisibility(View.VISIBLE);
            curViewHolder.ad_note.setVisibility(View.GONE);

        }
        curViewHolder.download.setVisibility(View.VISIBLE);


        final int curPosition = position;
        L.e("==== list size :::  " + mList.size());
        final String curYear = (String) mList.get(position);
        final String test;
        String[] years;

        curViewHolder.rr.setVisibility(View.VISIBLE);
        curViewHolder.image.setImageDrawable(mContext.getResources().getDrawable(drawables[position%6]));

        if (curYear.contains("_")) {
            final int ic_year = yearImageMap.get(curYear.replace("_", ""));
            years = curYear.split("_");
        } else {

            years = new String[3];
            years[0] = curYear.substring(0, 4);
            years[1] = curYear.substring(4, 6);
            years[2] = curYear.substring(6, curYear.length());
        }

        curViewHolder.rate.setVisibility(View.VISIBLE);
        curViewHolder.rate.setText(String.format
                ("正确 : %d/%d",op.getRightSum(curYear),25));
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

        if (prepareDownload(test) == 1) {
            curViewHolder.image.setVisibility(View.VISIBLE);
            curViewHolder.progressBar.setVisibility(View.GONE);
            curViewHolder.download.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_download_finished));
        } else if (prepareDownload(test) == 0) {
            curViewHolder.progressBar.setVisibility(View.GONE);
            curViewHolder.download.setVisibility(View.VISIBLE);
            curViewHolder.download.setImageDrawable(mContext.getResources().getDrawable(R.drawable.home_icon_download));

            curViewHolder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkStoragePermission()){
                        handler.obtainMessage(5, test).sendToTarget();
                        checkNetWork(test);
                        handler.sendEmptyMessageDelayed(-1, 1000);
                    }

                }
            });
        } else {
            curViewHolder.download.setVisibility(View.GONE);
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

        curViewHolder.ll_touch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                handler.sendEmptyMessage(-1);

            }
        });
        curViewHolder.section_a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.e("TestListAdapter", "test>>>>>" + test);
                startListeningActivity(0, test, "A", true, sb.toString(), position);
            }
        });
        curViewHolder.section_b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startListeningActivity(1, test, "B", true, sb.toString(), position);
            }
        });
        curViewHolder.section_c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startListeningActivity(2, test, "C", true, sb.toString(), position);

            }
        });
    }

    private boolean checkStoragePermission() {
        if(mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ((Activity)mContext).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return false ;
        }
        return true;
    }

    private void startListeningActivity(int type, String test, final String section, final boolean isNewType, final String title, int position) {
        List list = getData(type, test);
        if (list == null || list.size() == 0) {
            startListeningActivity00(type, test, section, isNewType, title, position);
        } else {
            Intent intent = new Intent(mContext, SectionA.class);
            intent.putExtra("section", section);
            intent.putExtra("isNewType", isNewType);
            intent.putExtra("title", title);
            mContext.startActivity(intent);
        }
    }

    private void startListeningActivity00(final int type, final String test, final String section, final boolean isNewType, final String title, int position) {
        waitingDialog.show();
        String year = ((String) mList.get(position)).replace("_", "");
        ExamDataUtil.requestExamData(mContext, Constant.APP_CONSTANT.TYPE(), section, year, new ExamDataUtil.DataCallback() {
            @Override
            public void onLoadData(boolean success) {
                waitingDialog.dismiss();
                if (success) {
                    getData(type, test);
                    Intent intent = new Intent(mContext, SectionA.class);
                    intent.putExtra("section", section);
                    intent.putExtra("isNewType", isNewType);
                    intent.putExtra("title", title);
                    mContext.startActivity(intent);
                } else {
                    T.showShort(mContext, "题库加载失败");
                }
            }
        });
    }

    private List getData(int type, String year) {
        ListenDataManager.Instance().year = year;
        L.e("adapter === year ==== " + year);
        switch (type) {
            case 0:
                ListenDataManager.Instance().answerList = new NewTypeSectionAAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            case 1:
                ListenDataManager.Instance().answerList = new NewTypeSectionBAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            case 2:
                ListenDataManager.Instance().answerList = new NewTypeSectionCAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            default:
                return null;
        }
    }

    private int prepareDownload(String year) {
        if (checkStoragePermission()){
            String fileNoAppend = Constant.videoAddr + year + ".cet4";
            String folder = Constant.videoAddr + year;
            File file1 = new File(folder);
            File file2 = new File(fileNoAppend);
            if (file1.exists()) {
                if (file1.list().length <= 0) {
                    file1.delete();
                    return 0;
                }
                // complete
                return 1;
            } else if (file2.exists()) {
                // downloading
                return 2;
            } else {
                // no down
                return 0;
            }
        }else {
            return 0;
        }

    }

    private void initDownload(String year) {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.id = year;
        downloadFile.downloadState = "start";
        downloadFile.fileAppend = ".zip";
        downloadFile.fileName = year;
        if (SettingConfig.Instance().isHighSpeed()) {
            downloadFile.downLoadAddress = "http://cetsounds.iyuba.cn/" + Constant.APP_CONSTANT.TYPE() + "/"
                    + year + ".zip";
        } else {
            downloadFile.downLoadAddress = "http://cetsounds.iyuba.cn/" + Constant.APP_CONSTANT.TYPE() + "/"
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

    public void setImageUrls(Map images) {
        imageUrls = images;
    }

    public void setImageMap() {
        if (Constant.APP_CONSTANT.TYPE().equals("4")) {
            yearImageMap = new HashMap<>();
            yearImageMap.put("2017121601", R.drawable.ic_2017121601);
            yearImageMap.put("2017061702", R.drawable.ic_2017061702);
            yearImageMap.put("2017061701", R.drawable.ic_2017061701);
            yearImageMap.put("2016121702", R.drawable.ic_2016121702);
            yearImageMap.put("2016121701", R.drawable.ic_2016121701);
            yearImageMap.put("2016061802", R.drawable.ic_2016062);
            yearImageMap.put("2016061801", R.drawable.ic_2016061);
            yearImageMap.put("2015121903", R.drawable.ic_2015123);
            yearImageMap.put("2015121902", R.drawable.ic_2015122);
            yearImageMap.put("2015121901", R.drawable.ic_2015121);
            yearImageMap.put("2015061303", R.drawable.ic_2015063);
            yearImageMap.put("2015061302", R.drawable.ic_2015062);
            yearImageMap.put("2015061301", R.drawable.ic_2015061);
        } else {
            yearImageMap = new HashMap<>();
            yearImageMap.put("2017061702", R.drawable.ic_2017061702);
            yearImageMap.put("2017061701", R.drawable.ic_2017061701);
            yearImageMap.put("201612172", R.drawable.ic_2016121702);
            yearImageMap.put("201612171", R.drawable.ic_2016121701);
            yearImageMap.put("201606182", R.drawable.ic_2016062);
            yearImageMap.put("201606181", R.drawable.ic_2016061);
            yearImageMap.put("201512193", R.drawable.ic_2015123);
            yearImageMap.put("201512192", R.drawable.ic_2015122);
            yearImageMap.put("201512191", R.drawable.ic_2015121);
            yearImageMap.put("201506133", R.drawable.ic_2015063);
            yearImageMap.put("201506132", R.drawable.ic_2015062);
            yearImageMap.put("201506131", R.drawable.ic_2015061);
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView ad_title;
        private View section_a, section_b, section_c, ll_touch;
        private RoundProgressBar progressBar;
        private TextView ad_note ;
        private ImageView image;
        private ImageView adimage;
        private ImageView download;
        private RelativeLayout rr;
        private TextView rate;
//        private View ivDownload;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.exam_year);
            ad_title = itemView.findViewById(R.id.ad_title);
            ll_touch = itemView.findViewById(R.id.ll_touch);
            section_a = itemView.findViewById(R.id.icon_a);
            section_b = itemView.findViewById(R.id.icon_b);
            section_c = itemView.findViewById(R.id.icon_c);
            rr = itemView.findViewById(R.id.rr);
            progressBar = itemView.findViewById(R.id.roundProgressBar);
            image = itemView.findViewById(R.id.src_img);
            adimage = itemView.findViewById(R.id.ad_img);
            download = itemView.findViewById(R.id.dl_view);
            rate = itemView.findViewById(R.id.rate);
            ad_note = itemView.findViewById(R.id.ad_note);

//            ivDownload = itemView.findViewById(R.id.iv_audio_download);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        View view;

        public VideoHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.ll);
        }
    }
}
