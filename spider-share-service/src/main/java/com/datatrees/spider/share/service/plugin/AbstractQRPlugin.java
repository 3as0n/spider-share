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
 * @author Jerry
 * @date 2019-02-14 15:38
 */
public abstract class AbstractQRPlugin implements QRPlugin, CommonPlugin {

    @Override
    public HttpResult<Object> init(CommonPluginParam param) {
        return null;
    }

    @Override
    public HttpResult<Object> refeshPicCode(CommonPluginParam param) {
        return null;
    }

    @Override
    public HttpResult<Object> refeshSmsCode(CommonPluginParam param) {
        return null;
    }

    @Override
    public HttpResult<Object> validatePicCode(CommonPluginParam param) {
        return null;
    }

    @Override
    public HttpResult<Object> submit(CommonPluginParam param) {
        return null;
    }

    @Override
    public HttpResult<Object> defineProcess(CommonPluginParam param) {
        return null;
    }

}
