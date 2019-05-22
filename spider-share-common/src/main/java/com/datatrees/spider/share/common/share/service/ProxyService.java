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

package com.datatrees.spider.share.common.share.service;

import com.treefinance.proxy.domain.Proxy;

@Deprecated
public interface ProxyService {

    /**
     * 获取代理
     * 
     * @param taskId 任务ID
     * @param websiteName 站点标识
     * @return 代理 {@link Proxy}
     */
    Proxy getProxy(Long taskId, String websiteName);

    /**
     * 通知wise-proxy释放代理
     * 
     * @param taskId 任务ID
     */
    void release(Long taskId);

    /**
     * 清理与任务绑定的代理
     * 
     * @param taskId 任务ID
     */
    void clear(Long taskId);
}
