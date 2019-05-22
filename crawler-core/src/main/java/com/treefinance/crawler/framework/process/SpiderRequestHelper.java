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

package com.treefinance.crawler.framework.process;

import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.consts.SpiderRequestAttrs;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;

/**
 * @author Jerry
 * @since 20:35 2018/8/7
 */
public final class SpiderRequestHelper {

    private SpiderRequestHelper() {}

    public static Boolean isKeepSegmentProcessingData(SpiderRequest request) {
        return Boolean.TRUE.equals(request.getAttribute(SpiderRequestAttrs.KEEP_PROCESSING_DATA));
    }

    public static void setKeepSegmentProcessingData(SpiderRequest request, boolean flag) {
        request.setAttribute(SpiderRequestAttrs.KEEP_PROCESSING_DATA, flag);
    }

    /**
     * must be invoking in search processing context.
     */
    public static SearchTemplateConfig getTemplateConfig(SpiderRequest request) {
        SearchProcessorContext context = (SearchProcessorContext)request.getProcessorContext();

        String templateId = RequestUtil.getCurrentTemplateId(request);

        return context.getSearchTemplateConfig(templateId);
    }

}
