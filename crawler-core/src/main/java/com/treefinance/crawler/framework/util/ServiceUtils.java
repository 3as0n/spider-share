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

package com.treefinance.crawler.framework.util;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderRequestFactory;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.InvokeException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.service.ServiceBase;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * notice: service invoking must be effective in search processing context
 * 
 * @author Jerry
 * @since 01:10 2018/7/24
 */
public final class ServiceUtils {

    public static String invokeAsString(AbstractService service, LinkNode linkNode, AbstractProcessorContext processorContext, Configuration configuration,
        Map<String, Object> extra) throws InvokeException, ResultEmptyException {
        SpiderResponse response = invoke(service, linkNode, processorContext, configuration, extra, null);

        return StringUtils.defaultString((String)response.getOutPut());
    }

    public static SpiderResponse invoke(AbstractService service, LinkNode linkNode, AbstractProcessorContext processorContext, Configuration configuration,
        Map<String, Object> extra) throws InvokeException, ResultEmptyException {
        return invoke(service, linkNode, processorContext, configuration, extra, null);
    }

    public static SpiderResponse invoke(AbstractService service, LinkNode linkNode, AbstractProcessorContext processorContext, Configuration configuration,
        Map<String, Object> extra, Integer retry) throws InvokeException, ResultEmptyException {
        if (!(processorContext instanceof SearchProcessorContext)) {
            throw new UnsupportedOperationException("Service invoking must be effective in search processing context.");
        }

        SpiderRequest request = SpiderRequestFactory.make();
        RequestUtil.setCurrentUrl(request, linkNode);
        request.setProcessorContext(processorContext);
        request.setConfiguration(configuration == null ? PropertiesConfiguration.getInstance() : configuration);
        if (extra != null) {
            request.setVisibleScope(extra);
        }
        if (retry != null) {
            RequestUtil.setRetryCount(request, retry);
        }

        SpiderResponse response = SpiderResponseFactory.make();

        ServiceBase serviceProcessor = ProcessorFactory.getService(service);
        serviceProcessor.invoke(request, response);

        return response;
    }
}
