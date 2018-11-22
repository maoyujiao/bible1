package com.iyuba.CET4bible.util.exam;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.base.http.Http;
import com.iyuba.base.http.HttpCallback;
import com.iyuba.base.util.SP;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * ExamDataUtil
 *
 * @author wayne
 * @date 2018/1/19
 */
public class ExamDataUtil {
    public static String getImageUrl(String name) {
        return "http://m.iyuba.com/ncet/coverImg/" + name;
    }
    public static boolean isFirstRequestData(Context context) {
        return (boolean) SP.get(context, "sp_exam_list_first", true);
    }

    public static void setFirstRequestData(Context context, boolean value) {
        SP.put(context, "sp_exam_list_first", value);
    }

    public static void writeListData2DB(Context context, List<ExamListBean.DataBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        List<ExamListBean.DataBean> updateList = ExamDbHelper.insertExamList(context, list);
        ExamDataUtil.removeData(context, updateList);

    }

    public static void removeData(Context context, List<ExamListBean.DataBean> list) {
        ExamDbHelper.removeData(context, list);
    }

    public static void writeExamData2DB(Context mContext, String section, ExamDataBean examData) {
        ExamDbHelper.insertExamListenData(mContext, section, examData);
    }

    public interface ListCallback {
        void onLoadData(List<ExamListBean.DataBean> list);
    }

    public interface DataCallback {
        void onLoadData(boolean success);
    }

    public static void requestList(String type, final ListCallback callback) {
        String url = "http://m.iyuba.com/ncet/getCetTestList.jsp?level=" + type;
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    ExamListBean bean = new Gson().fromJson(response, ExamListBean.class);
                    if (bean.getData() != null) {
                        callback.onLoadData(bean.getData());
                    } else {
                        callback.onLoadData(new ArrayList<ExamListBean.DataBean>());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    callback.onLoadData(new ArrayList<ExamListBean.DataBean>());
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                callback.onLoadData(new ArrayList<ExamListBean.DataBean>());
            }
        });
    }

    public static void requestExamData(final Context mContext, String type, final String section, String id, final DataCallback callback) {
        String url = String.format("http://m.iyuba.com/ncet/getCetTestDetailNew.jsp?level=%s&section=%s&id=%s", type, section, id);
        Log.d("diaodebug",url+"");
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    ExamDataBean bean = new Gson().fromJson(response, ExamDataBean.class);
                    ExamDataUtil.writeExamData2DB(mContext, section, bean);
                    callback.onLoadData(true);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    callback.onLoadData(false);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                callback.onLoadData(false);
            }
        });
    }
}
