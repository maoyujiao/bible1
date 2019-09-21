package com.iyuba.abilitytest.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.adapter.AbilityTestListAdapter;
import com.iyuba.abilitytest.entity.AbilityLessonInfoEntity;
import com.iyuba.abilitytest.listener.OnRecyclerViewItemClickListener;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.sqlite.AbilityDBHelper;
import com.iyuba.abilitytest.sqlite.AbilityDBManager;
import com.iyuba.abilitytest.utils.AbilityConstants;
import com.iyuba.abilitytest.utils.AdInfoFlowUtil;
import com.iyuba.abilitytest.utils.TestedUtil;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.widget.DividerItemDecoration;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 试题按照年份分类列表界面
 * Created by LiuZhenLi on 2017/9/6.
 * last modify time 20170906 by 刘振立
 */
public class AbilityTestListActivity extends AppBaseActivity {

    private RecyclerView rv_test_itemlist;
    private Context mContext;
    private final int FLAG_TEST = 1;//模式标记 1代表测评  2 代表练习
    private final int FLAG_PRACTICE = 2;//模式标记 1代表测评  2 代表练习
    private int flag_mode;//试题模式
    private ImageButton btn_nav_sub;
    private TextView tv_titlebar_sub;
    private ArrayList lessonInfos;
    private AbilityTestListAdapter adapter;

    AdInfoFlowUtil adInfoFlowUtil;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_abilitytest_sortlist;
    }

    public static void actionStart(Context context, int mode) {
        Intent intent = new Intent(context, AbilityTestListActivity.class);
        intent.putExtra("flag_mode", mode);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        mContext = this;
        flag_mode = getIntent().getIntExtra("flag_mode", 1);
        AbilityDBManager manager = new AbilityDBManager(mContext, AbilityConstants.DB_VERSION);
        manager.openDatabase();

        String appId = Constant.APPID;
        String type = Constant.APP_CONSTANT.TYPE();
        if (type.equals("4")) {
            appId = "207";
        } else if (type.equals("6")) {
            appId = "208";
        } else if (type.equals("1")) {
            appId = "205";
        } else if (type.equals("2")) {
            appId = "206";
        } else if (type.equals("3")) {
            appId = "203";
        }
        lessonInfos = AbilityDBHelper.getInstance().getLessonInfosByBookId(appId);
        if (lessonInfos == null || lessonInfos.size() == 0) {
            CustomToast.showToast(mContext, "没有数据");
            finish();
            return;
        }
        Constant.mListen = ((AbilityLessonInfoEntity) lessonInfos.get(0)).bookName;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        rv_test_itemlist = findView(R.id.rv_test_itemlist);
        btn_nav_sub = findView(R.id.btn_nav_sub);
        tv_titlebar_sub = findView(R.id.tv_titlebar_sub);
        View deleteCache = findViewById(R.id.iv_delete);
        deleteCache.setVisibility(View.VISIBLE);
        deleteCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });


        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(lessonInfos, ads, adInfoFlowUtil);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private int cachePosition = 0;

    private void showAlertDialog() {
        String path = Constant.videoAddr + "abilityTest";
        File file = new File(path);
        final ArrayList<String> list = new ArrayList<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    String name = f.getName();
                    if (name.equals("0") || name.equals("-1")) {
                        continue;
                    }
                    list.add(name);
                }
            }

        }
        if (list.size() == 0) {
            CustomToast.showToast(mContext, "暂无缓存");
            return;
        }
        final String[] data = list.toArray(new String[list.size()]);
        Arrays.sort(data);

        cachePosition = 0;
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("请选择要删除的缓存")
//                .setMessage("确定要删除缓存吗?")
                .setSingleChoiceItems(data, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cachePosition = which;
                    }
                })
                .setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String path = Constant.videoAddr + "/abilityTest/" + data[cachePosition];
                                Log.e("--delete---", "cache_abilityTest:::" + path);
                                GetFileSizeUtils.deleteDir(new File(path));
                                TestedUtil.getInstance().removeLessonId(data[cachePosition]);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void loadData() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        rv_test_itemlist.setLayoutManager(manager);
        adapter = new AbilityTestListAdapter(mContext);
        filter(lessonInfos);

        adapter.setData(lessonInfos);
        adInfoFlowUtil.refreshAd();

//        rv_test_itemlist.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        rv_test_itemlist.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 2);
            }
        });
        rv_test_itemlist.setAdapter(adapter);
        adapter.setOnitemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int lessonId = ((AbilityLessonInfoEntity) lessonInfos.get(position)).lessonId;
                DataManager.getInstance().lessonId = lessonId + "/";
                AbilityMapActivity.actionStart(mContext, flag_mode, lessonId);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        btn_nav_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_titlebar_sub.setText("练习列表");
    }

    private void filter(ArrayList lessonInfos) {
        List<String> list = new ArrayList<>();
        String bookName = "";
        int bookId = 0;
        for (int i = 0; i < lessonInfos.size(); i++) {
            AbilityLessonInfoEntity entity = (AbilityLessonInfoEntity) lessonInfos.get(i);
            Log.e("lessonInfo", entity.toString());
            bookName = entity.bookName;
            bookId = entity.bookId;
            String key = (entity.lessonId + "").substring(0, 6);
            if (!list.contains(key)) {
                list.add(key);
            }
        }
        lessonInfos.clear();

        for (String s : list) {

            AbilityLessonInfoEntity entity = new AbilityLessonInfoEntity();
            entity.bookId = bookId;
            entity.bookName = bookName;
            entity.lessonId = Integer.parseInt(s + "01");
            entity.lessonName = s.substring(0, 4) + "-" + s.substring(4, 6) + "(A卷)";

            AbilityLessonInfoEntity entity2 = new AbilityLessonInfoEntity();
            entity2.bookId = bookId;
            entity2.bookName = bookName;
            entity2.lessonId = Integer.parseInt(s + "02");
            entity2.lessonName = s.substring(0, 4) + "-" + s.substring(4, 6) + "(B卷)";

            AbilityLessonInfoEntity entity3 = new AbilityLessonInfoEntity();
            entity3.bookId = bookId;
            entity3.bookName = bookName;
            entity3.lessonId = Integer.parseInt(s + "03");
            entity3.lessonName = s.substring(0, 4) + "-" + s.substring(4, 6) + "(C卷)";

            lessonInfos.add(entity);
            lessonInfos.add(entity2);
            lessonInfos.add(entity3);

            Log.e("lessonInfo---A---", entity.toString());
            Log.e("lessonInfo---B---", entity2.toString());
            Log.e("lessonInfo---B---", entity3.toString());
        }
        if (Constant.mListen.contains("N1")||Constant.mListen.contains("N2")||Constant.mListen.contains("N3")){
            lessonInfos.clear();
            for (String s : list) {
                AbilityLessonInfoEntity entity = new AbilityLessonInfoEntity();
                entity.bookId = bookId;
                entity.bookName = bookName;
                entity.lessonId = Integer.parseInt(s);
                entity.lessonName = s.substring(0, 4) + "-" + s.substring(4, 6);
                lessonInfos.add(entity);
                Log.e("lessonInfo---A---", entity.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        DataManager.getInstance().lessonId = "-1/";
    }

    @Override
    protected void onDestroy() {
        adInfoFlowUtil.destroy();
        super.onDestroy();
    }
}
