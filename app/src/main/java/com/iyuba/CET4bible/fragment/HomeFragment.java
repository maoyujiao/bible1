package com.iyuba.CET4bible.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.BlogActivity;
import com.iyuba.CET4bible.activity.InfoActivity;
import com.iyuba.CET4bible.activity.JpBlogActivity;
import com.iyuba.CET4bible.activity.JpBlogListActivity;
import com.iyuba.CET4bible.activity.MainActivity;
import com.iyuba.CET4bible.activity.TestType;
import com.iyuba.CET4bible.adapter.CardPagerAdapter;
import com.iyuba.CET4bible.adapter.JpSimpleBlogListAdapter;
import com.iyuba.CET4bible.adapter.SimpBlogListAdapter;
import com.iyuba.CET4bible.bean.JpBlogListBean;
import com.iyuba.CET4bible.event.JPLevelChangeEvent;
import com.iyuba.CET4bible.iyulive.adapter.HomeCourseListAdapter;
import com.iyuba.CET4bible.manager.BlogDataManager;
import com.iyuba.CET4bible.protocol.AddImageRequest;
import com.iyuba.CET4bible.protocol.AddImageResponse;
import com.iyuba.CET4bible.protocol.info.BlogRequest;
import com.iyuba.CET4bible.protocol.info.BlogResponse;
import com.iyuba.CET4bible.protocol.info.JpBlogListRequest;
import com.iyuba.CET4bible.protocol.info.JpBlogListResponse;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.CET4bible.sqlite.mode.ImageData;
import com.iyuba.CET4bible.sqlite.op.BlogOp;
import com.iyuba.CET4bible.util.DateUtils;
import com.iyuba.CET4bible.vocabulary.Cet4WordList;
import com.iyuba.abilitytest.activity.AbilityMapActivity;
import com.iyuba.abilitytest.activity.AbilityTestListActivity;
import com.iyuba.base.util.BrandUtil;
import com.iyuba.base.util.T;
import com.iyuba.base.util.Util;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.discover.activity.WordCollection;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.mode.Sayings;
import com.iyuba.core.sqlite.mode.microclass.CoursePack;
import com.iyuba.core.sqlite.op.SayingsOp;
import com.iyuba.core.sqlite.op.microclass.CoursePackOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.PullToRefreshView_New;
import com.iyuba.core.widget.SuperListView;
import com.iyuba.trainingcamp.activity.GoldActivity;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.nativeads.YouDaoAdAdapter;
import com.youdao.sdk.nativeads.YouDaoNativeAdPositioning;
import com.youdao.sdk.nativeads.YouDaoNativeAdRenderer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * 类名
 */
public class HomeFragment extends Fragment implements OnClickListener, PullToRefreshView_New.OnHeaderRefreshListener {
    private Context mContext;
    private ImageView iv_fuction;
    private View root;
    private TextView tv_testDate, tv_duration;
    private SuperListView lv_information;
    private RelativeLayout rl_more;

    // 英语blog
    private ArrayList<Blog> blogs = new ArrayList<>();
    private BlogOp blogOp;
    // 日语blog
    private ArrayList<Blog> jpBlogList = new ArrayList<>();
    private JpSimpleBlogListAdapter jpSimpleBlogListAdapter;

    private ImageView iv_vip;

    private ViewPager vp;
    private List<ImageData> card_list;
    private CardPagerAdapter pagerAdapter;
    private TextView chinese;
    private TextView english;
    private SayingsOp sayingsOp;
    private ScrollView sv;
    private LinearLayout ll_vocabulary;
    private LinearLayout ll_practice;
    private LinearLayout ll_test;
    private View rl_test;
    private SuperListView list_live;
    private HomeCourseListAdapter homeCourseListAdapter;
    private ArrayList<MicroClassListBean.DataBean> microClassList = new ArrayList<>();

