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

package com.treefinance.crawler.framework.process.service.impl;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.google.common.base.Preconditions;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.service.PluginService;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.extension.plugin.PluginCaller;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.extension.plugin.PluginUtil;
import com.treefinance.crawler.framework.process.service.ServiceBase;
import com.treefinance.crawler.framework.protocol.ProtocolStatusCodes;
import com.treefinance.crawler.framework.proxy.Proxy;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.Map;

public class PluginServiceImpl extends ServiceBase<PluginService> {

    private final int retryCount = PropertiesConfiguration.getInstance().getInt("pluginService.retry.count", 3);

    public PluginServiceImpl(@Nonnull PluginService service) {
        super(service);
    }

    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        PluginService service = getService();
        LinkNode current = RequestUtil.getCurrentUrl(request);
        String url = current.getUrl();
        SearchProcessorContext context = (SearchProcessorContext)request.getProcessorContext();
        logger.info("handling request using plugin : {}", url);
        AbstractPlugin plugin = service.getPlugin();
        if (plugin != null) {
            for (int i = 0; i < retryCount; i++) {
                try {
                    String serviceResult = PluginCaller.call(plugin, context, () -> {
                        Map<String, String> params = new LinkedHashMap<>();
                        params.put(PluginConstants.CURRENT_URL, url);
                        params.put(PluginConstants.REDIRECT_URL, current.getRedirectUrl());
                        current.getPropertys().forEach((key, val) -> params.put(key, val + ""));
                        if (context.needProxyByUrl(url)) {
                            Proxy proxy = context.getProxy();
                            Preconditions.checkNotNull(proxy);
                            logger.info("set proxy to: " + url + "\tproxy:\t" + proxy.format());
                            params.put(PluginConstants.PROXY, proxy.format());
                        }

                        return params;
                    }).toString();

                    logger.debug("PluginServiceImpl output content : {} url : {}", serviceResult, url);
                    // get plugin json result
                    Map<String, Object> pluginResultMap = PluginUtil.checkPluginResult(serviceResult);
                    serviceResult = StringUtils.defaultIfEmpty((String)pluginResultMap.get(PluginConstants.SERVICE_RESULT), "");

                    response.setOutPut(serviceResult);

                    RequestUtil.setContent(request, serviceResult);
                    ProcessorContextUtil.addThreadLocalLinkNode(context, current);
                    ProcessorContextUtil.addThreadLocalResponse(context, response);
                    break;
                } catch (Exception e) {
                    logger.error("do plugin service error url:" + url + " , do revisit ... " + e.getMessage(), e);
                    if (i == retryCount - 1) {
                        response.setStatus(ProtocolStatusCodes.EXCEPTION);
                        throw e;
                    }
                    current.increaseRetryCount();
                }
            }
        }

    }
}
