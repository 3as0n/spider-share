package com.datatrees.spider.bank.api;

import com.datatrees.spider.share.domain.CommonPluginParam;
import com.datatrees.spider.share.domain.http.HttpResult;

/**
 * QQ邮箱模拟登陆接口
 * @author zhouxinghai
 * @date 2017/12/29
 */
public interface MailServiceApiForQQ {

    /**
     * 提交登陆请求
     * 必填参数: taskId,username,password
     * <p>
     * 结果异步获取
     * 详见:@see com.datatrees.spider.spider.share.domain.ProcessResult
     * </p>
     * @return
     */
    HttpResult<Object> login(CommonPluginParam param);

    /**
     * 刷新登陆二维码
     * 必填参数: taskId
     * <p>
     * 结果异步获取
     * 详见:@see com.datatrees.spider.spider.share.domain.ProcessResult
     * </p>
     * @param param
     * @return
     */
    HttpResult<Object> refeshQRCode(CommonPluginParam param);

    /**
     * 查询二维码登陆状态
     * 必填参数: taskId
     * <p>
     * 二维码状态详见:com.datatrees.spider.spider.share.domain.QRStatus
     * </p>
     * @param param
     * @return
     */
    HttpResult<Object> queryQRStatus(CommonPluginParam param);
}