    private YouDaoAdAdapter youdaoAdapter;
    private RequestParameters mRequestParameters;
    private RelativeLayout rl_more_live;
    private Sayings sayings;
    private int id;
    private Random rnd;

    private boolean isBannerRequested = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ExeProtocol.exe(new JpBlogListRequest(Constant.APP_CONSTANT.BLOG_ACCOUNT_ID(), "1"), new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            JpBlogListResponse response = (JpBlogListResponse) bhr;
                            if (response == null || response.bean == null || response.bean.getData() == null) {
                                return;
                            }
                            List<JpBlogListBean.DataBean> list = response.bean.getData();
                            if (jpBlogList == null) {
                                jpBlogList = new ArrayList<>();
                            }
                            jpBlogList.clear();
                            jpBlogList.addAll(toBlog(list));
                            new BlogOp(mContext).saveData(jpBlogList);
                            handler.sendEmptyMessage(1);
                        }

                        @Override
                        public void error() {
                            getFromDatabase();
                        }

                        private ArrayList<Blog> toBlog(List<JpBlogListBean.DataBean> list) {
                            ArrayList<Blog> blogArrayList = new ArrayList<>();
                            for (JpBlogListBean.DataBean dataBean : list) {
                                Blog blog = new Blog();
                                blog.createtime = DateFormat.format("yyyy-MM-dd",
                                        Long.parseLong(dataBean.getDateline()) * 1000).toString();
                                blog.readcount = dataBean.getViewnum();
                                blog.title = dataBean.getSubject();
                                blog.url = dataBean.getPic();
                                blog.id = Integer.parseInt(dataBean.getBlogid());
                                blogArrayList.add(blog);
                            }
                            return blogArrayList;
                        }
                    });
                    break;
                case 1:
                    jpSimpleBlogListAdapter = new JpSimpleBlogListAdapter(mContext, jpBlogList);
                    lv_information.setAdapter(jpSimpleBlogListAdapter);
                    break;
                case 2:
                    initHeadView();
                    break;
                case 3:
                    handler.removeMessages(3);
                    int currentItem = vp.getCurrentItem();
                    currentItem++;
                    vp.setCurrentItem(currentItem);
                    handler.sendEmptyMessageDelayed(3, 5000);
                    break;
                case 4:
                    rnd = new Random();
                    id = rnd.nextInt(10000) % 150 + 1;
                    sayings = sayingsOp.findDataById(id);
                    setData();
                    break;
                case 5:
                    if (homeCourseListAdapter == null) {
                        homeCourseListAdapter = new HomeCourseListAdapter(mContext, microClassList);
                    } else {
                        homeCourseListAdapter.notifyDataSetChanged();
                    }
                    break;
                case 6:
                    T.showShort(mContext, R.string.alert_network_error);
                    break;
                case 7:
//                    refreshView.onHeaderRefreshComplete();
                    break;
                case 8:
