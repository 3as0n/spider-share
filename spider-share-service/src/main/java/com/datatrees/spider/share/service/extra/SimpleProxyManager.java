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

package com.datatrees.spider.share.service.extra;

import com.datatrees.spider.share.common.share.service.ProxyService;
import com.datatrees.spider.share.common.utils.CheckUtils;
import com.treefinance.crawler.framework.proxy.Proxy;
import com.treefinance.crawler.framework.proxy.ProxyManager;
import com.treefinance.crawler.framework.proxy.ProxyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理,重新定义,以taskId为唯一标识
 */
public class SimpleProxyManager implements ProxyManager {

    private static final Logger logger = LoggerFactory.getLogger(SimpleProxyManager.class);

    /**
     * proxy dubbo service
     */
    private ProxyService proxyService;

    /**
     * 当前代理
     */
    private Proxy last;

    /**
     * 根据taskId获取,全部session模式,
     */
    private Long taskId;

    private String websiteName;

    public SimpleProxyManager(Long taskId, String websiteName, ProxyService proxyService) {
        CheckUtils.checkNotNull(taskId, "taskId is null");
        CheckUtils.checkNotNull(proxyService, "proxyService is null");
        CheckUtils.checkNotBlank(websiteName, "websiteName is null");
        this.taskId = taskId;
        this.proxyService = proxyService;
        this.websiteName = websiteName;
    }

    @Override
    public Proxy getProxy() throws Exception {
        if (last == null) {
            try {
                com.treefinance.proxy.domain.Proxy proxy = proxyService.getProxy(taskId, websiteName);
                if (null != proxy) {
                    last = new Proxy(proxy.getIp(), Integer.parseInt(proxy.getPort()));
                }
            } catch (Exception e) {
                logger.error("getProxy error taskId={},websiteName={}", taskId, websiteName, e);
            }
        }

        return last;
    }

    @Override
    public void callBackProxy(ProxyStatus status) throws Exception {
        // TODO: 2017/8/4
    }

    @Override
    public void release() throws Exception {
        try {
            proxyService.release(taskId);
        } catch (Exception e) {
            logger.error("release proxy error taskId={},websiteName={}", taskId, websiteName, e);
        }
    }

}