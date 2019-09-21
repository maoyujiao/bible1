package com.iyuba.core.discover.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.activity.Web;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.teacher.activity.FindTeacherActivity;
import com.iyuba.core.teacher.activity.TeacherBaseInfo;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.headlinelibrary.ui.search.MSearchActivity;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.module.movies.ui.movie.MovieActivity;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class DiscoverForAt extends BaseActivity {
    private Context mContext;
    private View exam, all, searchWord, findFriend, vibrate,
            collectWord, saying, back, discover_search_teacher,
            discover_search_certeacher, discover_search_circlefriend,
            latestAc, exGiftAc;
    private View ll_pratice;

    private OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent intent;
            int id = v.getId();
            if (id == R.id.button_back) {
                finish();
            } else if (id == R.id.all) {
                intent = new Intent();
                intent.setClass(mContext, Web.class);
                intent.putExtra("url", "http://app.iyuba.cn/android");
                intent.putExtra("title",
                        mContext.getString(R.string.discover_appall));
                startActivity(intent);
            } else if (id == R.id.search_word) {
                intent = new Intent();
                intent.setClass(mContext, SearchWord.class);
                startActivity(intent);
            } else if (id == R.id.saying) {
                intent = new Intent();
                intent.setClass(mContext, Saying.class);
                startActivity(intent);
            } else if (id == R.id.collect_word) {
                intent = new Intent();
                intent.setClass(mContext, WordCollection.class);
                startActivity(intent);
            } else if (id == R.id.discover_search_friend) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent();
                    intent.setClass(mContext, SearchFriend.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            } else if (id == R.id.discover_search_teacher) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent();
                    intent.setClass(mContext, FindTeacherActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, FindTeacherActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.discover_search_certeacher) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }
                    intent = new Intent();
                    intent.setClass(mContext, TeacherBaseInfo.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            } else if (id == R.id.discover_search_circlefriend) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent();
                    intent.setClass(mContext, FriendCircFreshListActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            }else if (id == R.id.ll_pratice){
                EventBus.getDefault().post(new GoPracticeEvent());
            }
//            else if (id == R.id.discover_latest_activity) {
//                if (AccountManager.Instance(mContext).checkUserLogin()) {
//                    intent = new Intent();
//                    intent.setClass(mContext, Web.class);
//                    intent.putExtra("url", "http://www.iyuba.cn/book/book.jsp?uid="
//                            + AccountManager.Instance(mContext).getId() + "&platform=android&appid="
//                            + Constant.APPID);
//                    intent.putExtra("title",
//                            mContext.getString(R.string.discover_iyubaac));
//                    startActivity(intent);
//                } else {
//                    intent = new Intent();
//                    intent.setClass(mContext, Login.class);
//                    startActivity(intent);
//                }
//            }
            else if (id == R.id.discover_exchange_gift) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    intent = new Intent();
                    intent.setClass(mContext, Web.class);
                    intent.putExtra("url", "http://m.iyuba.cn/mall/index.jsp?"
                            + "&uid=" + AccountManager.Instace(mContext).getId()
                            + "&sign=" + MD5.getMD5ofStr("iyuba" + AccountManager.Instace(mContext).getId() + "camstory")
                            + "&username=" + AccountManager.Instace(mContext).getUserName()
                            + "&platform=android&appid="
                            + Constant.APPID);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);
        mContext = this;
        initWidget();
    }

    private void initWidget() {

        TextView tv_moreapp = findViewById(R.id.tv_moreapp);
        tv_moreapp.setVisibility(View.GONE);
        back = findViewById(R.id.button_back);
        back.setOnClickListener(ocl);
        all = findViewById(R.id.all);
        all.setVisibility(View.GONE);
        all.setOnClickListener(ocl);
        searchWord = findViewById(R.id.search_word);
        searchWord.setOnClickListener(ocl);
        saying = findViewById(R.id.saying);
        saying.setOnClickListener(ocl);
        saying.setVisibility(View.GONE);
        collectWord = findViewById(R.id.collect_word);
        collectWord.setOnClickListener(ocl);
        findFriend = findViewById(R.id.discover_search_friend);
        discover_search_certeacher =
                findViewById(R.id.discover_search_certeacher);
        discover_search_circlefriend =
                findViewById(R.id.discover_search_circlefriend);
        ll_pratice =
                findViewById(R.id.ll_pratice);
        ll_pratice.setOnClickListener(ocl);
        discover_search_teacher =
                findViewById(R.id.discover_search_teacher);
        discover_search_circlefriend.setOnClickListener(ocl);
        discover_search_certeacher.setOnClickListener(ocl);
        discover_search_teacher.setOnClickListener(ocl);
        findFriend.setOnClickListener(ocl);
        vibrate = findViewById(R.id.discover_vibrate);
        vibrate.setOnClickListener(ocl);

//		latestAc = findViewById(R.id.discover_latest_activity);
//		latestAc.setOnClickListener(ocl);
        exGiftAc = findViewById(R.id.discover_exchange_gift);
        exGiftAc.setOnClickListener(ocl);

        if (AccountManager.Instace(mContext).isteacher.equals("1")) {
            discover_search_certeacher.setVisibility(View.VISIBLE);

        } else {
            discover_search_certeacher.setVisibility(View.VISIBLE);

        }

        // 优惠券
        findViewById(R.id.me_privilege).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    String date = sdf.format(new Date());

                    Intent intent = new Intent();
                    intent.setClass(mContext, Web.class);
//                    http://vip.iyuba.cn/mycode.jsp?uid=3830618&appid=148&sign=a4fa0c78b161c2f7b6fe91fdab352553
                    intent.putExtra("url", "http://vip.iyuba.cn/mycode.jsp?"
                            + "uid=" + AccountManager.Instace(mContext).userId
                            + "&appid=" + Constant.APPID
                            + "&sign=" + com.iyuba.core.me.pay.MD5.getMD5ofStr(AccountManager.Instace(mContext).userId + "iyuba" + Constant.APPID + date));
                    intent.putExtra("title",
                            "优惠券");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.ll_souyisou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setImovieStatus();
                startActivity(MSearchActivity.buildIntent(mContext, new String[]{"noqw"}));
            }
        });
        findViewById(R.id.ll_kanyikan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setImovieStatus();
                startActivity(new Intent(mContext, MovieActivity.class));
            }
        });

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
        if (AccountManager.Instace(mContext).isteacher.equals("1")) {
            discover_search_certeacher.setVisibility(View.VISIBLE);

        } else {
            discover_search_certeacher.setVisibility(View.VISIBLE);

        }
    }

    private void setImovieStatus() {
        ImoocManager.appId = Constant.APPID;
        User user = new User();

        if (AccountManager.Instace(mContext.getApplicationContext())
                .checkUserLogin()) {
            user.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
            user.name = AccountManager.Instace(mContext).getUserName();
            user.uid = Integer.parseInt(ConfigManager.Instance().loadString("userId"));
            IyuUserManager.getInstance().setCurrentUser(user);
        } else {
            user.vipStatus = "0";
            user.name = "";
            user.uid = 0;
            IyuUserManager.getInstance().setCurrentUser(user);
        }
    }
}
