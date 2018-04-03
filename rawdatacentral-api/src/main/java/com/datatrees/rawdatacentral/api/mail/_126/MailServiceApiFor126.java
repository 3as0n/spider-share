package com.datatrees.rawdatacentral.api.mail._126;

import com.datatrees.rawdatacentral.domain.plugin.CommonPluginParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;

/**
 * 126邮箱模拟登陆接口
 * User: yand
 * Date: 2018/2/27
 */
public interface MailServiceApiFor126 {

    /**
     * 提交登陆请求
     * 必填参数: taskId,username,password
     * <p>
     * 结果异步获取
     * 详见:@see com.datatrees.rawdatacentral.domain.result.ProcessResult
     * </p>
     * @return
     */
    HttpResult<Object> login(CommonPluginParam param);

    /**
     * 刷新登陆二维码
     * 必填参数: taskId
     * <p>
     * 结果异步获取
     * 详见:@see com.datatrees.rawdatacentral.domain.result.ProcessResult
     * </p>
     * @param param
     * @return
     */
    HttpResult<Object> refeshQRCode(CommonPluginParam param);

    /**
     * 查询二维码登陆状态
     * 必填参数: taskId
     * <p>
     * 二维码状态详见:@see com.datatrees.rawdatacentral.domain.enums.QRStatus
     * </p>
     * @param param
     * @return
     */
    HttpResult<Object> queryQRStatus(CommonPluginParam param);


}