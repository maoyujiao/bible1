/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.listener;

/**
 * 类操作完成回调
 *
 * @author 陈彤
 */
public interface OperateCallBack {
    void success(String message);

    void fail(String message);
}
