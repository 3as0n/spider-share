/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
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

package com.datatrees.spider.share.service.plugin;

import com.datatrees.spider.share.domain.CommonPluginParam;
import com.datatrees.spider.share.domain.http.HttpResult;

/**
 * 通用插件 使用前自行约定 Created by zhouxinghai on 2018/01/04.
 */
public interface CommonPlugin {

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

}
