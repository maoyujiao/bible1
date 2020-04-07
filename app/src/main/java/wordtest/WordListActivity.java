package wordtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.widget.RecyclerViewSideBar;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.ConfigManager;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import adapter.SimpleWordListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newDB.CetDataBase;
import newDB.CetRootWord;

public class WordListActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sidebar)
    RecyclerViewSideBar sidebar;
    @BindView(R.id.study)
    TextView study;
    @BindView(R.id.test)
    TextView test;
    private boolean showSideBar;

    public static void startIntnent(Context mContext, int stage ,boolean showSideBar) {
        Intent intent = new Intent(mContext, WordListActivity.class);


        intent.putExtra("stage", stage);
        intent.putExtra("showSideBar", showSideBar);
        mContext.startActivity(intent);
    }



    CetDataBase db;
    Context context;
    SimpleWordListAdapter adapter;
    List<CetRootWord> list;
    int stage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_main);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary) , 0);
        context = this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        stage = getIntent().getExtras().getInt("stage");
        showSideBar = getIntent().getExtras().getBoolean("showSideBar");
        db = CetDataBase.getInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (stage == -1) {
            list = db.getCetRootWordDao().getWordsCollect();
        } else {
            list = db.getCetRootWordDao().getWordsByStage(stage);
        }
        adapter = new SimpleWordListAdapter(list, false);
        adapter.setShowOrder(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick({R.id.study, R.id.test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.study:
                Intent intent = new Intent(context, WordDetailActiivty.class);
                intent.putExtra("stage", ConfigManager.Instance().loadInt("stage", 1));
                startActivity(intent);
                break;
            case R.id.test:
                WordTestActivity.start(context, ConfigManager.Instance().loadInt("stage", 1));
                break;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (stage == -1) {
            list = db.getCetRootWordDao().getWordsCollect();
            test.setVisibility(View.GONE);
            study.setVisibility(View.GONE);
            adapter = new SimpleWordListAdapter(list, false);
        } else if (stage == 0) {
            list = db.getCetRootWordDao().getAllRootWord();
            test.setVisibility(View.GONE);
            study.setVisibility(View.GONE);
            adapter = new SimpleWordListAdapter(list, true);
            adapter.setShowOrder(true);
        } else {
            list = db.getCetRootWordDao().getWordsByStage(stage);
            adapter = new SimpleWordListAdapter(list, true);
        }
        recyclerView.setAdapter(adapter);
//        sidebar.setFloatLetterTextView(mFloatLetterTv);
        sidebar.setSelectedSideBarColor(R.color.app_color);
        sidebar.setRecyclerView(recyclerView);
        if (!showSideBar){
            sidebar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