//                    refreshView.onHeaderRefreshComplete();
                    T.showShort(mContext, R.string.alert_network_error);
                    break;
                default:
                    break;
            }
        }
    };

    private void getFromDatabase() {
        jpBlogList.addAll(blogOp.selectData(3, 0));
        handler.sendEmptyMessage(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        root = inflater.inflate(R.layout.home, container, false);
        blogOp = new BlogOp(mContext);
        //资讯
        handler.sendEmptyMessage(0);
        init();

        pagerAdapter = new CardPagerAdapter(mContext);
        vp.setOffscreenPageLimit(3);
        card_list = DataSupport.findAll(ImageData.class);
        if (card_list != null && !card_list.isEmpty()) {
            for (int i = 0; i < card_list.size(); i++) {
                pagerAdapter.addCardItem(card_list.get(i));
            }
            initHeadView();
        }

        setImages();
        sv.smoothScrollTo(0, 0);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onJPLevelChanged(JPLevelChangeEvent event) {
        //资讯
        handler.sendEmptyMessage(0);
        setImages();
        getMicroClassData();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView_New view) {
        setImages();
        getMicroClassData();
    }

    private void initHeadView() {
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(card_list.size() * 100);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    private void init() {
        initLive(root);

        sv = root.findViewById(R.id.sv);

        vp = root.findViewById(R.id.vp);

        rl_test = root.findViewById(R.id.rl_test);
        rl_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, TestType.class));
            }
        });
        if (!BuildConfig.isEnglish) {
            rl_test.setVisibility(View.GONE);
        }

        ll_vocabulary = root.findViewById(R.id.ll_vocabulary);
        ll_practice = root.findViewById(R.id.ll_practice);
        ll_test = root.findViewById(R.id.ll_test);

        chinese = root.findViewById(R.id.chinese);
        english = root.findViewById(R.id.english);
        sayingsOp = new SayingsOp(mContext);
        handler.sendEmptyMessage(4);

        iv_vip = root.findViewById(R.id.iv_vip);
        iv_fuction = root.findViewById(R.id.iv_fuction);

        tv_testDate = root.findViewById(R.id.tv_testDate);
        tv_duration = root.findViewById(R.id.tv_duration);
        tv_testDate.setText(DateUtils.getTestDate());
        tv_duration.setText(DateUtils.getDuration());

        lv_information = root.findViewById(R.id.lv_information);
        lv_information.setFocusable(false);
        rl_more = root.findViewById(R.id.rl_more);

        rl_more.setOnClickListener(this);
        iv_vip.setOnClickListener(this);

        ll_vocabulary.setOnClickListener(this);
        ll_practice.setOnClickListener(this);
        ll_test.setOnClickListener(this);

        iv_fuction.setOnClickListener(this);

        lv_information.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (BuildConfig.isEnglish) {
//                    Intent intent = new Intent(mContext, BlogActivity.class);
//                    intent.putExtra("title", "综合");
//                    BlogDataManager.Instance().blog = blogs.get(position);
//                    startActivity(intent);
//                } else {
                Intent intent = new Intent(mContext, JpBlogActivity.class);
                intent.putExtra("blog", jpBlogList.get(position));
                startActivity(intent);
//                }
            }
        });
        ((TextView) root.findViewById(R.id.tv_practice_title))
                .setText(BuildConfig.isCET4 ? "四级真题 专项训练" : "六级真题 专项训练");
    }

    private void initLive(View root) {
        list_live = root.findViewById(R.id.list_live);
        list_live.setFocusable(false);
        rl_more_live = root.findViewById(R.id.rl_more_live);
        rl_more_live.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (getActivity() != null) {
//                    ((MainActivity) getActivity()).setTabSelection(1);
//                }
                startActivity(new Intent(mContext, MobClassActivity.class));

            }
        });
        homeCourseListAdapter = new HomeCourseListAdapter(mContext, microClassList);
        homeCourseListAdapter.clearList();
        list_live.setAdapter(homeCourseListAdapter);
        list_live.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (microClassList.size() > 0) {
                    MicroClassListBean.DataBean bean = microClassList.get(0);

                    Intent intent = new Intent();
                    intent.setClass(mContext, MobClassActivity.class);

//                    IMoocPackConstant.Instance().packid = bean.getId();
//                    IMoocPackConstant.Instance().ownerid = Integer.parseInt(bean.getOwnerid());
//                    IMoocPackConstant.Instance().appId = Constant.APPID;
//                    IMoocPackConstant.Instance().desc = bean.getDesc();
//                    IMoocPackConstant.Instance().curPackPrice = Double.parseDouble(bean.getPrice());
//                    IMoocPackConstant.Instance().CourseNum = bean.getClassNum();

                    intent.putExtra("viewCount", bean.getViewCount());
                    intent.putExtra("packname", bean.getName());
                    intent.putExtra("position", 0);
                    intent.putExtra("coursenum", bean.getClassNum());
                    startActivity(intent);
                } else {
//                    if (getActivity() != null) {
//                        ((MainActivity) getActivity()).setTabSelection(1);
//                    }
                    startActivity(new Intent(mContext, MobClassActivity.class));

                }
            }
        });

        getMicroClassData();
    }

    private void getMicroClassData() {
        String n1 = "1";
        String n2 = "5";
        String n3 = "6";
        int type = Integer.parseInt(Constant.APP_CONSTANT.TYPE());
        String id = type == 1 ? "1" : (type == 2 ? "5" : "6");
        if (BuildConfig.isEnglish) {
            if (Constant.APP_CONSTANT.TYPE().equals("4")) {
                id = "2";
            } else {
                id = "4";
            }
        }

        String url = "http://class.iyuba.com/getClass.iyuba?protocol=10102&id=" + id + "&type=1&pageNumber=1&pageCounts=5" +
                "&sign=" + MD5.getMD5ofStr("10102class" + id);
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(okhttp3.Call call, String response) {
                MicroClassListBean bean = new Gson().fromJson(response, MicroClassListBean.class);
                if (bean.getResult() != 1 || bean.getData() == null || bean.getData().size() <= 0) {
                    return;
                }
                microClassList.clear();
                microClassList.addAll(bean.getData());
                homeCourseListAdapter.notifyDataSetChanged();

                CoursePackOp coursePackOp = new CoursePackOp(mContext);
                coursePackOp.deleteCoursePackData();
                coursePackOp.insertCoursePacks(toCoursePack(microClassList));
            }

            @Override
            public void onError(okhttp3.Call call, Exception e) {
            }
        });
    }

    private List<CoursePack> toCoursePack(ArrayList<MicroClassListBean.DataBean> microClassList) {
        ArrayList<CoursePack> list = new ArrayList<>();
        for (MicroClassListBean.DataBean dataBean : microClassList) {
            CoursePack coursePack = new CoursePack();
            coursePack.name = dataBean.getName();
            coursePack.classNum = dataBean.getClassNum();
            coursePack.desc = dataBean.getDesc();
            coursePack.id = dataBean.getId();
            coursePack.ownerid = Integer.parseInt(dataBean.getOwnerid());
            coursePack.picUrl = dataBean.getPic();
            coursePack.realprice = Double.parseDouble(dataBean.getRealprice());
            coursePack.price = Double.parseDouble(dataBean.getPrice());
            coursePack.viewCount = dataBean.getViewCount();
            list.add(coursePack);
        }
        return list;
    }

    private void setImages() {
        if (NetWorkState.isConnectingToInternet()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AddImageRequest request = new AddImageRequest(Constant.HEAD);
                    ExeProtocol.exe(request, new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            isBannerRequested = true;
                            AddImageResponse response = (AddImageResponse) bhr;
                            if (response.result.equals("1")) {
                                Log.e("setImages", "result=1");
                                card_list = response.imageDatas;
                                DataSupport.deleteAll(ImageData.class);
                                DataSupport.saveAll(card_list);
                                pagerAdapter.clear();
                                for (int i = 0; i < card_list.size(); i++) {
                                    pagerAdapter.addCardItem(card_list.get(i));
                                }
                                handler.sendEmptyMessage(2);
                                handler.sendEmptyMessageDelayed(3, 5000);
                                handler.sendEmptyMessageDelayed(7, 3000);
                            }
                        }

                        @Override
                        public void error() {
                            isBannerRequested = false;
                            handler.sendEmptyMessage(8);
                        }
                    });
                }
            }).start();
        } else {
            handler.sendEmptyMessage(6);
        }
    }

    /**
     * 接入有道广告
     * 功能：set ListView for year
     */
    private void setAdView() {
        Log.e("setAdView", "YouDaoAdAdapter");
        youdaoAdapter = new YouDaoAdAdapter(mContext, homeCourseListAdapter,
                YouDaoNativeAdPositioning.newBuilder()
                        .addFixedPosition(0)
                        .build());
        // 绑定界面组件与广告参数的映射关系，用于渲染广告
        final YouDaoNativeAdRenderer adRenderer = new YouDaoNativeAdRenderer(
                new ViewBinder.Builder(R.layout.lib_native_ad_row)
                        .titleId(R.id.native_title)
                        .mainImageId(R.id.native_main_image)
                        .build());
        youdaoAdapter.registerAdRenderer(adRenderer);
        final Location location = null;
        final String keywords = null;
        // 声明app需要的资源，这样可以提供高质量的广告，也会节省网络带宽
        final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE, RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE, RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);
        mRequestParameters = new RequestParameters.Builder()
                .location(location).keywords(keywords)
                .desiredAssets(desiredAssets).build();
        list_live.setAdapter(youdaoAdapter);
        youdaoAdapter.loadAds("5542d99e63893312d28d7e49e2b43559",
                mRequestParameters);
    }

    /**
     * 获得spannableDate
     *
     * @param str
     * @return
     */
    private SpannableStringBuilder getSpannableDate(String str) {
        SpannableStringBuilder ss = new SpannableStringBuilder(str);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(127, 198, 80)), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(45), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(127, 198, 80)),
                str.length() - 1, str.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(45), str.length() - 1, str.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), str.length() - 1,
                str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(240, 100, 51)), 5,
                str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(55), 5, str.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 5, str.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void setData() {
        chinese.setText(sayings.chinese);
        english.setText(sayings.english);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_practice:
                AbilityTestListActivity.actionStart(mContext, 2);
                break;
            case R.id.ll_test:
                AbilityMapActivity.actionStart(mContext, 1, -1);
                break;
            case R.id.ll_vocabulary:
                intent = new Intent(mContext, Cet4WordList.class);
                startActivity(intent);
                break;
            case R.id.iv_fuction:
                showQQDialog(v);


                break;

            case R.id.iv_vip:
                intent = new Intent();
                intent.setClass(mContext, WordCollection.class);
                startActivity(intent);
                break;
            case R.id.rl_more:
//                if (BuildConfig.isEnglish) {
//                    intent = new Intent(mContext, InfoActivity.class);
//                    startActivity(intent);
//                } else {
                intent = new Intent(mContext, JpBlogListActivity.class);
                startActivity(intent);
//                }
                break;
            default:
                break;
        }
    }

    public void showQQDialog(View v) {
        final boolean isEnglish = Constant.APP_CONSTANT.isEnglish();
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home_qq, popupMenu.getMenu());

        popupMenu.getMenu().getItem(0).setTitle(
                isEnglish ? String.format("%s用户群: %s", BrandUtil.getBrandChinese(), BrandUtil.getQQGroupNumber(mContext)) : "安卓用户群: 586621024");
        popupMenu.getMenu().getItem(1).setTitle(isEnglish ? "客服QQ: 2326344291" : "内容QQ: 3274422495");
        popupMenu.getMenu().getItem(2).setTitle("技术QQ: 1549330086");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.qq_group:
                        Util.startQQGroup(mContext, isEnglish ? BrandUtil.getQQGroupKey(mContext) : "YLNknASyf_6d6M98DVFHyMnSzkkLxcjA");
                        return true;
                    case R.id.qq_server:
                        Util.startQQ(mContext, isEnglish ? "2326344291" : "3274422495");
                        return true;
                    case R.id.qq_tech:
                        Util.startQQ(mContext, "1549330086");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        if (ConfigManager.Instance().loadInt("isvip") == 0) {
            setAdView();
        } else if (list_live.getAdapter() instanceof YouDaoAdAdapter) {
            list_live.setAdapter(homeCourseListAdapter);
        }

        if (!isBannerRequested) {
            setImages();
        }
    }

}
