package com.iyuba.CET4bible.util.exam;

import android.content.Context;

import com.iyuba.base.util.L;
import com.iyuba.base.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * ExamDbHelper
 *
 * @author wayne
 * @date 2018/1/19
 */
public class ExamDbHelper {
    public static List<ExamListBean.DataBean> insertExamList(Context context, List<ExamListBean.DataBean> list) {
        List<ExamListBean.DataBean> updateList = new ArrayList<>();
        if (list.size() == 0) {
            // 没有网络数据
            return updateList;
        }

        ExamListOp examListOp = new ExamListOp(context);
        List<DbExamListBean> dbList = examListOp.findAll();

        for (DbExamListBean dbExamListBean : dbList) {
            L.e("localBean ::::  " + dbExamListBean.toString());
        }

        if (dbList.size() == 0) {
            // 本地没有数据
            L.e("== DB == 本地没有数据考试列表全部插入");
            examListOp.write(list);
            for (ExamListBean.DataBean dataBean : list) {
                if (!"1".equals(dataBean.getVersion())) {
                    updateList.add(dataBean);
                }
            }
            return updateList;
        }

        for (int i = 0; i < list.size(); i++) {
            ExamListBean.DataBean serverBean = list.get(i);
            L.e("serverBean ::::  " + serverBean.toString());

            boolean flag = true;
            for (int k = 0; k < dbList.size(); k++) {
                DbExamListBean bean = dbList.get(k);
                // 如果是同一套题
                if (bean.year.equals(serverBean.getId())) {
                    flag = false;
                    // 如果本地版本号小于服务器版本号
                    if (compareVersion(bean.version, serverBean.getVersion())) {
                        L.e("== DB == 版本号升级 == " + serverBean.getName() + "  ::: " + serverBean.getId());
                        L.e("== DB == 版本号升级 == 本地版本号 ：：" + bean.version + "  新版本号 ：： " + serverBean.getVersion());
                        updateList.add(serverBean);
                        examListOp.write(serverBean);
                    }
                }
            }

            if (flag) {
                L.e("== DB == 本地缺少部分考试列表");
                examListOp.write(serverBean);
            }
        }
        return updateList;
    }

    private static boolean compareVersion(String version, String serverVersion) {
        try {
            int local = Integer.parseInt(version);
            int server = Integer.parseInt(serverVersion);
            L.e("version === local :::  " + local + "   server ::::   " + serverVersion);
            return server > local;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void removeData(Context context, List<ExamListBean.DataBean> updateList) {
        ExamListOp examListOp = new ExamListOp(context);
        for (ExamListBean.DataBean dataBean : updateList) {
            examListOp.deleteAllTable(dataBean.getId());
        }
    }

    public static void insertExamListenData(Context mContext, String section, ExamDataBean examData) {
        ExamListOp examListOp = new ExamListOp(mContext);
        try {
            for (int i = 0; i < examData.getItemNum(); i++) {
                ExamDataBean.ItemListBean data = examData.getItemList().get(i);
                examListOp.insertListenData(section, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            T.showShort(mContext, "题库加载失败");

            try {
                examListOp.deleteAllTable(examData.getItemList().get(0).getExplain().get(0).getTestTime());
            } catch (Exception x) {

            }
        }
    }
}
