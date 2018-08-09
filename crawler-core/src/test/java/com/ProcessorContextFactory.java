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

package com;

import java.util.Objects;

import com.datatrees.crawler.core.domain.Website;
import com.datatrees.crawler.core.domain.config.ExtractorConfig;
import com.datatrees.crawler.core.domain.config.SearchConfig;
import com.datatrees.crawler.core.processor.ExtractorProcessorContext;
import com.datatrees.crawler.core.processor.SearchProcessorContext;
import com.datatrees.crawler.core.util.xml.exception.ParseException;

/**
 * @author Jerry
 * @since 20:27 06/12/2017
 */
public final class ProcessorContextFactory {

    private ProcessorContextFactory() {
    }

    public static SearchProcessorContext createSearchProcessorContext(String website, String filePath) throws ParseException {
        Objects.requireNonNull(filePath);
        SearchConfig websiteConfig = TestHelper.getConfig(filePath, SearchConfig.class);

        Website web = new Website();
        web.setWebsiteName(website);
        web.setWebsiteDomain(website);
        web.setSearchConfig(websiteConfig);

        SearchProcessorContext context = new SearchProcessorContext(web, 0L);

        context.init();

        return context;
    }

    public static ExtractorProcessorContext createExtractorProcessorContext(String website, String filePath) throws ParseException {
        Objects.requireNonNull(filePath);
        ExtractorConfig extractorConfig = TestHelper.getConfig(filePath, ExtractorConfig.class);

        Website web = new Website();
        web.setWebsiteName(website);
        web.setWebsiteDomain(website);
        web.setExtractorConfig(extractorConfig);

        ExtractorProcessorContext context = new ExtractorProcessorContext(web, 0L);
        context.init();
        return context;
    }
}