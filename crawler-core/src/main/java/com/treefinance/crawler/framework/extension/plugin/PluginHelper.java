/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.extension.plugin;

import java.util.Objects;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.common.protocol.ProtocolOutput;
import com.datatrees.crawler.core.processor.AbstractProcessorContext;
import com.datatrees.crawler.core.processor.SearchProcessorContext;
import com.datatrees.crawler.core.processor.bean.LinkNode;
import com.datatrees.crawler.core.processor.common.ProcessorFactory;
import com.datatrees.crawler.core.processor.common.RequestUtil;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.crawler.core.processor.proxy.Proxy;
import com.datatrees.crawler.core.processor.service.ServiceBase;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerry
 * @since 18:35 16/11/2017
 */
public final class PluginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginHelper.class);

    private PluginHelper() {
    }

    public static String requestAsString(LinkNode linkNode, Integer retry) throws Exception {
        Response response = invokeDefaultService(linkNode, retry);
        return StringUtils.defaultString((String) response.getOutPut());
    }

    public static ProtocolOutput sendRequest(LinkNode linkNode, Integer retry) throws Exception {
        return sendRequest(linkNode, ProcessContextHolder.getProcessorContext(), retry);
    }

    public static ProtocolOutput sendRequest(LinkNode linkNode, AbstractProcessorContext processorContext, Integer retry) throws Exception {
        Response response = invokeDefaultService(linkNode, processorContext, retry);

        return ResponseUtil.getProtocolResponse(response);
    }

    public static Response invokeDefaultService(LinkNode linkNode, Integer retry) throws Exception {
        return invokeDefaultService(linkNode, ProcessContextHolder.getProcessorContext(), retry);
    }

    public static Response invokeDefaultService(LinkNode linkNode, AbstractProcessorContext processorContext, Integer retry) throws Exception {
        return invokeDefaultService(linkNode, processorContext, null, retry);
    }

    public static Response invokeDefaultService(LinkNode linkNode, AbstractProcessorContext processorContext, Configuration configuration, Integer retry) throws Exception {
        Objects.requireNonNull(linkNode);
        Objects.requireNonNull(processorContext);
        Request request = new Request();
        RequestUtil.setCurrentUrl(request, linkNode);
        RequestUtil.setProcessorContext(request, processorContext);
        RequestUtil.setContext(request, processorContext.getContext());
        if (configuration != null) {
            RequestUtil.setConf(request, configuration);
        } else {
            RequestUtil.setConf(request, PropertiesConfiguration.getInstance());
        }
        if (retry != null) {
            RequestUtil.setRetryCount(request, retry);
        }

        Response response = new Response();
        ServiceBase serviceProcessor = ProcessorFactory.getService(null);
        serviceProcessor.invoke(request, response);

        return response;
    }

    public static String getProxy(String url) throws Exception {
        AbstractProcessorContext context = ProcessContextHolder.getProcessorContext();

        return getProxy(context, url);
    }

    public static String getProxy(AbstractProcessorContext context, String url) throws Exception {
        if (context instanceof SearchProcessorContext) {
            Proxy proxy = ((SearchProcessorContext) context).getProxy(url);
            if (proxy != null) {
                return proxy.format();
            }

            LOGGER.warn("Not found available proxy used for '{}', so used default instead!", url);
        }

        return null;
    }

}