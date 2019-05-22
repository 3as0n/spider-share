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

package com.datatrees.spider.share.service.collector.worker.filter;

import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import org.apache.commons.lang.StringUtils;

/**
 * @author Jerry
 * @since 12:17 09/01/2018
 */
public final class TemplateFilter {

    private TemplateFilter() {}

    public static boolean isFilter(SearchTemplateConfig templateConfig, String expectedTemplateId) {
        if (StringUtils.isNotBlank(expectedTemplateId)) {
            return !expectedTemplateId.contains(templateConfig.getId());
        }

        return Boolean.FALSE.equals(templateConfig.getAutoStart());
    }
}
