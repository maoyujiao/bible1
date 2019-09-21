/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.listener;

import com.iyuba.core.protocol.BaseHttpResponse;

/**
 * 协议操作完成回调
 *
 * @author 陈彤
 */
public interface ProtocolResponse {
    void finish(BaseHttpResponse bhr);

    void error();
}
