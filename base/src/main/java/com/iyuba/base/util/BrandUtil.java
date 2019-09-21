package com.iyuba.base.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iyuba.base.http.Http;
import com.iyuba.base.http.HttpCallback;

import okhttp3.Call;

/**
 * BrandUtil
 *
 * @author wayne
 * @date 2017/11/18
 */
public class BrandUtil {

    private static final String BRAND_HUAWEI = "huawei";
    private static final String BRAND_XIAOMI = "xiaomi";
    private static final String BRAND_MEIZU = "meizu";
    private static final String BRAND_VIVO = "vivo";
    private static final String BRAND_OPPO = "oppo";
    private static final String BRAND_SAMSUNG = "samsung";
    private static final String BRAND_GIONEE = "gionee";
    private static final String BRAND_360 = "360";
    private static final String BRAND_OTHER = "android";

    private static String brandName;

    private static String getBrandName() {
        if (TextUtils.isEmpty(brandName)) {
            brandName = setBrandName();
        }
        return brandName;
    }

    public static String getBrandChinese() {
        String brand = getBrandName();
        switch (brand) {
            case BRAND_HUAWEI:
                return "华为";
            case BRAND_VIVO:
                return "Vivo";
            case BRAND_OPPO:
                return "Oppo";
            case BRAND_XIAOMI:
                return "小米";
            case BRAND_SAMSUNG:
                return "三星";
            case BRAND_GIONEE:
                return "金立";
            case BRAND_MEIZU:
                return "魅族";
            case BRAND_360:
                return "360";
            default:
                return "安卓";
        }
    }

    private static String setBrandName() {
        String brand = Build.MANUFACTURER.trim().toLowerCase();
//        if (brand.contains("huawei") || brand.contains("honor")
//                || brand.contains("nova") || brand.contains("mate")) {
//            return BRAND_HUAWEI;
//        }
//        if (brand.contains("xiaomi")) {
//            return BRAND_XIAOMI;
//        }
//        if (brand.contains("vivo")) {
//            return BRAND_VIVO;
//        }
//        if (brand.contains("oppo")) {
//            return BRAND_OPPO;
//        }
//        if (brand.contains("samsung")) {
//            return BRAND_SAMSUNG;
//        }
//        if (brand.contains("meizu")) {
//            return BRAND_MEIZU;
//        }
//        // 金立
//        if (brand.contains("gionee")) {
//            return BRAND_GIONEE;
//        }
//        if (brand.contains("360") || brand.contains("qiku")
//                || brand.contains("qiho") || brand.contains("qihu")) {
//            return BRAND_360;
//        }
        return brand;
    }


    private static String getQQGroupNumber(String brand) {
        switch (brand) {
            case BRAND_HUAWEI:
                return "705250027";
            case BRAND_VIVO:
                return "433075910";
            case BRAND_OPPO:
                return "334687859";
            case BRAND_XIAOMI:
                return "499939472";
            case BRAND_SAMSUNG:
                return "639727892";
            case BRAND_GIONEE:
                return "621392974";
            case BRAND_MEIZU:
                return "625401994";
            case BRAND_360:
                return "625355797";
            default:
                return "540297996";
        }
    }

    private static String getQQGroupKey(String brand) {
        switch (brand) {
            case BRAND_HUAWEI:
                return "X_XYfsL0_-ewHkXpIUhlwpbxvQcxEWLb";
            case BRAND_VIVO:
                return "CFBROmhoDx_440-ukjYYugIf61SSujRC";
            case BRAND_OPPO:
                return "Yuhyc18Q34Lmy0b6W1HeXuDG3TdferpX";
            case BRAND_XIAOMI:
                return "9UmuKvpLjV-ib9W-bDSgEok_KyvAZYQ";
            case BRAND_SAMSUNG:
                return "4LU-47yf_P510zgmdp98miJtDx366Ty5";
            case BRAND_GIONEE:
                return "pb42xTKokAQVzo1buzX95skntd5UOLUQ";
            case BRAND_MEIZU:
                return "Tg2nNWD9wFOqu_RTX6FKcfy1Ay4TkGcD";
            case BRAND_360:
                return "0yHQOAWPGPOPacORm2BXdOblJZvlzeLw";
            default:
                return "N69aGfm78SVXbgCrpHalYlHxRMPh7LHz";
        }
    }


    public static String getQQGroupNumber(Context context) {
        return (String) SP.get(context, "sp_qq_group_number", getQQGroupNumber(getBrandName()));
    }

    public static String getQQGroupKey(Context context) {
        return (String) SP.get(context, "sp_qq_group_key", getQQGroupKey(getBrandName()));
    }

    public static void requestQQGroupNumber(final Context context) {
        String url = "http://m.iyuba.cn/m_login/getQQGroup.jsp?type=cet";
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    QQGroupBean bean = new Gson().fromJson(response, QQGroupBean.class);
                    if ("true".equals(bean.message)) {
                        SP.put(context, "sp_qq_group_number", bean.QQ);
                        SP.put(context, "sp_qq_group_key", bean.key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
            }
        });
    }

    class QQGroupBean {

        /**
         * message : true
         * QQ : 433075910
         * key : lr0jfBh_9Ly0S3iUPUnCSNhAV8UkiQRI
         */

        public String message;
        public String QQ;
        public String key;
    }
}
