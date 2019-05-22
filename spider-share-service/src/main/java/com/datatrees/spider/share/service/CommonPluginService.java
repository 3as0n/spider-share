/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.datatrees.spider.share.service;

import com.datatrees.spider.share.domain.CommonPluginParam;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.domain.http.Cookie;
import com.datatrees.spider.share.domain.http.HttpResult;

import java.util.List;

/**
 * 通用插件服务
 */
public interface CommonPluginService {

    /**
     * 初始化
     * 
     * @param param
     * @return
     */
    HttpResult<Object> init(CommonPluginParam param);

    /**
     * 刷新图片验证码
     * 
     * @param param
     * @return
     */
    HttpResult<Object> refeshPicCode(CommonPluginParam param);

    /**
     * 刷新短信验证码
     * 
     * @param param
     * @return
     */
    HttpResult<Object> refeshSmsCode(CommonPluginParam param);

    /**
     * 验证图片验证码
     * 
     * @return
     */
    HttpResult<Object> validatePicCode(CommonPluginParam param);

    /**
     * 登录提交
     * 
     * @return
     */
    HttpResult<Object> submit(CommonPluginParam param);

    /**
     * 自定义方法入口
     * 
     * @return
     */
    HttpResult<Object> defineProcess(CommonPluginParam param);

    /**
     * 发送登陆成功消息
     * 
     * @param loginMessage
     */
    void sendLoginSuccessMsg(String topic, LoginMessage loginMessage);

    /**
     * 发送登陆成功消息
     * 
     * @param loginMessage
     */
    void sendLoginSuccessMsg(String topic, LoginMessage loginMessage, List<Cookie> cookies);

    /**
     * 刷新登陆二维码
     * 
     * @param param
     * @return
     */
    HttpResult<Object> refeshQRCode(CommonPluginParam param);

    /**
     * 查询二维码状态
     * 
     * @param param
     * @return
     */
    HttpResult<Object> queryQRStatus(CommonPluginParam param);

}
