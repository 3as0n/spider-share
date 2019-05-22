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

package com.treefinance.crawler.framework.format.special;

import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.format.CommonFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.crawler.framework.protocol.util.UrlUtils;
import com.treefinance.crawler.framework.util.ServiceUtils;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 00:43 2018/6/2
 */
public class ResourceStringFormatter extends CommonFormatter<String> {

    @Override
    protected String toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String output;
        AbstractProcessorContext processorContext = config.getProcessorContext();
        boolean isSearchProcess = processorContext instanceof SearchProcessorContext;
        if (isSearchProcess && UrlUtils.isUrl(value)) {
            LinkNode linkNode = new LinkNode(value);
            AbstractService service = processorContext.getDefaultService();
            output = ServiceUtils.invokeAsString(service, linkNode, processorContext, null, null);
        } else {// html file
            if (!isSearchProcess) {
                logger.warn("ResourceString formatter must be effective during search processing.");
            }
            output = value;
        }
        return output;
    }
}
